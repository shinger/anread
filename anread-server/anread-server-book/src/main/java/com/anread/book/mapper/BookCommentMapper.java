package com.anread.book.mapper;

import com.anread.common.entity.BookComment;
import com.anread.common.vo.BookCommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookCommentMapper extends BaseMapper<BookComment> {
    /**
     * 根据图书ID获取图书评论列表
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return
     */
    List<BookCommentVO> getComments(@Param("userId") String userId, @Param("bookId") String bookId);

    BookCommentVO getComment(String id);

    @Update("update book_comment set likes = likes + 1 where id = #{commentId}")
    void addLike(Long commentId);

    @Update("update book_comment set likes = likes - 1 where id = #{commentId}")
    void minusLike(Long commentId);
}
