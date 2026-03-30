package com.anread.book.service;

import com.anread.common.dto.Result;
import com.anread.common.vo.ShelfBookVo;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookShelfService {

    /**
     * 获取书架列表
     * @param pageNum 页码
     * @return 书架列表
     */
    Mono<Result<List<ShelfBookVo>>> getBookList(String userId, Integer pageNum, long pageSize);

    /**
     * 加入书架
     * @param bookId 书本ID
     * @return
     */
    Result join(String userId, String bookId);

    /**
     * 查找是否在书架内
     * @param bookId 书本ID
     * @return 是否在书架内
     */
    Result<Boolean> inShelf(String userId, String bookId);

    /**
     * 移出书架
     * @param bookId
     * @return
     */
    Result removeShelf(String userId, String bookId);

    /**
     * 批量移出书架
     * @param bookIds 书本ID列表
     * @return
     */
    Result removeBatchShelf(String userId, List<String> bookIds);

    /**
     *
     * @param userId 用户ID
     * @param file 图书文件
     * @return
     */
    Result uploadBook(String userId, MultipartFile file);

}
