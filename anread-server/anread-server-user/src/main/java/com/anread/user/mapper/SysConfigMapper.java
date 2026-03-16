package com.anread.user.mapper;

import com.anread.common.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    @Select("select config_value from sys_config where config_label = '访问IP'")
    public String getAccessIP();
}
