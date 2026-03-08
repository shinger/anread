package com.anread.book.service;

import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RecommendService {
    /**
     * 获取大家都在看推荐页列表
     * @return 推荐书本列表
     */
    Mono<Result<List<Book>>> getCommonRecommend();
}
