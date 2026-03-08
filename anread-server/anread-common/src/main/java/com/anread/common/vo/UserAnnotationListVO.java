package com.anread.common.vo;

import com.anread.common.typehandler.JsonListTypeHandler;
import com.anread.common.typehandler.UserAnnotationListTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

import java.util.List;

@Data
@TableName(autoResultMap = true)
public class UserAnnotationListVO {
    /**
     * 父级目录索引
     */
    private Integer tocParentIndex;
    /**
     * 子级目录索引
     */
    private Integer tocChildIndex;
    /**
     * 该目录下的用户划线
     */
    @TableField(typeHandler = JsonListTypeHandler.class)
    private List<UserAnnotationVO> userAnnotationList;
}
