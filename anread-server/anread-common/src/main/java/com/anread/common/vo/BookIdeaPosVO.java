package com.anread.common.vo;

import lombok.Data;

/**
 * 根据bookId和epubCfiRange定位想法的位置
 */
@Data
public class BookIdeaPosVO {
    /**
     * 书籍ID
     */
    private String bookId;
    /**
     * EPUB CFIRange
     */
    private String epubCfiRange;
}
