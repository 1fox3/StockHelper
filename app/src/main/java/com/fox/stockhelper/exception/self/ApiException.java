package com.fox.stockhelper.exception.self;

import androidx.annotation.Nullable;

import lombok.EqualsAndHashCode;

/**
 * 接口异常
 */
@EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String message;

    /**
     * 采用位置的错误信息
     * @param code
     * @param message
     */
    public ApiException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
