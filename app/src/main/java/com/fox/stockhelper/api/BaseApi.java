package com.fox.stockhelper.api;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fox.stockhelper.entity.dto.api.ApiDto;
import com.fox.stockhelper.entity.dto.http.HttpResponseDto;
import com.fox.stockhelper.exception.self.ApiException;
import com.fox.stockhelper.util.HttpUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

/**
 * 接口请求基类
 * @author lusongsong
 */
@Data
public class BaseApi {
    public static final String METHOD_GET = "get";
    public static final String METHOD_POST = "post";
    /**
     * 接口域名
     */
    protected String host = "http://api.1fox3.com/";
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
    protected Class dataClass;
    /**
     * 返回数据是否为对象列表
     */
    protected boolean isListObject = false;
    /**
     * 返回数据是否为数值列表
     */
    protected boolean isListData = false;
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
        Set paramKeys = params.keySet();
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
    public Object request() throws ApiException {
        HttpUtil httpUtil = new HttpUtil();
        try {
            httpUtil.setMethod(this.method);
            httpUtil.setUrl(this.getUrl());
            httpUtil.setParams(this.requestParams);
            HttpResponseDto httpResponseDto = httpUtil.request();
            //同步请求起止时间
            this.startTime = httpUtil.startTime;
            this.endTime = httpUtil.endTime;
            //请求状态码
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
            ObjectMapper objectMapper = new ObjectMapper();
            if (isListObject) {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, this.dataClass);
                return objectMapper.readValue(JSONObject.toJSONString(this.data), javaType);
            }
            if (isListData) {
                return convertListToObject();
            }
            return objectMapper.readValue(JSONObject.toJSONString(this.data), this.dataClass);
        } catch (ApiException e) {
            throw new ApiException(1, e.getMessage());
        } catch (IOException e) {
            throw new ApiException(1, e.getMessage());
        } catch (JSONException e) {
            throw new ApiException(1, e.getMessage());
        }
    }

    /**
     * 将二维列表数据装维列表对象
     * @return
     */
    private List convertListToObject() {
        Field[] fields = this.dataClass.getDeclaredFields();
        List<String> fieldOrderList = new ArrayList<>();
        try {
            Method method = this.dataClass.getMethod("fieldOrderList");
            fieldOrderList = (List<String>)method.invoke(this.dataClass.newInstance());
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        List list = new ArrayList();
        List<Object> dataList = (List)this.data;
        for (int i = 0; i < dataList.size(); i++) {
            try {
                Object obj = this.dataClass.newInstance();
                List<Object> dataItem = (List<Object>)dataList.get(i);
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    field.setAccessible(true);
                    int fieldOrder = fieldOrderList.indexOf(field.getName());
                    Object value = dataItem.get(fieldOrder);
                    switch (field.getGenericType().toString()) {
                        case "class java.lang.String":
                            field.set(obj, (String)value);
                            break;
                        case "class java.lang.Integer":
                            field.set(obj, (Integer)value);
                            break;
                        case "class java.lang.Long":
                            field.set(obj, new Long(value.toString()));
                            break;
                        case "class java.math.BigDecimal":
                            field.set(obj, new BigDecimal(value.toString()));
                            break;
                    }
                }
                list.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
