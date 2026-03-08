package com.anread.filesystem.service;

import com.anread.common.dto.Result;
import com.anread.common.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface FileService {
    /**
     * 上传书本
     * @param file Epub格式文件
     */
    Result<FileVo> uploadBook(MultipartFile file);

    /**
     * 上传文件
     * @param file
     * @return 文件URL
     */
    Result<String> uploadCommonFile(MultipartFile file);

    /**
     * 获取Epub文件URL
     * @param fileId
     * @return
     */
     Result<String> getEpubURL(String fileId);

     /**
     * 删除文件
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    Result deleteCommonFile(String filePath);
}
