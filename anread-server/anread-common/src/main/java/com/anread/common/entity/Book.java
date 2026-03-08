package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Book表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("book")
@Document("book")
public class Book {
    /**
     * 书本ID
     */
    @Id
    private String id;
    /**
     * 书名
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 出版社
     */
    private String press;
    /**
     * 出版年份
     */
    private Integer pressYear;
    /**
     * 出版月份
     */
    private Integer pressMonth;
    /**
     * 分类ID
     */
    private Integer categoryId;
    /**
     * 主分类
     */
    private String mainCategory;
    /**
     * 子分类
     */
    private String subCategory;
    /**
     * 字数
     */
    private Float wordCount;
    /**
     * 读者数量
     */
    private Long readership;
     /**
     * 读完人数
     */
    private Long readingOverCount;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 封面URL
     */
    private String cover;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件ID
     */
    private String fileId;
     /**
     * EPUB文件URL
     */
    private String epubURL;
}
