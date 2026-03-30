package com.anread.filesystem.utils;

import com.anread.common.entity.Book;
import com.anread.common.enums.StateEnum;
import com.anread.common.exception.BizException;
import com.anread.common.utils.CommonUtil;
import com.anread.filesystem.config.MinioBucket;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@DependsOn("minioBucket")
public class MinioUtil {



    @Autowired
    private MinioClient minioClient;

    /**
     * 上传普通文件到Minio
     * 按照日期路径索引
     * @param fileId 文件ID
     * @param filename 文件名
     * @param contentType 文件MIME类型
     * @param fileBytes 文件字节数组
     */
    public void uploadCommonFile(String fileId, String filename, String contentType, byte[] fileBytes) {
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileBytes);
        String suffix = filename.split("\\.")[1];
        String datePath = CommonUtil.generateDatePath();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MinioBucket.BUCKET_COMMON) // 存储桶名称
                            .object(datePath + fileId + "." + suffix) // 文件名
                            .stream(fileInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(contentType) // MIME类型
                            .build()
            );
            log.info("【Minio - Upload Common File 】 路径：{} 信息：上传成功", datePath + fileId + "." + suffix);
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            log.error("【Minio - Upload Common File 】 文件名：{} 信息：{}", filename, e.getMessage());
            throw new BizException(StateEnum.UPDATE_FAILED);
        }
    }

    public String getEpubURL(String bookPath) {
        String presignedObjectUrl = null;
        try {
            presignedObjectUrl = minioClient
                                    .getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(MinioBucket.BUCKET_BOOKS)
                                    .object(bookPath)
                                    .expiry(1, TimeUnit.DAYS)
                                    .build());
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            log.error("【Minio - Get Epub File URL 】 路径：{} 信息：{}", bookPath, e.getMessage());
            throw new BizException(StateEnum.BUSSINESS_ERROR);
        }

        if (presignedObjectUrl == null) {
            log.error("【Minio - Get Epub File URL 】 路径：{} 信息：获取失败", bookPath);
            throw new BizException(StateEnum.BUSSINESS_ERROR);
        }

        log.info("【Minio - Get Epub File URL 】 路径：{} 信息：获取成功", bookPath);
        return presignedObjectUrl;
    }

    public byte[] getEpub(Book book) {
        byte[] bytes = null;
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(MinioBucket.BUCKET_BOOKS)
                            .object(book.getPath())
                            .build()
            );
            bytes = response.readAllBytes();
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * 根据书名获取封面
     * @param bookName
     * @return
     */
    public byte[] getCover(String bookName) {
        byte[] bytes = null;
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(MinioBucket.BUCKET_BOOKS)
                            .object(bookName + "/cover.jpeg")
                            .build()
            );
            bytes = response.readAllBytes();
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            log.error("【Minio - Get Cover File 】 书名：{} 信息：{}", bookName, e.getMessage());
            throw new BizException(StateEnum.UPDATE_FAILED);
        }

        return bytes;
    }

    public void uploadEpub(String fileId, String fileName, String contentType, byte[] coverBytes, byte[] fileBytes) {
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileBytes);
        ByteArrayInputStream coverInputStream = coverBytes != null ? new ByteArrayInputStream(coverBytes) : null;
        String bookName = fileName.split("\\.")[0];
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MinioBucket.BUCKET_BOOKS) // 存储桶名称
                            .object(fileId + "/" + fileName) // 文件名
                            .stream(fileInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(contentType) // MIME类型
                            .build()
            );
            log.info("【Minio - Upload Epub File 】 路径：{} 信息：上传成功", fileId + "/" + fileName);
            if (coverInputStream != null) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket("books") // 存储桶名称
                                .object(fileId + "/cover.jpeg") // 文件名
                                .stream(coverInputStream, coverBytes.length, -1) // 字节数组输入流
                                .contentType("image/jpeg") // MIME类型
                                .build()
                );
                log.info("【Minio - Upload Epub File 】 路径：{} 信息：上传成功", fileId + "/cover.jpeg");
            }
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            log.error("【Minio - Upload Epub File 】 路径：{} 信息：{}", fileId + "/" + fileName, e.getMessage());
            throw new BizException(StateEnum.UPDATE_FAILED);
        }
    }

    /**
     * 将 epub 内容资源上传到 minio
     * @param index 资源序号
     * @param bookName 书名
     * @param mediaType 媒体类型
     * @param fileBytes 文件字节数据
     */
    public void uploadBookContent(String bookName, int index, MediaType mediaType, byte[] fileBytes) {
        // 上传字节数组到MinIO
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MinioBucket.BUCKET_BOOKS) // 存储桶名称
                            .object(bookName + "/" + index + mediaType.getDefaultExtension()) // 文件名
                            .stream(byteArrayInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(mediaType.getName()) // MIME类型
                            .build()
            );
            log.info("【Minio - Upload Book Content 】 路径：{} 信息：上传成功", bookName + "/" + index + mediaType.getDefaultExtension());
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            log.error("【Minio - Upload Book Content 】 路径：{} 信息：{}", bookName + "/" + index + mediaType.getDefaultExtension(), e.getMessage());
            throw new BizException(StateEnum.UPDATE_FAILED);
        }

    }

    /**
     * 将 epub 资源上传到 minio
     * @param fileName 文件名
     * @param bookName 书名
     * @param mediaType 媒体类型
     * @param fileBytes 文件字节数组
     */
    public void uploadBookResources(String bookName, String fileName,  MediaType mediaType, byte[] fileBytes) {
        // 上传字节数组到MinIO
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MinioBucket.BUCKET_BOOKS) // 存储桶名称
                            .object(bookName + "/" + fileName) // 文件名
                            .stream(byteArrayInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(mediaType.getName()) // MIME类型
                            .build()
            );
            log.info("【Minio - Upload Book Resources 】 路径：{} 信息：上传成功", bookName + "/" + fileName);
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            log.error("【Minio - Upload Book Resources 】 路径：{} 信息：{}", bookName + "/" + fileName, e.getMessage());
            throw new BizException(StateEnum.UPDATE_FAILED);
        }
    }

    /**
     * 删除Minio文件
     * @param filePath 文件路径
     */
    public void deleteCommonFile(String filePath) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(MinioBucket.BUCKET_COMMON) // 存储桶名称
                            .object(filePath) // 文件路径
                            .build()
            );
            log.info("【Minio - Delete Common File 】 路径：{} 信息：删除成功", filePath);
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            log.error("【Minio - Delete Common File 】 路径：{} 信息：{}", filePath, e.getMessage());
            throw new BizException(StateEnum.UPDATE_FAILED);
        }
    }
}
