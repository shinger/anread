package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@TableName("user")
public class User {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像URL
     */
    private String avatar;
    /**
     * 阅读时长
     */
    private Integer readingDuration;
    /**
     * 关注者数量
     */
    private Integer followers;
    /**
     * 点赞数量
     */
    private Integer likesCount;
    /**
     * 认证令牌
     */
    private String token;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
