package com.anread.book.service.impl;

import com.anread.book.mapper.BookCommentMapper;
import com.anread.book.mapper.CommentLikeMapper;
import com.anread.book.mapper.SubCommentMapper;
import com.anread.book.repository.BookRepository;
import com.anread.book.service.BookCommentService;
import com.anread.common.dto.Result;
import com.anread.common.entity.Book;
import com.anread.common.entity.BookComment;
import com.anread.common.entity.CommentLike;
import com.anread.common.entity.SubComment;
import com.anread.common.enums.RelateTableEnum;
import com.anread.common.vo.BookCommentListVO;
import com.anread.common.vo.BookCommentVO;
import com.anread.common.vo.CommentLikeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookCommentServiceImpl implements BookCommentService {

    @Autowired
    private BookCommentMapper bookCommentMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Autowired
    private SubCommentMapper subCommentMapper;

    @Override
    public Result<Void> uploadComment(BookComment bookComment) {
        Book book = bookRepository.findById(bookComment.getBookId()).block();
        if (book == null) {
            return Result.error("书本不存在");
        }

        BookComment existed = bookCommentMapper.selectOne(new LambdaQueryWrapper<BookComment>()
                .eq(BookComment::getBookId, bookComment.getBookId())
                .eq(BookComment::getUserId, bookComment.getUserId()));
        if (existed != null) {
            return Result.error("已评论过该书本");
        }

        bookComment.setLikes(0);
        int insert = bookCommentMapper.insert(bookComment);
        if (insert != 1) {
            return Result.error("评论上传失败");
        }
        return Result.success();
    }

    @Override
    public Result<BookCommentListVO> list(String userId, String bookId) {
        List<BookCommentVO> bookComments = bookCommentMapper.getComments(userId, bookId);
        int scoreSum = 0;
        int recommendSum = 0;
        for (BookCommentVO bookComment : bookComments) {
            scoreSum += bookComment.getScore();
            recommendSum += bookComment.getRecommendation() == 1 ? 1 : 0;
        }
        BookCommentListVO bookCommentListVO = new BookCommentListVO();
        if (bookComments.isEmpty()) {
            bookCommentListVO.setAvgScore(0.0);
            bookCommentListVO.setAvgRecommendation(0.0);
            bookCommentListVO.setComments(bookComments);
            return Result.<BookCommentListVO>success().data(bookCommentListVO);
        }
        double avgScore = (double)scoreSum / bookComments.size();
        double avgRecommend = (double)recommendSum * 100 / bookComments.size();
        DecimalFormat df = new DecimalFormat("0.00");
        bookCommentListVO.setAvgScore(Double.valueOf(df.format(avgScore)));
        bookCommentListVO.setAvgRecommendation(Double.valueOf(df.format(avgRecommend)));
        bookCommentListVO.setComments(bookComments);
        return Result.<BookCommentListVO>success().data(bookCommentListVO);
    }

    @Override
    public Result<BookCommentVO> getComment(String userId, String id) {
        BookCommentVO bookComment = bookCommentMapper.getComment(id);
        if (bookComment == null) {
            return Result.error("评论不存在");
        }
        Book book = bookRepository.findById(bookComment.getBookId()).block();
        if (book == null) {
            return Result.error("书本不存在");
        }
        bookComment.setBookTitle(book.getTitle());
        bookComment.setBookAuthor(book.getAuthor());
        bookComment.setBookCover(book.getCover());
        bookComment.setIsSelf(bookComment.getUserId().equals(userId) ? 1 : 0);
        bookComment.setHasLiked(false);
        for (CommentLikeVO commentLike : bookComment.getCommentLikes()) {
            if (commentLike.getUserId().equals(userId)) {
                bookComment.setHasLiked(true);
            }
        }
        return Result.<BookCommentVO>success().data(bookComment);
    }

    @Override
    public Result<Void> deleteComment(String id) {
        bookCommentMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<Void> updateComment(BookComment bookComment) {
        Book book = bookRepository.findById(bookComment.getBookId()).block();
        if (book == null) {
            return Result.error("书本不存在");
        }

        bookCommentMapper.updateById(bookComment);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> likeComment(String userId, Long commentId) {
        CommentLike existed = commentLikeMapper.selectOne(new LambdaQueryWrapper<CommentLike>()
                .eq(CommentLike::getLikeUserId, userId)
                .eq(CommentLike::getCommentId, commentId));
        if (existed != null) {
            commentLikeMapper.deleteById(existed.getId());
            bookCommentMapper.minusLike(commentId);
            return Result.success();
        }

        bookCommentMapper.addLike(commentId);
        CommentLike commentLike = new CommentLike();
        commentLike.setLikeUserId(userId);
        commentLike.setCommentId(commentId);
        commentLike.setRelateTable(RelateTableEnum.BOOK_COMMENT.getTableName());
        commentLikeMapper.insert(commentLike);
        return Result.success();
    }

    @Override
    public Result<Void> addSubComment(SubComment subComment) {
        subComment.setLikes(0);
        subComment.setRelateTable(RelateTableEnum.BOOK_COMMENT.getTableName());
        subComment.setCreateTime(LocalDateTime.now());
        subComment.setUpdateTime(LocalDateTime.now());
        int insert = subCommentMapper.insert(subComment);
        if (insert != 1) {
            return Result.error("添加子评论失败");
        }
        return Result.success();
    }
}
