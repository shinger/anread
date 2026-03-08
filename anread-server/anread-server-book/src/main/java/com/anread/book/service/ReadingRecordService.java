package com.anread.book.service;

import com.anread.common.dto.ReadingRecordDto;
import com.anread.common.dto.Result;
import com.anread.common.vo.ReadingRecordVO;

public interface ReadingRecordService {

    /**
     * 上传阅读记录
     * @param readingRecordDto
     * @return
     */
    Result uploadRecord(String userId, ReadingRecordDto readingRecordDto);

    /**
     * 获取书本的阅读记录
     * @param bookId 书本id
     * @return 阅读记录
     */
    Result<ReadingRecordVO> getRecords(String userId, String bookId);

    /**
     * 阅读完毕更新
     * @param bookId 书本id
     * @return
     */
    Result readFinished(String userId, String bookId);
}
