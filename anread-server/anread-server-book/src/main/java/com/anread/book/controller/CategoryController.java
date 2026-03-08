package com.anread.book.controller;

import com.anread.common.dto.Result;
import com.anread.book.service.CategoryService;
import com.anread.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类服务接口
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表
     * @param parentId 父级分类ID
     * @return 分类列表
     */
    @GetMapping("/{parentId}")
    public Result<List<Category>> getCategories(@PathVariable("parentId") Integer parentId) {
        return categoryService.getCategories(parentId);
    }

}
