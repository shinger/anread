package com.anread.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadingRecordVO {
    /**
     * 阅读状态
     */
    private Integer readingStatus;
    /**
     * 最后阅读位置
     */
    private String lastReadCfi;
    /**
     * 阅读进度
     */
    private Float readingProgress;
    /**
     * 阅读时长
     */
    private Integer readingDuration;
}
