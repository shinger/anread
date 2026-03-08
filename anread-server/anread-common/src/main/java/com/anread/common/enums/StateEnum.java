package com.anread.common.enums;


import com.anread.common.exception.BaseErrorInfoInterface;

public enum StateEnum implements BaseErrorInfoInterface {
    SUCCESS("200", null),                           // 请求成功
    UPLOAD_SUCCESS("200", "上传成功"),               // 上传成功
    BOOK_NOT_EXISTS("301", "书本不存在"),            // 书本不存在
    REQUEST_DATA_ERROR("401", "请求数据错误"),       // 前端参数错误
    BUSSINESS_ERROR("501", "后台空指针异常错误"),     // 空指针
    INTERNAL_SERVER_ERROR("500", "服务异常"),       // 内部服务异常
    ADD_FAILED("302", "添加失败"),
    UPDATE_FAILED("303", "修改失败"),
    BOOK_EXISTS("302", "书本已存在"),
    UPLOAD_FAILED("304", "上传失败"),
    PARAMS_ERROR("400", "参数错误");

    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    StateEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getResultMsg() {
        return this.resultMsg;
    }
}
