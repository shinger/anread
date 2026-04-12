package com.anread.book.config;

import com.anread.book.listener.BookChatModelListener;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LLMConfig {

    @Value("${llm.aliyun.api.key}")
    private String ALIYUN_API_KEY;

    @Value("${llm.aliyun.api.embed_model}")
    private String ALIYUN_API_EMBED_MODEL;

    @Value("${llm.aliyun.api.chat_model}")
    private String ALIYUN_API_CHAT_MODEL;

    @Value("${llm.aliyun.api.url}")
    private String ALIYUN_API_URL;

    @Value("${llm.qdrant.host}")
    private String QDRANT_HOST;

    @Value("${llm.qdrant.key}")
    private String QDRANT_KEY;

    private final static String QDRANT_COLLECTION = "anread-book";

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(ALIYUN_API_KEY)
                .modelName(ALIYUN_API_EMBED_MODEL)  // 文本向量模型
                .baseUrl(ALIYUN_API_URL)
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(ALIYUN_API_KEY)
                .modelName(ALIYUN_API_CHAT_MODEL)
                .baseUrl(ALIYUN_API_URL)
//                .listeners(List.of(new BookChatModelListener()))
                .build();
    }

    // 创建Qdrant客户端
    @Bean
    public QdrantClient qdrantClient() {
        QdrantGrpcClient.Builder builder = QdrantGrpcClient.newBuilder(QDRANT_HOST, 6334, false);
        return new QdrantClient(builder.build());
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return QdrantEmbeddingStore.builder()
                .apiKey(QDRANT_KEY)
                .host(QDRANT_HOST)
                .port(6334)
                .collectionName(QDRANT_COLLECTION)
                .build();
    }
}
