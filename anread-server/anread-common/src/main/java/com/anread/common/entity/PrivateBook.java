package com.anread.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("private_book")
public class PrivateBook {
    /**
     * 私有图书ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String userId;

    private String title;

    private String fileId;
}
