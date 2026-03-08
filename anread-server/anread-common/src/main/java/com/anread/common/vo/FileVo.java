package com.anread.common.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 文件上传返回前端
 */
@Data
@Builder
public class FileVo {
    /**
     * 文件id
     */
    private String id;
    /**
     * 书本封面
     */
    private String coverImg;
}
