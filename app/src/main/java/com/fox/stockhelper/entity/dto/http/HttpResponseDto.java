package com.fox.stockhelper.entity.dto.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 请求响应结果
 * @author lusongsong
 */
public class HttpResponseDto {
    private int code;
    private String msg;
    private Map<String, List<String>> headers;
    private String requestUrl;
    private String content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HttpResponseDto(int code, String msg, Map<String, List<String>> headers,
                           String requestUrl, String content) {
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
