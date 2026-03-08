package com.anread.admin.service;

import com.anread.common.dto.Result;
import com.anread.common.entity.SysConfig;

import java.util.List;

public interface SysConfigService {
    /**
     * 获取系统配置列表
     * @return
     */
    Result<List<SysConfig>> list();

    /**
     * 更新系统配置
     * @param sysConfig
     * @return
     */
    Result<SysConfig> update(SysConfig sysConfig);
}
