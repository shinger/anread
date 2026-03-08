package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户标注
 */
@Data
@TableName("user_annotation")
public class UserAnnotation {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 书籍ID
     */
    private String bookId;
    /**
     * EPUB CFIRange
     */
    private String epubCfiRange;
    /**
     * 标注类型
     * highlight、underline、idea
     */
    private String type;
    /**
     * 划线颜色
     */
    private String lineColor;
    /**
     * 划线内容
     */
    private String lineContent;
    /**
     * 想法内容
     */
    private String ideaContent;
     /**
     * 父级目录索引
     */
    private Integer tocParentIndex;
    /**
     * 子级目录索引
     */
    private Integer tocChildIndex;
    /**
     * 是否公开
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
