package com.anread.book.service;

import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookService {

    /**
     * 根据分类ID获取分类下的所有书本
     * @param categoryId 分类ID
     * @return 分类下的所有书本
     */
    Mono<Result<List<Book>>> getBooksByCategory(Integer categoryId);

    /**
     * 根据书本ID获取书本详情
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 书本详情
     */
    Mono<Result<Book>> getBookByBookId(String userId, String bookId);

     /**
      * 获取Epub文件URL
      * @param userId 用户ID
      * @param bookId 书本ID
      * @return Epub文件URL
      */
    Mono<Result<String>> getBookEpub(String userId, String bookId);

    /**
     * 搜索书本
     * @param keyword 搜索关键词
     * @return 搜索到的书本列表
     */
    Mono<Result<List<Book>>> searchBook(String keyword);
}
