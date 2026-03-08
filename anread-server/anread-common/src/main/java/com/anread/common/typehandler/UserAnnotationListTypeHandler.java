package com.anread.common.typehandler;

import com.anread.common.vo.UserAnnotationVO;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MyBatis-Plus 适配的 JSON 数组 → List<UserAnnotationVO> 类型处理器
 * 继承 AbstractJsonTypeHandler 简化JSON处理（MyBatis-Plus提供的工具类）
 */
@Component
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.ARRAY})
public class UserAnnotationListTypeHandler extends AbstractJsonTypeHandler<List<UserAnnotationVO>> {

    // 复用Spring的ObjectMapper（避免重复创建），也可直接new ObjectMapper()
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public UserAnnotationListTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<UserAnnotationVO> parse(String json) {
        // 核心：将JSON字符串解析为List<UserAnnotationVO>
        if (json == null || json.isEmpty()) {
            return null; // 或返回空列表：return new ArrayList<>()
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<UserAnnotationVO>>() {});
        } catch (Exception e) {
            throw new RuntimeException("解析JSON数组到List<UserAnnotationVO>失败", e);
        }
    }

    @Override
    public String toJson(List<UserAnnotationVO> obj) {
        // 反向：List → JSON字符串（入参时用，查询时无需此逻辑）
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("转换List<UserAnnotationVO>到JSON字符串失败", e);
        }
    }
}