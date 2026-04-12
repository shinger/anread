package com.anread.common.dto;

import lombok.Data;

@Data
public class ChatMessageDto {
    private String userId;
    private String bookId;
    private String message;
    private String quotation;
}
