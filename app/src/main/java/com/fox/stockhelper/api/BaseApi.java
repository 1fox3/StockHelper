package com.fox.stockhelper.api;

import android.util.Log;

import com.fox.stockhelper.entity.dto.api.ApiDto;
import com.fox.stockhelper.entity.dto.http.HttpResponseDto;
import com.fox.stockhelper.exception.self.ApiException;
import com.fox.stockhelper.util.HttpUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 接口请求基类
 * @author lusongsong
 */
public class BaseApi {
    public final static String METHOD_GET = "get";
    public final static String METHOD_POST = "post";
    /**
     * 接口域名
     */
    protected String host = "http://api.1fox3.com/stockhelper/";
    /**
     * 接口
     */
    protected String url = "";
    /**
     * 请求方式
     */
    protected String method = METHOD_GET;
    /**
     * 请求开始时间
     */
    protected long startTime;
    /**
     * 请求结束时间
     */
    protected long endTime;
    /**
     * 响应码
     */
    protected int code;
    /**
     * 响应消息
     */
    protected String msg;
    /**
     * 响应数据
     */
    protected Object data;
    /**
     * 响应数据对应的类
     */
    protected String dataClass;
    /**
     * 请求的参数key
     */
    protected String[] requestParamKeys;
    /**
     * 请求数据
     */
    protected Map<String, String> requestParams = new HashMap<>();

    /**
     * 设置请求参数
     */
    public void setParams(Map<String, Object> params) {
        Set paramKeys =params.keySet();
        if (null != requestParamKeys && requestParamKeys.length > 0) {
            for (String paramKey : requestParamKeys) {
                if (paramKeys.contains(paramKey)) {
                    requestParams.put(paramKey, params.get(paramKey).toString());
                }
            }
        }
    }

    /**
     * 获取请求链接
     * @return
     */
    protected String getUrl() {
        return this.host + this.url;
    }

    /**
     * 接口请求
     */
    public Object request() {
        HttpUtil httpUtil = new HttpUtil();
        try {
            httpUtil.setMethod(this.method);
            httpUtil.setUrl(this.getUrl());
            Log.e("lusongsong", this.requestParams.toString());
            httpUtil.setParams(this.requestParams);
            HttpResponseDto httpResponseDto = httpUtil.request();
            this.startTime = httpUtil.startTime;
            this.endTime = httpUtil.endTime;
            this.code = httpResponseDto.getCode();
            this.msg = httpResponseDto.getMsg();
            this.data = httpResponseDto.getContent();
            if (HttpURLConnection.HTTP_OK != httpResponseDto.getCode()) {
                this.code = httpResponseDto.getCode();
                throw new ApiException(httpResponseDto.getCode(), httpResponseDto.getMsg());
            }
            ApiDto apiDto = httpResponseDto.getContent(ApiDto.class);
            this.code = apiDto.getCode();
            this.msg = apiDto.getMsg();
            this.data = apiDto.getData();
            if (0 != apiDto.getCode()) {
                throw new ApiException(apiDto.getCode(), apiDto.getMsg());
            }
//            return new ObjectMapper().readValue(apiDto.getData(), this.dataClass);
        } catch (IOException e) {
            throw new ApiException(1, e.getMessage());
        }
        return "";
    }
}
