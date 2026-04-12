package com.anread.admin.service.impl;

import com.anread.admin.config.RabbitMQConfig;
import com.anread.admin.repositry.BookRepositry;
import com.anread.common.entity.Category;
import com.anread.admin.mapper.CategoryMapper;
import com.anread.admin.mapper.FileMapper;
import com.anread.admin.service.BookService;
import com.anread.admin.vo.BookVO;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import com.anread.common.entity.BookFile;
import com.anread.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private BookRepositry bookRepositry;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result createBook(BookVO bookVO) {
        Book book = bookVO2Book(bookVO);
        bookRepositry.findByFileId(bookVO.getFileId());
        Book existBook = bookRepositry.findByFileId(book.getFileId());
        if (existBook != null){
            return Result.error("EPUB文件重复，请重新上传");
        }
        // 插入到 MongoDB
        bookRepositry.insert(book);
        log.info("【MongoDB】 插入成功: {}", book);
        // 更新文件引用
        BookFile bookFile = fileMapper.selectById(book.getFileId());
        if (bookFile == null) {
            return Result.error("文件不存在");
        }
        bookFile.setReference(true);
        bookFile.setBookId(book.getId());
        bookFile.setTitle(bookVO.getTitle());
        bookFile.setIsVectorized(false);
        fileMapper.updateById(bookFile);

        // 发送文件解析消息
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_BOOK_SPLIT, RabbitMQConfig.ROUTING_BOOK_SPLIT, book.getId());

        return Result.success().message("创建成功");
    }

    @Override
    public Result<Page<Book>> getBooksPage(long pageNumber, long pageSize) {
        Page<Book> books = bookRepositry.findByIsPrivateFalse(PageRequest.of((int) pageNumber, (int) pageSize));

        return Result.<Page<Book>>success().data(books);
    }

    @Override
    public Result<Book> getBook(String id) {
        Book book = bookRepositry.findById(id).orElse(null);

        if (book == null) {
            return Result.error("书本不存在");
        }
        return Result.<Book>success().data(book);
    }

    @Override
    public Result updateBook(BookVO bookVO) {
        Book book = bookVO2Book(bookVO);
        Optional<Book> existBook = bookRepositry.findById(book.getId());
        if (existBook.isEmpty()) {
            return Result.error("书本不存在");
        }
        BeanUtils.copyProperties(book, existBook.get());

        bookRepositry.save(existBook.get());

        return Result.success().message("修改成功");
    }

    @Override
    public Result deleteBook(String id) {
        Optional<Book> existBook = bookRepositry.findById(id);
        if (existBook.isEmpty()) {
            return Result.error("书本不存在");
        }
        bookRepositry.deleteById(id);
        return Result.success().message("删除成功");
    }

    @Override
    public Result updateBookStatus(String id, Integer status) {
        Optional<Book> existBook = bookRepositry.findById(id);
        if (existBook.isEmpty()) {
            return Result.error("书本不存在");
        }
        existBook.get().setStatus(status);
        bookRepositry.save(existBook.get());
        return Result.success().message("修改成功");
    }

    /**
     * 将BookVO对象转换为存储对象Book
     * @param bookVO
     * @return
     */
    private Book bookVO2Book(BookVO bookVO) {
        if (bookVO.getFileId() == null) {
            throw new BizException("未上传电子书文件");
        }
        // 分割时间
        String[] pressDateSplit = bookVO.getPressDate().split("-");
        // 获取主分类
        Integer categoryId = bookVO.getCategoryId();
        Category subCategory = categoryMapper.selectById(categoryId);
        Category mainCategory = categoryMapper.selectById(subCategory.getParentId());
        // 获取 path
        BookFile bookFile = fileMapper.selectById(bookVO.getFileId());


        Book book = Book.builder()
                .id(bookVO.getBookId())
                .title(bookVO.getTitle())
                .author(bookVO.getAuthor())
                .introduction(bookVO.getIntroduction())
                .press(bookVO.getPress())
                .pressYear(Integer.valueOf(pressDateSplit[0]))
                .pressMonth(Integer.valueOf(pressDateSplit[1]))
                .categoryId(bookVO.getCategoryId())
                .mainCategory(mainCategory.getCategoryName())
                .subCategory(subCategory.getCategoryName())
                .wordCount(bookVO.getWordCount())
                .readership(0L)
                .readingOverCount(0L)
                .status(bookVO.getStatus())
                .cover(bookVO.getCoverImg())
                .path(bookFile.getPath())
                .fileId(bookVO.getFileId())
                .epubURL(bookFile.getFileUrl())
                .isPrivate(false)
                .build();

        return book;
    }


}
