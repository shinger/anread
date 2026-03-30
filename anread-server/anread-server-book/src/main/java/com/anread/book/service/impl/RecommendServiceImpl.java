package com.anread.book.service.impl;

import com.anread.book.mapper.SysConfigMapper;
import com.anread.book.repository.BookRepository;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import com.anread.book.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public Mono<Result<List<Book>>> getCommonRecommend() {
        return bookRepository.findByRandom()
                .collectList()
                .map(books -> {
                    return Result.<List<Book>>success().data(books);
                });

    }
}
