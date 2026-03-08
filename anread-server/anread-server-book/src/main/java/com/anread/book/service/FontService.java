package com.anread.book.service;

import com.anread.common.dto.Result;
import com.anread.common.entity.Font;

import java.util.List;

public interface FontService {
    /**
     * 获取全部字体
     * @return 字体列表
     */
    Result<List<Font>> getFonts();

    /**
     * 添加字体
     * @param font 字体对象
     * @return
     */
    Result addFont(Font font);

    /**
     * 更新字体
     * @param font 字体对象
     * @return
     */
    Result updateFont(Font font);

    /**
     * 删除字体
     * @param id 字体ID
     * @return
     */
    Result deleteFont(Integer id);
}
