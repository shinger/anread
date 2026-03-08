package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
@TableName("user_shelf")
public class UserShelf {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 书本ID
     */
    private String bookId;
    /**
     * 更新时间戳
     */
    private Timestamp updateTimestamp;
}
