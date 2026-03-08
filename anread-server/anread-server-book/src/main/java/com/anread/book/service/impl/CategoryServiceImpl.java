package com.anread.book.service.impl;

import com.anread.common.dto.Result;
import com.anread.common.entity.Category;
import com.anread.book.mapper.CategoryMapper;
import com.anread.book.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<List<Category>> getCategories(Integer parentId) {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parentId));
        return Result.<List<Category>>success().data(categories);
    }
}
