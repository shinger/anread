package com.anread.admin.controller;

import com.anread.admin.service.BookService;
import com.anread.admin.vo.BookVO;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 图书管理接口
 */
@RestController
@RequestMapping("/book")
public class BookController {

     @Autowired
     private BookService bookService;

     /**
      * 创建图书
      */
     @PostMapping
     public Result createBook(@RequestBody BookVO bookVO) {
         return bookService.createBook(bookVO);
     }

     /**
      * 获取图书列表
      */
     @GetMapping("/list")
     public Result<Page<Book>> getBooks(@RequestParam("pageNumber") long pageNumber, @RequestParam("pageSize") long pageSize) {
         return bookService.getBooksPage(pageNumber, pageSize);
     }

     /**
      * 获取图书详情
      */
     @GetMapping("/{id}")
     public Result<Book> getBook(@PathVariable("id") String id) {
        return bookService.getBook(id);
     }

     /**
      * 更新图书
      */
     @PutMapping
     public Result updateBook(@RequestBody BookVO bookVO) {
        return bookService.updateBook(bookVO);
     }

     /**
      * 删除图书
      */
     @DeleteMapping("/{id}")
     public Result deleteBook(@PathVariable("id") String id) {
        return bookService.deleteBook(id);
     }

     /**
      * 更新图书状态
      */
     @PutMapping("/{id}/{status}")
     public Result updateBookStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        return bookService.updateBookStatus(id, status);
     }


}
