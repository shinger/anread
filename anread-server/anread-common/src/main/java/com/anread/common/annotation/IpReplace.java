package com.anread.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：标记需要替换IP的字段
 */
@Target({ElementType.FIELD}) // 仅用于字段
@Retention(RetentionPolicy.RUNTIME) // 运行时保留，支持反射获取
@Documented
public @interface IpReplace {
    // 可选：定义注解属性，比如指定要替换的原始IP（默认127.0.0.1）
    String originalIp() default "127.0.0.1";
}