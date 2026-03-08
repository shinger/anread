package com.anread.common.dto;

import com.anread.common.enums.StateEnum;
import com.anread.common.exception.BaseErrorInfoInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 返回结果封装类
 * @param <T> 数据类型
 */
public class Result<T> {
    /**
     * 状态码
     */
    private String code;
    /**
     * 状态描述
     */
    private String message;
    /**
     * 数据
     */
    private T data;

    public Result() {
    }

    public Result(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    // Getters and Setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    // 成功和失败的方法...
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(StateEnum.SUCCESS.getResultCode());
        result.setMessage(StateEnum.SUCCESS.getResultMsg());
        return result;
    }

    public static <T> Result<T> error(StateEnum state) {
        Result<T> result = new Result<>();
        result.setCode(state.getResultCode());
        result.setMessage(state.getResultMsg());
        return result;
    }

    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result<T> state(StateEnum state) {
        this.setCode(state.getResultCode());
        this.setMessage(state.getResultMsg());
        return this;
    }
    public Result<T> data(T data) {
        this.setData(data);
        return this;
    }

    public static <T> Result<T> error(BaseErrorInfoInterface errorInfo) {
        Result<T> result = new Result<>();
        result.setCode(errorInfo.getResultCode());
        result.setMessage(errorInfo.getResultMsg());
        result.setData(null);
        return result;
    }

    public static <T> Result<T> error(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode("-1");
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return StateEnum.SUCCESS.getResultCode().equals(this.code);
    }
}
