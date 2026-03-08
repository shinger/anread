package com.anread.admin.service;

import com.anread.admin.vo.BookVO;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import org.springframework.data.domain.Page;


/**
 * 图书管理服务接口
 */
public interface BookService {
    /**
     * 创建图书
     */
    Result createBook(BookVO bookVO);
    /**
     * 获取图书分页列表
     */
    Result<Page<Book>> getBooksPage(long pageNumber, long pageSize);
    /**
     * 获取图书详情
     */
    Result<Book> getBook(String id);
    /**
     * 更新图书
     */
    Result updateBook(BookVO bookVO);
     /**
      * 删除图书
      */
    Result deleteBook(String id);

     /**
      * 更新图书状态
      */
    Result updateBookStatus(String id, Integer status);
}
