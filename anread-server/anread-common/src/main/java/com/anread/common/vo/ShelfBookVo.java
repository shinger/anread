package com.anread.common.vo;

import com.anread.common.annotation.IpReplace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Book前端显示
 */
@Data
@AllArgsConstructor
@Builder
public class ShelfBookVo {
    /**
     * 书架id
     */
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
     * 分类id
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
     * 封面
     */
    @IpReplace
    private String cover;
     /**
     * 是否读完
     */
    private Boolean readFinished;
    /**
     * 是否私有
     */
    private Boolean isPrivate;
}
