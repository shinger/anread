package com.anread.filesystem.controller;

import com.anread.common.enums.StateEnum;
import com.anread.common.exception.BizException;
import com.anread.common.vo.FileVo;
import com.anread.filesystem.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.anread.common.dto.Result;
import reactor.core.publisher.Mono;

/**
 * 文件服务接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传书本
     * @param file Epub格式文件
     * @return 文件ID及封面
     */
    @PostMapping("/book")
    public Result<FileVo> uploadBook(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException(StateEnum.REQUEST_DATA_ERROR);
        }

        return fileService.uploadBook(file);
    }

    /**
     * 上传文件
     * @param file 文件
     * @return 文件URL
     */
    @PostMapping("/common")
    public Result<String> uploadCommonFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException(StateEnum.REQUEST_DATA_ERROR);
        }

        return fileService.uploadCommonFile(file);
    }

    /**
     * 删除文件
     * @param filePath 文件
     * @return 是否删除成功
     */
    @DeleteMapping("/common")
    public Result deleteCommonFile(@RequestParam("filePath") String filePath) {
        if (filePath == null) {
            throw new BizException(StateEnum.REQUEST_DATA_ERROR);
        }

        return fileService.deleteCommonFile(filePath);
    }

    /**
     * 获取Epub文件URL
     * @param fileId 文件ID
     * @return Epub文件URL
     */
    @GetMapping("/{fileId}")
    public Result<String> getEpubURL(@PathVariable("fileId") String fileId){
        return fileService.getEpubURL(fileId);
    }


}
