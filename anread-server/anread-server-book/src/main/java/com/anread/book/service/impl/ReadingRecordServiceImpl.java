package com.anread.book.service.impl;

import com.anread.book.repositry.BookRepository;
import com.anread.common.dto.ReadingRecordDto;
import com.anread.common.dto.Result;
import com.anread.common.entity.ReadingRecord;
import com.anread.common.enums.ReadingStatus;
import com.anread.common.vo.ReadingRecordVO;
import com.anread.book.mapper.ReadingRecordMapper;
import com.anread.book.service.ReadingRecordService;
import com.anread.feign.IUserClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReadingRecordServiceImpl implements ReadingRecordService {

    @Autowired
    private ReadingRecordMapper readingRecordMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IUserClient userClient;

    @Transactional
    @Override
    public Result uploadRecord(String userId, ReadingRecordDto readingRecordDto) {
        if (readingRecordDto.getReadingDuration() == 0) {
            return Result.success();
        }

        // 先更新个人信息统计时长
        Result result = userClient.incrementReadingRecord(userId, readingRecordDto.getReadingDuration());
        if (!result.isSuccess()) {
            return result;
        }
        // 查找是否第一次阅读
        ReadingRecord existReadingRecord = readingRecordMapper.selectOne(new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, readingRecordDto.getBookId()));

        ReadingRecord readingRecordNew = new ReadingRecord();
        BeanUtils.copyProperties(readingRecordDto, readingRecordNew);
        readingRecordNew.setReadingProgress(readingRecordDto.getReadingProgress());
        readingRecordNew.setUserId(userId);
//        readingRecordNew.setReadingStatus(ReadingStatus.UNREAD.getStatus());


        if (existReadingRecord == null) {
            // 第一次阅读，直接插入
            if (readingRecordNew.getReadingDuration() > 20) {
                readingRecordNew.setReadingStatus(ReadingStatus.READING.getStatus());
            }

            readingRecordMapper.insert(readingRecordNew);
            return Result.success();
        }
        readingRecordNew.setReadingStatus(existReadingRecord.getReadingStatus());

        // 第二次阅读
        // 计算新时长
        int newDuration = readingRecordDto.getReadingDuration()
                + existReadingRecord.getReadingDuration();
        readingRecordNew.setReadingDuration(newDuration);

        // 时长 > 20 分钟且之前是未读状态，则更新阅读状态为在读
        if (existReadingRecord.getReadingStatus().equals(ReadingStatus.UNREAD.getStatus())
                && newDuration > 20) {
            readingRecordNew.setReadingStatus(ReadingStatus.READING.getStatus());
        }


        // 更新
        readingRecordMapper.update(readingRecordNew, new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, readingRecordDto.getBookId()));

        return Result.success();
    }

    @Override
    public Result<ReadingRecordVO> getRecords(String userId, String bookId) {
        ReadingRecord readingRecord = readingRecordMapper.selectOne(new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, bookId));

        // 如果没有记录，直接返回0
        if (readingRecord == null) {
            return Result.<ReadingRecordVO>success().data(null);
        }
        ReadingRecordVO readingRecordVO = new ReadingRecordVO();
        BeanUtils.copyProperties(readingRecord, readingRecordVO);
        return Result.<ReadingRecordVO>success().data(readingRecordVO);
    }


    /**
     * 阅读完毕更新
     * @param userId 用户id
     * @param bookId 书本id
     * @return
     */
    @Override
    public Result readFinished(String userId, String bookId) {
        ReadingRecord readingRecord = readingRecordMapper.selectOne(new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getBookId, bookId)
                .eq(ReadingRecord::getUserId, userId));
        // 如果已经阅读完毕，则更新为在读
        if (readingRecord.getReadingStatus() == ReadingStatus.READOVER.getStatus()) {
            readingRecordMapper.update(new LambdaUpdateWrapper<ReadingRecord>()
                    .eq(ReadingRecord::getBookId, bookId)
                    .eq(ReadingRecord::getUserId, userId)
                    .set(ReadingRecord::getReadingProgress, 100F)
                    .set(ReadingRecord::getReadingStatus, ReadingStatus.READING.getStatus()));
            bookRepository.findById(bookId).subscribe(book -> {
                book.setReadingOverCount(book.getReadingOverCount() - 1);
                bookRepository.save(book).subscribe();
            });
            return Result.success();
        }
        // 更新阅读进度为100%，状态为已读
        readingRecordMapper.update(new LambdaUpdateWrapper<ReadingRecord>()
                .eq(ReadingRecord::getBookId, bookId)
                .eq(ReadingRecord::getUserId, userId)
                .set(ReadingRecord::getReadingProgress, 100F)
                .set(ReadingRecord::getReadingStatus, ReadingStatus.READOVER.getStatus()));
        bookRepository.findById(bookId).subscribe(book -> {
            book.setReadingOverCount(book.getReadingOverCount() + 1);
            bookRepository.save(book).subscribe();
        });
        return Result.success();
    }
}
