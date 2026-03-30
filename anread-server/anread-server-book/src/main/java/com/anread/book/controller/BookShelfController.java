package com.anread.book.controller;

import com.anread.common.dto.Result;
import com.anread.book.service.BookShelfService;
import com.anread.common.vo.ShelfBookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 书架服务接口
 */
@RestController
@RequestMapping("/bookshelf")
public class BookShelfController {

    @Autowired
    private BookShelfService bookShelfService;

    /**
     * 首页继续阅读书架列表
     * @return 书架列表
     */
    @GetMapping("/list_8")
    public Mono<Result<List<ShelfBookVo>>> getHomeBookList(@RequestHeader("X-User-ID") String userId, @RequestParam("pageNum") Integer pageNum) {
        return bookShelfService.getBookList(userId, pageNum, 8);
    }

    /**
     * 获取书架列表
     * @return 书架列表
     */
    @GetMapping("/list")
    public Mono<Result<List<ShelfBookVo>>> getBookList(@RequestHeader("X-User-ID") String userId, @RequestParam("pageNum") Integer pageNum) {
        return bookShelfService.getBookList(userId, pageNum, 30);
    }

    /**
     * 添加到书架
     * @param bookId 书本ID
     * @return
     */
    @PostMapping("/join/{id}")
    public Result joinShelf(@RequestHeader("X-User-ID") String userId, @PathVariable("id") String bookId) {
        return bookShelfService.join(userId, bookId);
    }

    /**
     * 查找是否在书架内
     * @param bookId 书本ID
     * @return 是否在书架内
     */
    @GetMapping("/inshelf/{id}")
    public Result<Boolean> inShelf(@RequestHeader("X-User-ID") String userId, @PathVariable("id") String bookId) {
        return bookShelfService.inShelf(userId, bookId);
    }

    /**
     * 移出书架
     * @param bookId 书本ID
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public Result removeShelf(@RequestHeader("X-User-ID") String userId, @PathVariable("id") String bookId) {
        return bookShelfService.removeShelf(userId, bookId);
    }

    @DeleteMapping("/remove/batch")
    public Result removeBatchShelf(@RequestHeader("X-User-ID") String userId, @RequestBody List<String> bookIds) {
        return bookShelfService.removeBatchShelf(userId, bookIds);
    }

    /**
     * 上传图书到书架
     * @param userId 用户ID
     * @param file 图书文件
     * @return
     */
    @PostMapping("/upload")
    public Result uploadBook(@RequestHeader("X-User-ID") String userId, @RequestParam("file") MultipartFile file) {
        return bookShelfService.uploadBook(userId, file);
    }

}
