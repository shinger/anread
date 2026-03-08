package com.anread.filesystem.controller;

import com.anread.common.dto.Result;
import com.anread.common.enums.StateEnum;
import com.anread.common.exception.BizException;
import com.anread.common.vo.FileVo;
import com.anread.filesystem.service.FileServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

/**
 * 文件服务响应式接口
 */
@RestController
@RequestMapping("/v2/file")
public class FileControllerV2 {

    @Autowired
    private FileServiceV2 fileServiceV2;

    /**
     * 上传书本
     * @param filePart Epub格式文件
     * @return 文件ID及封面
     */
    @PostMapping("/book")
    public Mono<Result<FileVo>> uploadBook(@RequestParam("file") Mono<FilePart> filePart) {
        return fileServiceV2.uploadBook(filePart)
                .switchIfEmpty(Mono.error(new BizException(StateEnum.REQUEST_DATA_ERROR)));

    }
}
