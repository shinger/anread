package com.anread.admin.service;


import com.anread.common.entity.Category;
import com.anread.common.dto.Result;

import java.util.List;

/**
 * 分类管理服务接口
 */
public interface CategoryService {
    /**
     * 获取分类列表
     */
    Result<List<Category>> getCategories(Integer parentId);
    /**
     * 创建分类
     */
    Result addCategory(Category category);
    /**
     * 获取分类详情
     */
    Result<Category> getCategory(Integer id);
    /**
     * 更新分类
     */
    Result updateCategory(Category category);
}
