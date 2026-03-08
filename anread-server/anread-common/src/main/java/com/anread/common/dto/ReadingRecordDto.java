package com.anread.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadingRecordDto {
    /**
     * 书本id
     */
    private String bookId;
    /**
     * 最后读取的cfi
     */
    private String lastReadCfi;
    /**
     * 阅读时长
     */
    private Integer readingDuration;
    /**
     * 阅读进度
     */
    private Float readingProgress;
}