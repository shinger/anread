package com.anread.common.entity;

import com.anread.common.enums.ReadingStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("reading_record")
public class ReadingRecord {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 书本ID
     */
    private String bookId;
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

    public Boolean isReadFinished() {
        return readingStatus == ReadingStatus.READOVER.getStatus();
    }
}
