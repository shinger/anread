package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sub_comment")
public class SubComment {
    /**
     * 子评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父级评论ID
     */
    private Long commentId;

    /**
     * 关联表名
     */
    private String relateTable;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 子评论内容
     */
    private String content;

     /**
      * 点赞数
      */
    private Integer likes;

     /**
      * 创建时间
      */
    private LocalDateTime createTime;

     /**
      * 更新时间
      */
    private LocalDateTime updateTime;
}
