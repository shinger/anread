package com.anread.book.controller;

import com.anread.common.dto.Result;
import com.anread.book.service.RecommendService;
import com.anread.common.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 荐书服务接口
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 大家都在看推荐页列表
     * @return 推荐书本列表
     */
    @GetMapping("/common")
    public Mono<Result<List<Book>>> getCommonRecommend() {
        return recommendService.getCommonRecommend();
    }

}
