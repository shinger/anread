package com.anread.feign;

import com.anread.common.dto.Result;
import com.anread.common.vo.FileVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(url = "http://127.0.0.1:8002", name = "anread-filesystem")
public interface IFileClient {

    @PostMapping(value = "/file/common", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> uploadCommonFile(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/file/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<FileVo> uploadBook(@RequestPart("file") MultipartFile file);

    @GetMapping("/file/{fileId}")
    Result<String> getEpubURL(@PathVariable("fileId") String fileId);

    @DeleteMapping("/common")
    Result deleteCommonFile(@RequestParam("filePath") String filePath);
}
