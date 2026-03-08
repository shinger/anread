package com.anread.book.controller;

import com.anread.common.dto.Result;
import com.anread.book.service.BookService;
import com.anread.common.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 图书服务接口
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 获取分类下的书本列表
     * @param categoryId 分类ID
     * @return
     */
    @GetMapping("/category/{categoryId}")
    public Mono<Result<List<Book>>> getBooks(@PathVariable("categoryId") Integer categoryId) {
        return bookService.getBooksByCategory(categoryId);
    }

     /**
      * 获取书本详情
      *
      * @param bookId 书本ID
      * @return
      */
    @GetMapping("/{bookId}")
    public Mono<Result<Book>> getBook(@RequestHeader("X-User-ID") String userId, @PathVariable("bookId") String bookId) {
        return bookService.getBookByBookId(userId, bookId);
    }

    /**
     * 获取Epub文件URL
     * @param bookId 书本ID
     * @return Epub文件URL
     */
    @GetMapping("/epub/{id}")
    public Mono<Result<String>> getBookEpub(@RequestHeader("X-User-ID") String userId, @PathVariable("id") String bookId) {
        return bookService.getBookEpub(userId, bookId);
    }

     /**
      * 搜索书本
      * @param keyword 搜索关键词
      * @return 搜索到的书本列表
      */
    @GetMapping("/search")
    public Mono<Result<List<Book>>> searchBook(@RequestParam("keyword") String keyword) {
        return bookService.searchBook(keyword);
    }

}
