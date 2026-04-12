package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiChatRecord {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
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
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
