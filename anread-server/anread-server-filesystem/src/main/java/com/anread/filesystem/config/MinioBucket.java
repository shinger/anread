package com.anread.filesystem.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 全局常量类（Spring 管理）
@Component
public class MinioBucket {
    // 静态常量（不可修改，通过 Spring 配置初始化）
    public static String BUCKET_BOOKS;
    public static String BUCKET_COMMON;
    public static String BASE_URL;
    public static int PORT = 9000;

    // 非静态变量，用于接收 Spring 注入的配置值
    @Value("${minio.buckets.common}")
    private String bucketCommon;

    @Value("${minio.buckets.books}")
    private String bucketBooks;

    @Value("${minio.admin.endpoint}")
    private String endpoint;

    // Spring 初始化时执行（构造方法之后），为静态常量赋值
    @PostConstruct
    private void init() {
        // 为静态常量赋值（仅初始化一次，后续无法修改）
        BUCKET_BOOKS = this.bucketBooks;
        BUCKET_COMMON = this.bucketCommon;
        BASE_URL = this.endpoint + ":" + PORT;
    }
}