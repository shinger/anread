package com.anread.book.service.impl;

import com.anread.book.mapper.SysConfigMapper;
import com.anread.book.repositry.BookRepository;
import com.anread.book.utils.DeviceUtils;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import com.anread.book.mapper.BookMapper;
import com.anread.book.service.RecommendService;
import com.anread.common.entity.SysConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        boolean isMobile;
        String accessIP;
        if (DeviceUtils.isMobileDevice()) {
            isMobile = true;
            accessIP = sysConfigMapper.getAccessIP();
        } else {
            accessIP = null;
            isMobile = false;
        }
        return bookRepository.findByRandom()
                .collectList()
                .map(books -> {
                    // 对每个 Book 进行处理
                    List<Book> processedBooks = books.stream()
                            .map(book -> {
                                if (isMobile && accessIP != null) {
                                    book.setCover(book.getCover().replace("127.0.0.1", accessIP));
                                    book.setEpubURL(book.getEpubURL().replace("127.0.0.1", accessIP));
                                }

                                return book; // 返回处理后的 book（可原地修改或返回新对象）
                            })
                            .toList(); // Java 16+ 用 toList()，否则用 collect(Collectors.toList())

                    return Result.<List<Book>>success().data(processedBooks);
                });

    }
}
