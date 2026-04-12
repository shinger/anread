package com.anread.book.listener;

import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverErrorContext;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverListener;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverRequestContext;
import dev.langchain4j.rag.content.retriever.listener.ContentRetrieverResponseContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookContentRetrieverListener implements ContentRetrieverListener {
    @Override
    public void onRequest(ContentRetrieverRequestContext requestContext) {
        String question = requestContext.query().text();
        log.info("Book question: " + question);
    }

    @Override
    public void onResponse(ContentRetrieverResponseContext responseContext) {
        for (Content content : responseContext.contents()) {
            String text = content.textSegment().text();
            log.info("RAG Book content: {}", text);
        }
    }

    @Override
    public void onError(ContentRetrieverErrorContext errorContext) {
        String message = errorContext.error().getMessage();
        log.error("RAG Book error: {}", message);
    }
}
