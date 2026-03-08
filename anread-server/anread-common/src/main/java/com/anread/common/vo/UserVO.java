package com.anread.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 头像URL
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 关注者数量
     */
    private Integer followers;
    /**
     * 点赞数量
     */
    private Integer likesCount;
    /**
     * 阅读时长
     */
    private Integer readingDuration;
    /**
     * 用户名
     */
    private String username;
}
