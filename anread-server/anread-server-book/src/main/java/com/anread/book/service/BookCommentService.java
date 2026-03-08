package com.anread.book.service;

import com.anread.common.dto.Result;
import com.anread.common.entity.BookComment;
import com.anread.common.entity.SubComment;
import com.anread.common.vo.BookCommentListVO;
import com.anread.common.vo.BookCommentVO;

import java.util.List;

public interface BookCommentService {
    /**
     * 发表图书评论
     * @param bookComment 评论实体
     * @return
     */
    Result<Void> uploadComment(BookComment bookComment);

    /**
     * 根据图书ID获取图书评论列表
     * @param bookId 图书ID
     * @return
     */
    Result<BookCommentListVO> list(String userId, String bookId);

    /**
     * 根据评论ID获取评论详情
     * @param userId 用户ID
     * @param id 评论ID
     * @return
     */
    Result<BookCommentVO> getComment(String userId, String id);

    /**
     * 删除评论
     * @param id 评论ID
     * @return
     */
    Result<Void> deleteComment(String id);

    /**
     * 更新评论
     * @param bookComment 评论实体
     * @return
     */
    Result<Void> updateComment(BookComment bookComment);

    /**
     * 点赞评论
     * @param userId 点赞用户ID
     * @param commentId 评论ID
     * @return
     */
    Result<Void> likeComment(String userId, Long commentId);

    /**
     * 添加子评论
     * @param subComment 子评论实体
     * @return
     */
    Result<Void> addSubComment(SubComment subComment);
}
