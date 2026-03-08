package com.anread.common.vo;

import com.anread.common.entity.CommentLike;
import com.anread.common.entity.SubComment;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookCommentVO {
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 书本ID
     */
    private String bookId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

     /**
      * 用户头像
      */
    private String avatar;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 推荐值 0无 1推荐 2一般 3不行
     */
    private Integer recommendation;

    /**
     * 是否公开 1是 0否
     */
    private Integer isPublic;

     /**
      * 点赞数
      */
    private Integer likes;

    /**
     * 是否点赞
     */
    private Boolean hasLiked;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy/M/d H:mm")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/M/d H:mm")
    private LocalDateTime updateTime;

    /**
     * 书本标题
     */
    private String bookTitle;

     /**
      * 书本作者
      */
    private String bookAuthor;

     /**
      * 书本封面
      */
    private String bookCover;

     /**
      * 是否是当前用户的评论 1是 0否
      */
    private Integer isSelf;

    /**
     * 评论点赞列表
     */
    private List<CommentLikeVO> commentLikes;

     /**
      * 子评论列表
      */
    private List<SubCommentVO> subComments;
}
