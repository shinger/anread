package com.anread.book.service;


import com.anread.common.dto.Result;
import com.anread.common.entity.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 获取分类列表
     * @param parentId 父级分类ID
     * @return 分类列表
     */
    Result<List<Category>> getCategories(Integer parentId);

}
