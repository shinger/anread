package com.anread.book.listener;

import com.anread.book.mapper.AiChatRecordMapper;
import com.anread.common.entity.AiChatRecord;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class BookChatModelListener implements ChatModelListener {

    @Autowired
    private AiChatRecordMapper aiChatRecordMapper;

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        ChatModelListener.super.onRequest(requestContext);
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        String answer = responseContext.chatResponse().aiMessage().text();
        log.info("AI answer: {}", answer);
        // 存储到数据库
//        AiChatRecord aiChatRecord = new AiChatRecord();
//        aiChatRecord.setUserId(userId);
//        aiChatRecord.setBookId(bookId);
//        aiChatRecord.setQuestion(question);
//        aiChatRecord.setQuotation(quotation);
//        aiChatRecord.setAnswer(answer);
//        aiChatRecord.setCreateTime(LocalDateTime.now());
//        aiChatRecordMapper.insert(aiChatRecord);
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        ChatModelListener.super.onError(errorContext);
    }
}
