package com.anread.admin.service.impl;

import com.anread.common.entity.Category;
import com.anread.admin.mapper.CategoryMapper;
import com.anread.admin.service.CategoryService;
import com.anread.common.dto.Result;
import com.anread.common.enums.StateEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<List<Category>> getCategories(Integer parentId) {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId, parentId));
        return Result.<List<Category>>success().data(categories);
    }

    @Override
    public Result addCategory(Category category) {
        int insert = categoryMapper.insert(category);
        if (insert > 0) {
            return Result.success();
        } else {
            return Result.error(StateEnum.ADD_FAILED);
        }
    }

    @Override
    public Result<Category> getCategory(Integer id) {
        Category category = categoryMapper.selectById(id);
        return Result.<Category>success().data(category);
    }

    @Override
    public Result updateCategory(Category category) {
        int i = categoryMapper.updateById(category);
        if (i > 0) {
            return Result.success();
        } else {
            return Result.error(StateEnum.UPDATE_FAILED);
        }
    }
}
