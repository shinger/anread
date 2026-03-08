package com.anread.common.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户标注
 */
@Data
public class UserAnnotationVO {
    /**
     * 主键ID
     */
    private String id;
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
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否公开
     */
    private Integer isPublic;
    /**
     * 是否是当前用户标注
     */
    private Boolean isSelf;
}
