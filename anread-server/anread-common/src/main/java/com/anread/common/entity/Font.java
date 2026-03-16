package com.anread.common.entity;

import com.anread.common.annotation.IpReplace;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 字体配置表
 */
@Data
@TableName("font")
public class Font {
    /**
     * 字体ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 字体名称
     */
    private String fontName;
    /**
     * 字体值
     */
    private String fontValue;
     /**
     * 字体文件名
     */
    private String fontFileName;
    /**
     * 字体URL
     */
    @IpReplace
    private String url;
}
