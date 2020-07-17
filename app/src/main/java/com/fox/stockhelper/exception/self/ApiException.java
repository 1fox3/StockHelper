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
    private String msg;

    /**
     * 采用位置的错误信息
     * @param code
     * @param msg
     */
    public ApiException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
