package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@TableName("book_file")
public class BookFile {
    /**
     * 文件ID
     */
    private String id;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 存储桶
     */
    private String bucket;
    /**
     * 文件路径
     */
    private String path;
     /**
      * 文件URL
      */
    private String fileUrl;
     /**
      * 是否被引用
      */
    private Boolean reference;
}
