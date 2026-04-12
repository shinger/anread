package com.anread.filesystem.service.impl;

import com.anread.common.entity.CommonFile;
import com.anread.common.utils.CommonUtil;
import com.anread.common.utils.MD5Uitl;
import com.anread.common.enums.StateEnum;
import com.anread.common.entity.BookFile;
import com.anread.common.utils.RandomStringGenerator;
import com.anread.common.vo.FileVo;
import com.anread.filesystem.config.MinioBucket;
import com.anread.filesystem.mapper.BookFileMapper;
import com.anread.filesystem.mapper.CommonFileMapper;
import com.anread.filesystem.service.FileService;
import com.anread.filesystem.utils.EpublibUtil;
import com.anread.filesystem.utils.MinioUtil;
import com.anread.common.dto.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private BookFileMapper fileMapper;

    @Autowired
    private CommonFileMapper commonFileMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private EpublibUtil epublibUtil;

    @Override
    public Result<FileVo> uploadBook(MultipartFile file) {
        // 读取文件字节数组
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取文件MD5
        String md5 = MD5Uitl.generateMD5(fileBytes);
        // 判断文件是否存在
        BookFile existFile = fileMapper.selectOne(new LambdaQueryWrapper<BookFile>()
                .eq(BookFile::getId, md5));
        if (existFile != null) {
            FileVo fileVo = FileVo.builder()
                    .id(md5)
                    .fileUrl(existFile.getFileUrl())
                    .coverImg(MinioBucket.BASE_URL + "/" + MinioBucket.BUCKET_BOOKS + "/" + md5 + "/cover.jpeg")
                    .build();
            // 返回
            return Result.<FileVo>success().data(fileVo).state(StateEnum.UPLOAD_SUCCESS);
        }

        // 文件名
        String filename = "book_file_" + RandomStringGenerator.generate(8) + ".epub";
        // 书名
        String bookName = filename.split("\\.")[0];
        // 文件类型
        String contentType = file.getContentType();
        // 提取封面字节
        byte[] coverBytes = epublibUtil.extractCover(file);
        // 存入Minio
        minioUtil.uploadEpub(md5, filename, contentType, coverBytes, fileBytes);

        String fileUrl = MinioBucket.BASE_URL + "/" + MinioBucket.BUCKET_BOOKS + "/" + md5 + "/" + filename;

        fileMapper.insert(BookFile.builder()
                .id(md5)
                .filename(filename)
                .bucket("books")
                .path(md5 + "/" + filename)
                .fileUrl(fileUrl)
                .reference(false)
                .build());

        log.info("==Mysql Insert File " + md5 + "成功！==");

        FileVo fileVo = FileVo.builder()
                .id(md5)
                .fileUrl(fileUrl)
                .coverImg(coverBytes != null ? MinioBucket.BASE_URL + "/" + MinioBucket.BUCKET_BOOKS + "/" + md5 + "/cover.jpeg" : MinioBucket.BASE_URL + "/" + MinioBucket.BUCKET_CONFIG + "/images/book_cover_default.png" )
                .build();
        // 返回
        return Result.<FileVo>success().data(fileVo).state(StateEnum.UPLOAD_SUCCESS);
    }

    @Override
    public Result<String> uploadCommonFile(MultipartFile file) {
        // 读取文件字节数组
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取文件MD5
        String md5 = MD5Uitl.generateMD5(fileBytes);
        // 判断文件是否存在
        CommonFile commonFile = commonFileMapper.selectById(md5);
        if (commonFile != null) {
            return Result.<String>success().data(commonFile.getImageUrl());
        }

        // 文件名
        String filename = file.getOriginalFilename();
        // 文件类型
        String contentType = file.getContentType();
        // 日期路径
        String datePath = CommonUtil.generateDatePath();
        // 存入Minio
        try {
            minioUtil.uploadCommonFile(md5, filename, contentType, fileBytes);
        } catch (Exception e) {
            log.error("Minio upload exception", e);
            return Result.error("File upload exception");
        }

        String postfix = filename.split("\\.")[1];
        String imgUrl = MinioBucket.BASE_URL + "/" + MinioBucket.BUCKET_COMMON + "/" + datePath + md5 + "." + postfix;

        commonFileMapper.insert(CommonFile.builder()
                .id(md5)
                .filename(filename)
                .path(datePath + md5 + "." + postfix)
                .referenceCount(0)
                .imageUrl(imgUrl)
                .build());

        log.info("==Mysql Insert File " + filename + "成功！==");

        // 返回
        return Result.<String>success().data(imgUrl).state(StateEnum.UPLOAD_SUCCESS);
    }

    @Override
    public Result<String> getEpubURL(String fileId) {
        BookFile bookFile = fileMapper.selectById(fileId);
        String epubURL = minioUtil.getEpubURL(bookFile.getPath());
        if (epubURL == null) {
            return Result.error(StateEnum.BUSSINESS_ERROR);
        }
        return Result.<String>success().data(epubURL);
    }

    @Override
    public Result deleteCommonFile(String filePath) {
        if (filePath.startsWith("http")) {
            filePath = filePath.substring(filePath.lastIndexOf("common/")+7, filePath.length());
        }
        // 删除Minio文件
        minioUtil.deleteCommonFile(filePath);
        // 删除数据库文件
        commonFileMapper.delete(new LambdaQueryWrapper<CommonFile>()
                .eq(CommonFile::getPath, filePath));
        return Result.success();
    }
}
