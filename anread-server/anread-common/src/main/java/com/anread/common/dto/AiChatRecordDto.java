package com.anread.common.dto;

import lombok.Data;

/**
 * AI对话记录DTO
 */
@Data
public class AiChatRecordDto {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 书本ID
     */
    private String bookId;
    /**
     * 问题
     */
    private String question;
    /**
     * 引用
     */
    private String quotation;
    /**
     * 回答
     */
    private String answer;
}
