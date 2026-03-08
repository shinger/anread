package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("book_comment")
public class BookComment {
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
      * 创建时间
      */
     private LocalDateTime createTime;

     /**
      * 更新时间
      */
     private LocalDateTime updateTime;
}
