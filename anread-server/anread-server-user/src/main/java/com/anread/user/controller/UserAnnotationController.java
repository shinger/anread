package com.anread.user.controller;

import com.anread.common.dto.Result;
import com.anread.common.entity.UserAnnotation;
import com.anread.common.vo.BookIdeaPosVO;
import com.anread.common.vo.UserAnnotationListVO;
import com.anread.common.vo.UserAnnotationVO;
import com.anread.user.service.UserAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户标注接口
 */
@RestController
@RequestMapping("/user/annotation")
public class UserAnnotationController {

    @Autowired
    private UserAnnotationService userAnnotationService;

    /**
     * 上传用户标注
     *
     * @param userAnnotation 用户标注
     * @return 响应
     */
    @PostMapping("/upload")
    public Result upload(@RequestBody UserAnnotation userAnnotation) {
        return userAnnotationService.upload(userAnnotation);
    }

    /**
     * 获取用户标注
     *
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 获取用户标注
     */
    @GetMapping("/{bookId}")
    public Result<List<UserAnnotationListVO>> getUserAnnotations(@RequestHeader("X-User-ID") String userId, @PathVariable("bookId") String bookId) {
        return userAnnotationService.getUserAnnotationsByBookId(userId, bookId);
    }

    /**
     * 获取所有公开想法的位置
     *
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 获取所有公开想法的位置
     */
    @GetMapping("/public/{bookId}")
    public Result<List<BookIdeaPosVO>> getPublicIdeaPos(@RequestHeader("X-User-ID") String userId, @PathVariable("bookId") String bookId) {
        return userAnnotationService.getPublicIdeaPos(userId, bookId);
    }

    @GetMapping("/public/ideas")
    public Result<List<UserAnnotationVO>> getPublicIdeas(@RequestHeader("X-User-ID") String userId,
                                                         @RequestParam("bookId") String bookId,
                                                         @RequestParam("epubCfiRange") String epubCfiRange) {
        return userAnnotationService.getPublicIdeas(userId, bookId, epubCfiRange);
    }

    /**
     * 删除用户标注
     *
     * @param userId 用户ID
     * @param id     标注ID
     * @return 响应
     */
    @DeleteMapping("/{id}")
    public Result delete(@RequestHeader("X-User-ID") String userId, @PathVariable("id") String id) {
        return userAnnotationService.delete(userId, id);
    }

    @PutMapping
    public Result update(@RequestBody UserAnnotation userAnnotation) {
        return userAnnotationService.update(userAnnotation);
    }
}
