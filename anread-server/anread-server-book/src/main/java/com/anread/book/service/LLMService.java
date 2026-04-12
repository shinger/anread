package com.anread.book.service;

import com.anread.book.config.ChatMemoryFactory;
import com.anread.book.listener.BookContentRetrieverListener;
import com.anread.book.mapper.AiChatRecordMapper;
import com.anread.book.repository.BookRepository;
import com.anread.common.entity.AiChatRecord;
import com.anread.common.entity.Book;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import io.qdrant.client.QdrantClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Slf4j
@Service
public class LLMService {
    
    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    @Autowired
    private AiChatRecordMapper aiChatRecordMapper;

    @Autowired
    private BookRepository bookRepository;

    // OpenAI embedding 模型的最大 token 限制
    private static final int MAX_CHUNK_SIZE = 2000;
    private static final int CHUNK_OVERLAP = 100;

    // 工具方法：将列表分批
    private <T> List<List<T>> partition(List<T> list, int batchSize) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            partitions.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return partitions;
    }
    
    public List<TextSegment> search(String query, String bookId, int maxResults) {
        Embedding embedding = embeddingModel.embed(query).content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding)
                .filter(new IsEqualTo("book_id", bookId))
                .maxResults(3)
                .build();

        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);
        List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();
        List<TextSegment> list = matches.stream().map(EmbeddingMatch::embedded).toList();
        return list;
    }

    public Flux<String> answerQuestionStreaming(String userId, String bookId, String question, String quotation) {
        log.info("用户{}对{}提问：{}", userId, bookId, question);
        // 1. 构建图书信息
        Book book = bookRepository.findById(bookId).block();
        String bookInfo = null;
        if (book == null) {
            log.error("未找到图书：{}", bookId);
            throw new RuntimeException("未找到图书：" + bookId);
        } else {
            bookInfo = String.format(
                    "书名：%s\n作者：%s\n简介：%s\n出版社：%s\n出版年份：%s\n出版月份：%s",
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIntroduction(),
                    book.getPress(),
                    book.getPressYear(),
                    book.getPressMonth()
            );
        }

        // 2. 创建带历史的 ChatMemory
        ChatMemory chatMemory = new ChatMemoryFactory(aiChatRecordMapper)
                .createChatMemory(userId, bookId);

        // 3. 创建 RetrievalAugmentor
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(1)
                .dynamicFilter(query -> {
                    String metaBookId = query.metadata().invocationParameters().get("bookId");
                    return metadataKey("book_id").isEqualTo(metaBookId);
                })
                .build();

        // 注册事件监听
        contentRetriever = contentRetriever.addListener(new BookContentRetrieverListener());

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .build();

        // 4. 构建 AiService（使用 streaming model）
        BookQaAiService aiService = AiServices.builder(BookQaAiService.class)
                .streamingChatModel(streamingChatModel)
                .chatMemory(chatMemory)
                .retrievalAugmentor(retrievalAugmentor)
                .build();

        // 5. 返回结果
        AtomicReference<String> fullAnswerRef = new AtomicReference<>("");
        InvocationParameters parameters = InvocationParameters.from(Map.of("bookId", bookId, "userId", userId));
        return aiService.chat(
                "引用：" + (quotation!=null ? quotation : "无引用内容") + "\n提问：" + question,
                        bookInfo,
                        parameters)
                .doOnNext(chunk -> {
                    fullAnswerRef.updateAndGet(s -> s+chunk);
                })
                .doOnComplete(() -> {
                    String answer = fullAnswerRef.get();
                    log.info("完整回答：{}", answer);
                    AiChatRecord aiChatRecord = new AiChatRecord();
                    aiChatRecord.setUserId(userId);
                    aiChatRecord.setBookId(bookId);
                    aiChatRecord.setQuestion(question);
                    aiChatRecord.setQuotation(quotation);
                    aiChatRecord.setAnswer(answer);
                    aiChatRecord.setCreateTime(LocalDateTime.now());
                    aiChatRecordMapper.insert(aiChatRecord);
                });
    }
    
    public void close() {
        // 清理资源
        if (embeddingStore instanceof AutoCloseable) {
            try {
                ((AutoCloseable) embeddingStore).close();
            } catch (Exception e) {
                System.err.println("Error closing embedding store: " + e.getMessage());
            }
        }
    }
}