package com.anread.book.service.impl;

import com.anread.book.mapper.FontMapper;
import com.anread.book.service.FontService;
import com.anread.common.dto.Result;
import com.anread.common.entity.Font;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FontServiceImpl implements FontService {

    @Autowired
    private FontMapper fontMapper;

    @Override
    public Result<List<Font>> getFonts() {
        List<Font> fonts = fontMapper.selectList(null);
        return Result.<List<Font>>success().data(fonts);
    }

    @Override
    public Result addFont(Font font) {
        // 检查字体是否已存在
        Font existFont = fontMapper.selectOne(new LambdaQueryWrapper<Font>()
                .eq(Font::getFontName, font.getFontName())
                .or().eq(Font::getUrl, font.getUrl()));

        if (existFont != null) {
            return Result.error("字体名称或文件重复");
        }
        fontMapper.insert(font);
        return Result.success();
    }

    @Override
    public Result updateFont(Font font) {
        Font existFont = fontMapper.selectById(font.getId());
        if (existFont == null) {
            return Result.error("字体不存在");
        }
        fontMapper.updateById(font);
        return Result.success();
    }

    @Override
    public Result deleteFont(Integer id) {
        Font existFont = fontMapper.selectById(id);
        if (existFont == null) {
            return Result.error("字体不存在");
        }
        fontMapper.deleteById(id);
        return Result.success();
    }
}
