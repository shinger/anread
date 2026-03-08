package com.anread.common.dto;

import lombok.Data;

@Data
public class UserDto {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
}
