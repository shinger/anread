package com.anread.user.mapper;

import com.anread.common.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set reading_duration = #{duration}, update_time = #{newUpdateTime} where update_time = #{originalUpdateTime}")
    int updateDuration(@Param("duration") Integer duration, @Param("userId") String userId, @Param("originalUpdateTime") LocalDateTime originalUpdateTime, @Param("newUpdateTime") LocalDateTime newUpdateTime);
}
