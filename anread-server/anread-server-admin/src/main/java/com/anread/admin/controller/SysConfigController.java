package com.anread.admin.controller;

import com.anread.admin.service.SysConfigService;
import com.anread.common.dto.Result;
import com.anread.common.entity.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置管理接口
 */
@RestController
@RequestMapping("/sys_config")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("/list")
    public Result<List<SysConfig>> list() {
        return sysConfigService.list();
    }

    @PutMapping
    public Result<SysConfig> update(@RequestBody SysConfig sysConfig) {
        return sysConfigService.update(sysConfig);
    }
}
