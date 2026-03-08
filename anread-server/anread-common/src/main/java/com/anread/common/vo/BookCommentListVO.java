package com.anread.common.vo;

import com.anread.common.entity.BookComment;
import lombok.Data;

import java.util.List;

@Data
public class BookCommentListVO {
    private Double avgScore;
     private Double avgRecommendation;
    private List<BookCommentVO> comments;
}
