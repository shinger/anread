package com.anread.book.config;

import com.anread.book.mapper.AiChatRecordMapper;
import com.anread.common.entity.AiChatRecord;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class ChatMemoryFactory {
    private final AiChatRecordMapper aiChatRecordMapper;

    public ChatMemoryFactory(AiChatRecordMapper aiChatRecordMapper) {
        this.aiChatRecordMapper = aiChatRecordMapper;
    }

    public ChatMemory createChatMemory(String userId, String bookId) {
        // 限制12条历史消息
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(12);

        List<AiChatRecord> aiChatRecords = aiChatRecordMapper.selectList(new LambdaQueryWrapper<AiChatRecord>()
                .eq(AiChatRecord::getUserId, userId)
                .eq(AiChatRecord::getBookId, bookId));

        for (AiChatRecord record : aiChatRecords) {
            String userContent = "引用：" + (record.getQuotation() != null ? record.getQuotation() : "无引用信息") +
                    "\n提问：" + record.getQuestion();
            chatMemory.add(new UserMessage(userContent));
            chatMemory.add(new AiMessage(record.getAnswer()));
        }

        return chatMemory;
    }
}
