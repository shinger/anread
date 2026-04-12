package com.anread.common.dto;

import lombok.Data;

@Data
public class QdrantBookQuery {
    /**
     * 查询语句
     */
    private String query;
    /**
     * 图书ID
     */
    private String bookId;
    /**
     * 返回结果条数最大值
     */
    private Integer maxResults;
}
