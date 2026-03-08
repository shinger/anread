package com.anread.book.controller;

import com.anread.book.service.BookCommentService;
import com.anread.common.dto.Result;
import com.anread.common.entity.BookComment;
import com.anread.common.entity.SubComment;
import com.anread.common.vo.BookCommentListVO;
import com.anread.common.vo.BookCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/book_comment")
public class BookCommentController {

    @Autowired
    private BookCommentService bookCommentService;

    @PostMapping("/post")
    public Result<Void> uploadComment(@RequestHeader("X-User-ID") String userId, @RequestBody BookComment bookComment) {
        bookComment.setUserId(userId);
        bookComment.setCreateTime(LocalDateTime.now());
        bookComment.setUpdateTime(LocalDateTime.now());
        return bookCommentService.uploadComment(bookComment);
    }

    @GetMapping("/list/{bookId}")
    public Result<BookCommentListVO> list(@RequestHeader("X-User-ID") String userId, @PathVariable("bookId") String bookId) {
        return bookCommentService.list(userId, bookId);
    }

    @GetMapping("/{id}")
    public Result<BookCommentVO> getComment(@RequestHeader("X-User-ID") String userId, @PathVariable("id") String id) {
        return bookCommentService.getComment(userId, id);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable("id") String id) {
        return bookCommentService.deleteComment(id);
    }

    @PostMapping("/like/{commentId}")
    public Result<Void> likeComment(@RequestHeader("X-User-ID") String userId, @PathVariable("commentId") Long commentId) {
        return bookCommentService.likeComment(userId, commentId);
    }

    @PutMapping
    public Result<Void> updateComment(@RequestBody BookComment bookComment) {
        bookComment.setUpdateTime(LocalDateTime.now());
        return bookCommentService.updateComment(bookComment);
    }

    @PostMapping("/sub")
    public Result<Void> addSubComment(@RequestHeader("X-User-ID") String userId, @RequestBody SubComment subComment) {
        subComment.setUserId(userId);
        return bookCommentService.addSubComment(subComment);
    }
}
