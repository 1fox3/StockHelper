package com.fox.stockhelper.exception.self;

import lombok.Data;

/**
 * 接口异常
 */
@Data
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
}
