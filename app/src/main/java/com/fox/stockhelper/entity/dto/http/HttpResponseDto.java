package com.fox.stockhelper.entity.dto.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 请求响应结果
 * @author lusongsong
 */
@Data
public class HttpResponseDto {
    private int code;
    private String msg;
    private Map<String, List<String>> headers;
    private String requestUrl;
    private String content;

    public HttpResponseDto(int code, String msg, Map<String, List<String>> headers, String requestUrl, String content) {
        this.code = code;
        this.msg = msg;
        this.headers = headers;
        this.requestUrl = requestUrl;
        this.content = content;
    }

    public <T> T getContent(Class<T> clz) throws IOException {
        if (null != content && !content.equals("")) {
            return new ObjectMapper().readValue(content, clz);
        }
        return null;
    }
}
