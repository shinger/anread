package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@TableName("common_file")
public class CommonFile {
    /**
     * 文件ID
     */
    private String id;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 引用次数
     */
    private Integer referenceCount;
    /**
     * 图片URL
     */
    private String imageUrl;

}
