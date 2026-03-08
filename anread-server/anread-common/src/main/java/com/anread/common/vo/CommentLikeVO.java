package com.anread.common.vo;

import com.anread.common.entity.CommentLike;
import lombok.Data;

@Data
public class CommentLikeVO extends CommentLike {
    /**
     * 点赞用户ID
     */
    private String userId;

     /**
      * 点赞用户昵称
      */
    private String username;

     /**
      * 点赞用户头像
      */
    private String avatar;
}
