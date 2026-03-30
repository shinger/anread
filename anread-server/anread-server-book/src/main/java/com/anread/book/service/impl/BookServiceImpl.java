package com.anread.book.service.impl;

import com.anread.book.mapper.SysConfigMapper;
import com.anread.book.mapper.UserShelfMapper;
import com.anread.book.repository.BookRepository;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import com.anread.common.entity.Category;
import com.anread.common.entity.UserShelf;
import com.anread.common.exception.BizException;
import com.anread.book.mapper.CategoryMapper;
import com.anread.book.service.BookService;
import com.anread.feign.IFileClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IFileClient fileClient;

    @Autowired
    private UserShelfMapper userShelfMapper;

    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public Mono<Result<List<Book>>> getBooksByCategory(Integer categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return Mono.error(new BizException("分类不存在"));
        }
        Flux<Book> books = null;
        if (category.getParentId() == 0) { // 获取主分类的全部书本
            List<Category> subCategories = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                    .eq(Category::getParentId, categoryId));
            List<Integer> subCategoryIds = subCategories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            if (!subCategoryIds.isEmpty()) {
                books = bookRepository.findByCategoryIds(subCategoryIds);
            } else {
                books = Flux.empty();
            }
        } else {
            books = bookRepository.findByCategoryIds(Collections.singletonList(categoryId));
        }

        return books.collectList()
                .map(bookList -> {
                    log.info("【根据分类ID查询书本】 分类ID: {}, 书本数量: {}", categoryId, bookList.size());
                    return Result.<List<Book>>success().data(bookList);
                }).doOnError(e -> log.error("【根据分类ID查询书本】 异常: {}", e.getMessage()));
    }

    @Override
    public Mono<Result<Book>> getBookByBookId(String userId, String bookId) {
        return bookRepository.findById(bookId)
                .doOnSuccess(book -> {
                    // 更新用户书架
                    userShelfMapper.update(new LambdaUpdateWrapper<UserShelf>()
                            .eq(UserShelf::getUserId, userId)
                            .eq(UserShelf::getBookId, bookId)
                            .set(UserShelf::getUpdateTimestamp, new Timestamp(System.currentTimeMillis())));
                })
                .map(book -> {
                    log.info("【根据书本ID查询书本】 书本ID: {}", bookId);
                    return Result.<Book>success().data(book);
                })
                .switchIfEmpty(Mono.error(new BizException("书本不存在")))
                .doOnError(e -> log.error("【根据书本ID查询书本】 异常: {}", e.getMessage()));
    }

    @Override
    public Mono<Result<String>> getBookEpub(String userId, String bookId) {
        // 更新用户书架
        UserShelf userShelf = userShelfMapper.selectOne(new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getUserId, userId)
                .eq(UserShelf::getBookId, bookId));
        if (userShelf != null) {
            userShelfMapper.update(new LambdaUpdateWrapper<UserShelf>()
                    .eq(UserShelf::getUserId, userId)
                    .eq(UserShelf::getBookId, bookId)
                    .set(UserShelf::getUpdateTimestamp, new Timestamp(System.currentTimeMillis())));
        }

        // 从Redis缓存中获取书本
        return reactiveRedisTemplate.opsForValue().get(bookId)
                .switchIfEmpty(Mono.defer(() -> {
                    // 缓存中不存在，从数据库中查询
                    return bookRepository.findById(bookId)
                            .switchIfEmpty(Mono.error(new BizException("书本不存在")))
                            .onErrorMap(e -> new BizException("Feign远程调用失败", e.getMessage()))
                            .flatMap(book -> Mono.fromCallable(() -> fileClient.getEpubURL(book.getFileId()))
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .map(result -> {
                                        if (!result.isSuccess()) {
                                            throw new BizException("Feign远程调用失败", result.getMessage());
                                        }
                                        log.info("【Feign调用Minio获取Epub URL】 成功: {}", result.getData());
                                        return result.getData();
                                    }))
                            .flatMap(epubUrl -> {
                                log.info("【存储到Redis缓存】 书本ID: {}, Epub URL: {}", bookId, epubUrl);
                                return reactiveRedisTemplate.opsForValue()
                                        .set(bookId, epubUrl, Duration.ofDays(1).minusMinutes(5)) // 缓存1天 - 5分钟
                                        .thenReturn(epubUrl);
                            });
                }))
                .map(epubURL -> {
                    log.info("【根据书本ID查询书本Epub URL】 书本ID: {}, Epub URL: {}", bookId, epubURL);
                    return Result.<String>success().data(epubURL);
                })
                .doOnError(e -> log.error("【根据书本ID查询书本】 异常: {}", e.getMessage()));
    }

    @Override
    public Mono<Result<List<Book>>> searchBook(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Mono.error(new BizException("搜索关键词不能为空"));
        }
        Flux<Book> bookFlux = bookRepository.findByTitleOrAuthorContaining(keyword.trim());
        return bookFlux.collectList()
                .map(bookList -> {
                    log.info("【搜索书本】 关键词: {}, 书本数量: {}", keyword, bookList.size());
                    return Result.<List<Book>>success().data(bookList);
                }).doOnError(e -> log.error("【搜索书本】 异常: {}", e.getMessage()));
    }

}
