package com.anread.filesystem.service;

import com.anread.common.entity.Chapter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import io.qdrant.client.QdrantClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class QdrantService {
    
    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private QdrantClient qdrantClient;

    // OpenAI embedding 模型的最大 token 限制
    private static final int MAX_CHUNK_SIZE = 2000; // 留点余量
    private static final int CHUNK_OVERLAP = 100;

    public void storeChapters(String bookId, String bookTitle, List<Chapter> chapters) {
        List<TextSegment> segments = new ArrayList<>();

        DocumentSplitter splitter = DocumentSplitters.recursive(
                MAX_CHUNK_SIZE,
                CHUNK_OVERLAP
        );

        for (Chapter chapter : chapters) {
            // 创建元数据
            Map<String, Object> baseMetadata = Map.of(
                    "book_id", bookId,
                    "book_title", bookTitle,
                    "chapter_title", chapter.getTitle(),
                    "chapter_index", String.valueOf(chapter.getChapterIndex()),
                    "level", String.valueOf(chapter.getLevel())
            );

            String content = chapter.getContent();

            if (content.length() <= 1000) {
                Metadata metadata = Metadata.from(baseMetadata);
                TextSegment segment = TextSegment.from(content, metadata);
                segments.add(segment);
                continue;
            }

            // 长章节：分块处理
            List<TextSegment> chunks = splitter.split(Document.from(content))
                    .stream()
                    .map(text -> TextSegment.from(text.text(), Metadata.from(baseMetadata)))
                    .toList();

            segments.addAll(chunks);
        }

        log.info("Total segments to embed: {}", segments.size());

        try {
            // 分批处理，每批10个 TextSegment
            List<List<TextSegment>> batches = partition(segments, 10);

            for (List<TextSegment> batch : batches) {
                // 1. 对当前 batch 的 TextSegment 进行 embedding
                Response<List<Embedding>> response = embeddingModel.embedAll(batch);
                List<Embedding> embeddings = response.content();

                // 2. 同时传入 embeddings 和对应的 TextSegments（顺序必须一致！）
                embeddingStore.addAll(embeddings, batch); // ← 关键修改！
            }

            log.info("Successfully stored {} segments to Qdrant", segments.size());
        } catch (Exception e) {
            log.error("Error storing chapters to Qdrant: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // 工具方法：将列表分批
    private <T> List<List<T>> partition(List<T> list, int batchSize) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            partitions.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return partitions;
    }
    
    public List<String> search(String query, String bookId, int maxResults) {
        Embedding embedding = embeddingModel.embed(query).content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding)
                .filter(new IsEqualTo("book_id", bookId))
                .maxResults(1)
                .build();

        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);
        List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();
        List<String> results = new ArrayList<>();
        if (matches != null && !matches.isEmpty()) {
            for (EmbeddingMatch<TextSegment> match : matches) {
                results.add(match.embedded().text());
            }
        }

        return results;
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