package com.anread.book.controller;

import com.anread.book.service.FontService;
import com.anread.common.dto.Result;
import com.anread.common.entity.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字体管理接口
 */
@RestController
@RequestMapping("/font")
public class FontController {

    @Autowired
    private FontService fontService;

    /**
     * 获取全部字体
     * @return
     */
    @GetMapping
    public Result<List<Font>> getFonts() {
        return fontService.getFonts();
    }

    /**
     * 添加字体
     * @param font
     * @return
     */
    @PostMapping
    public Result addFont(@RequestBody Font font) {
        return fontService.addFont(font);
    }

    /**
     * 更新字体
     * @param font 字体对象
     * @return
     */
    @PutMapping
    public Result updateFont(@RequestBody Font font) {
        return fontService.updateFont(font);
    }

     /**
      * 删除字体
      * @param id 字体ID
      * @return
      */
    @DeleteMapping("/{id}")
    public Result deleteFont(@PathVariable("id") Integer id) {
        return fontService.deleteFont(id);
    }

}
