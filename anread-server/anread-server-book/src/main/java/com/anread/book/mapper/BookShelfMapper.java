package com.anread.book.mapper;

import com.anread.common.entity.UserShelf;
import com.anread.common.vo.ShelfBookVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookShelfMapper extends BaseMapper<UserShelf> {

//    List<ShelfBookVo> selectBookVo(@Param("userId") String userId);

}
