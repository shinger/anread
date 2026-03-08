package com.anread.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubCommentVO {
    /**
     * 子评论ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 子评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;
}
