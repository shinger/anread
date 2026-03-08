package com.anread.filesystem.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Slf4j
@DependsOn("minioBucket")
public class MinioConfiguration {

    @Value("${minio.admin.endpoint}")
    private String endpoint;

    @Value("${minio.admin.accessKey}")
    private String accessKey;

    @Value("${minio.admin.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint, 9000, false)
                .credentials(accessKey, secretKey)
                .build();
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(MinioBucket.BUCKET_BOOKS).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().
                        bucket(MinioBucket.BUCKET_BOOKS).build());
            }
            found = minioClient.bucketExists(BucketExistsArgs.builder()
                        .bucket(MinioBucket.BUCKET_COMMON).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().
                        bucket(MinioBucket.BUCKET_COMMON).build());
            }
        } catch (Exception e) {
            log.error("Minio初始化失败: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return minioClient;
    }

}
