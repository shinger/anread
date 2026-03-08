package com.anread.admin.controller;

import com.anread.common.entity.Category;
import com.anread.admin.service.CategoryService;
import com.anread.common.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理接口
 */
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

     /**
      * 获取分类列表
      */
    @GetMapping("/categories")
    public Result<List<Category>> getCategories(@RequestParam(required = false, value = "parentId") Integer parentId) {
        return categoryService.getCategories(parentId);
    }

     /**
      * 获取分类详情
      */
    @GetMapping("/categories/{id}")
    public Result<Category> getCategory(@PathVariable("id") Integer id) {
        return categoryService.getCategory(id);
    }

     /**
      * 创建分类
      */
    @PostMapping("/categories")
    public Result addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

     /**
      * 更新分类
      */
    @PutMapping("/categories")
    public Result updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

}
