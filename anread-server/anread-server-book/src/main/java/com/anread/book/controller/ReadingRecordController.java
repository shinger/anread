package com.anread.book.controller;

import com.anread.common.dto.ReadingRecordDto;
import com.anread.common.dto.Result;
import com.anread.book.service.ReadingRecordService;
import com.anread.common.vo.ReadingRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 阅读记录服务接口
 */
@RestController
@RequestMapping("/reading/record")
public class ReadingRecordController {

    @Autowired
    private ReadingRecordService readingRecordService;

    /**
     * 上传阅读记录
     * @param readingRecordDto
     * @return
     */
    @PostMapping("/upload")
    public Result uploadRecords(@RequestHeader("X-User-ID") String userId, @RequestBody ReadingRecordDto readingRecordDto) {
        return readingRecordService.uploadRecord(userId, readingRecordDto);
    }

    /**
     * 获取阅读记录
     * @param bookId
     * @return 阅读记录
     */
    @GetMapping("/{bookId}")
    public Result<ReadingRecordVO> getRecords(@RequestHeader("X-User-ID") String userId, @PathVariable("bookId") String bookId) {
        return readingRecordService.getRecords(userId, bookId);
    }


    /**
     * 阅读完毕更新
     * @param bookId 书本id
     * @return
     */
    @PostMapping("/finished/{id}")
    public Result readFinished(@RequestHeader("X-User-ID") String userId, @PathVariable("id") String bookId) {
        return readingRecordService.readFinished(userId, bookId);
    }
}
