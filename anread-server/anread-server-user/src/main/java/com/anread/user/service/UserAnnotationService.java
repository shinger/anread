package com.anread.user.service;

import com.anread.common.dto.Result;
import com.anread.common.entity.UserAnnotation;
import com.anread.common.vo.BookIdeaPosVO;
import com.anread.common.vo.UserAnnotationListVO;
import com.anread.common.vo.UserAnnotationVO;

import java.util.List;

public interface UserAnnotationService {
    /**
     * 上传用户标注
     * @param userAnnotation 用户标注
     */
    Result upload(UserAnnotation userAnnotation);
    /**
     * 获取用户标注
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 用户标注
     */
    Result<List<UserAnnotationListVO>> getUserAnnotationsByBookId(String userId, String bookId);

    /**
     * 删除用户标注
     * @param userId 用户ID
     * @param id 用户标注ID
     * @return 响应
     */
    Result delete(String userId, String id);

    /**
     * 更新用户标注
     * @param userAnnotation 用户标注
     * @return 响应
     */
    Result update(UserAnnotation userAnnotation);

    /**
     * 获取公开用户标注
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 获取公开用户标注
     */
    Result<List<BookIdeaPosVO>> getPublicIdeaPos(String userId, String bookId);

    /**
     * 获取公开想法的笔记
     * @param userId 用户ID
     * @param bookId 书本ID
     * @param epubCfiRange EPUB CFIRange
     * @return 获取公开想法的笔记
     */
    Result<List<UserAnnotationVO>> getPublicIdeas(String userId, String bookId, String epubCfiRange);
}
