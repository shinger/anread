package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("comment_like")
public class CommentLike {
    /**
     * 点赞ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

     /**
      * 评论ID
      */
    private Long commentId;

    /**
     * 关联表名
     */
    private String relateTable;

     /**
      * 点赞用户ID
      */
    private String likeUserId;
}
