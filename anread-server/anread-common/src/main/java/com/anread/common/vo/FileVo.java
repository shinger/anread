package com.anread.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传返回前端
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileVo {
    /**
     * 文件id
     */
    private String id;
    /**
     * 书本封面
     */
    private String coverImg;
    /**
     * 书本文件URL
     */
    private String fileUrl;
}
