package com.anread.admin.service.impl;

import com.anread.admin.mapper.SysConfigMapper;
import com.anread.admin.service.SysConfigService;
import com.anread.common.dto.Result;
import com.anread.common.entity.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public Result<List<SysConfig>> list() {
        List<SysConfig> list = sysConfigMapper.selectList(null);
        return Result.<List<SysConfig>>success().data(list);
    }

    @Override
    public Result<SysConfig> update(SysConfig sysConfig) {
        sysConfigMapper.updateById(sysConfig);
        return Result.success();
    }
}
