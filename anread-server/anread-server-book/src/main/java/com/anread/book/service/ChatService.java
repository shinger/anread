package com.anread.book.service;

import com.anread.common.dto.AiChatRecordDto;
import com.anread.common.dto.ChatMessageDto;
import com.anread.common.dto.Result;
import com.anread.common.entity.AiChatRecord;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    /**
     * 对话
     * @param request 对话消息
     * @return 对话结果
     */
    Flux<String> chat(ChatMessageDto request);

    /**
     * 带增强检索的对话接口
     * @param request
     * @return
     */
    Flux<String> chatRAG(ChatMessageDto request);

    /**
     * 上传对话记录
     * @param request 对话记录
     * @return 上传结果
     */
    Result<Long> uploadRecord(AiChatRecordDto request);

    /**
     * 获取对话记录
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 对话记录
     */
    Result<List<AiChatRecord>> getRecords(String userId, String bookId);

    /**
     * 更新对话记录
     * @param record 对话记录
     * @return 更新结果
     */
    Result updateRecord(AiChatRecord record);

    /**
     * 删除对话记录
     * @param id 对话记录ID
     * @return 删除结果
     */
    Result deleteRecord(Long id);


}
