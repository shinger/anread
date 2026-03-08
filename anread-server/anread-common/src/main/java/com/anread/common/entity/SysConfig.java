package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("sys_config")
public class SysConfig {
    /**
     * 系统配置ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 配置标签
     */
    private String configLabel;
    /**
     * 配置值
     */
    private String configValue;
}
