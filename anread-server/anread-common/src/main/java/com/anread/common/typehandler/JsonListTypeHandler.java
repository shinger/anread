package com.anread.common.typehandler;

import com.alibaba.fastjson2.JSON;
import com.anread.common.vo.UserAnnotationVO;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.List;

// 指定处理的Java类型和JDBC类型
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JsonListTypeHandler extends BaseTypeHandler<List<UserAnnotationVO>> {

    // 从数据库读取JSON字符串转List
    @Override
    public List<UserAnnotationVO> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return json == null ? null : JSON.parseArray(json, UserAnnotationVO.class);
    }

    @Override
    public List<UserAnnotationVO> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return json == null ? null : JSON.parseArray(json, UserAnnotationVO.class);
    }

    @Override
    public List<UserAnnotationVO> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return json == null ? null : JSON.parseArray(json, UserAnnotationVO.class);
    }

    // 从Java对象转JSON字符串写入数据库（此处查询场景可暂不实现）
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<UserAnnotationVO> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }
}