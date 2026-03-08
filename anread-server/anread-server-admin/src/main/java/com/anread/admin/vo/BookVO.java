package com.anread.admin.vo;

import lombok.Data;

/**
 * 前端创建 Book
 */
@Data
public class BookVO {
    private String bookId;
    private Integer categoryId;
    private String title;
    private String author;
    private String introduction;
    private String press;
    private String pressDate;
    private Float wordCount;
    private String coverImg;
    private String fileId;
    private Integer status;
}
