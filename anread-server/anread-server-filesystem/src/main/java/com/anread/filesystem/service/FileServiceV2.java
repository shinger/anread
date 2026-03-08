package com.anread.filesystem.service;

import com.anread.common.dto.Result;
import com.anread.common.vo.FileVo;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileServiceV2 {
    /**
     * 上传书本
     * @param filePart Epub格式文件
     * @return 文件ID及封面
     */
    Mono<Result<FileVo>> uploadBook(Mono<FilePart> filePart);
}
