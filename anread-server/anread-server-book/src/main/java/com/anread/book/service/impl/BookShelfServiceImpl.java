package com.anread.book.service.impl;

import com.anread.book.mapper.ReadingRecordMapper;
import com.anread.book.repositry.BookRepository;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import com.anread.common.entity.ReadingRecord;
import com.anread.common.entity.UserShelf;
import com.anread.common.enums.StateEnum;
import com.anread.common.vo.ShelfBookVo;
import com.anread.book.mapper.BookShelfMapper;
import com.anread.book.mapper.UserShelfMapper;
import com.anread.book.service.BookShelfService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class BookShelfServiceImpl implements BookShelfService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserShelfMapper userShelfMapper;

    @Autowired
    private BookShelfMapper bookShelfMapper;

    @Autowired
    private ReadingRecordMapper readingRecordMapper;

    @Override
    public Mono<Result<List<ShelfBookVo>>> getBookList(String userId, Integer pageNum, long pageSize) {
        // 分页查询，默认每页30本
        Page<UserShelf> userShelfPage = bookShelfMapper.selectPage(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getUserId, userId)
                .orderByDesc(UserShelf::getUpdateTimestamp));
        List<UserShelf> userShelfList = userShelfPage.getRecords();
        // 检查是否还有更多数据
        boolean hasMore = userShelfPage.getPages() > pageNum;

        List<String> bookIdList = userShelfList.stream().map(UserShelf::getBookId).toList();
        return bookRepository.findByIds(bookIdList)
                .map(book -> {
                    // 查找阅读记录
                    ReadingRecord readingRecord = readingRecordMapper.selectOne(new LambdaQueryWrapper<ReadingRecord>()
                            .eq(ReadingRecord::getUserId, userId)
                            .eq(ReadingRecord::getBookId, book.getId()));
                    return ShelfBookVo.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .author(book.getAuthor())
                            .categoryId(book.getCategoryId())
                            .mainCategory(book.getMainCategory())
                            .subCategory(book.getSubCategory())
                            .cover(book.getCover())
                            .readFinished(readingRecord != null ? readingRecord.isReadFinished() : false)
                            .build();
                })
                .collectList()
                .map(bookList -> Result.<List<ShelfBookVo>>success().data(bookList));
    }


    @Override
    public Result join(String userId, String bookId) {
        // 查找书架是否存在
        UserShelf userShelf = userShelfMapper.selectOne(new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getUserId, userId)
                .eq(UserShelf::getBookId, bookId));
        if (userShelf != null) {
            log.info("【加入书架】用户{} 书本ID{} 已在书架内", userId, bookId);
            return Result.error("已在书架内");
        }
        // 插入用户-书架表
        userShelfMapper.insert(UserShelf.builder()
                .bookId(bookId)
                .userId(userId)
                .updateTimestamp(new Timestamp(System.currentTimeMillis()))
                .build());
        log.info("【加入书架】用户{} 书本ID{} 已加入书架", userId, bookId);
        // 更新阅读人数+1
        Book book = bookRepository.findById(bookId).block();
        if (book == null) {
            return Result.error(StateEnum.BUSSINESS_ERROR);
        }
        book.setReadership(book.getReadership() + 1);
        bookRepository.save(book).subscribe();
        log.info("【加入书架】书本ID{} 阅读人数已更新为{}", bookId, book.getReadership());

        return Result.success().message("已加入书架");
    }

    @Override
    public Result<Boolean> inShelf(String userId, String bookId) {
        // 查找书架是否存在
        UserShelf userShelf = userShelfMapper.selectOne(new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getUserId, userId)
                .eq(UserShelf::getBookId, bookId));
        if (userShelf == null) {
            log.info("【查询书架】用户{} 书本ID{} 不在书架内", userId, bookId);
        } else {
            log.info("【查询书架】用户{} 书本ID{} 在书架内", userId, bookId);
        }
        return Result.<Boolean>success().data(userShelf != null);
    }

    @Override
    public Result removeShelf(String userId, String bookId) {
        int delete = userShelfMapper.delete(new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getBookId, bookId)
                .eq(UserShelf::getUserId, userId));
        if (delete == 0) {
            log.info("【移出书架】用户{} 书本ID{} 不在书架内", userId, bookId);
            return Result.error("移出书架失败");
        }
        log.info("【移出书架】用户{} 书本ID{} 已移出书架", userId, bookId);
        // 更新阅读人数-1
        Book book = bookRepository.findById(bookId).block();
        if (book == null) {
            return Result.error(StateEnum.BUSSINESS_ERROR);
        }
        book.setReadership(book.getReadership() - 1);
        bookRepository.save(book).subscribe();
        log.info("【移出书架】书本ID{} 阅读人数已更新为{}", bookId, book.getReadership());

        return Result.success().data("已移出书架");
    }
}
