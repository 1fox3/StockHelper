package com.fox.stockhelper.util;

import android.util.Log;

import com.fox.stockhelper.entity.dto.http.HttpResponseDto;
import com.fox.stockhelper.exception.self.ApiException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类
 * @author lusongsong
 */
public class HttpUtil {
    /**
     * 请求方式
     */
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    /**
     * 字符编码
     */
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_GBK = "GBK";
    /**
     * 请求头参数方式KEY
     */
    private static final String CONTENT_TYPE = "Content-Type";
    /**
     * 请求头参数方式
     */
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_PARAM = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_FORM = "multipart/form-data";
    /**
     * 请求参数方式
     */
    private static final String PARAM_TYPE_JSON = "JSON";
    private static final String PARAM_TYPE_PARAM = "PARAM";
    private static final String PARAM_TYPE_FORM = "FORM";
    /**
     * 支持的请求方式
     */
    private static final ArrayList<String> methodScope = new ArrayList<>(
            Arrays.asList(METHOD_GET, METHOD_POST)
    );
    /**
     * 数据类型
     */
    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>() {
        {
            put(PARAM_TYPE_JSON, CONTENT_TYPE_JSON);
            put(PARAM_TYPE_PARAM, CONTENT_TYPE_PARAM);
            put(PARAM_TYPE_FORM, CONTENT_TYPE_FORM);
        }
    };
    /**
     * 请求链接
     */
    private String url;
    /**
     * 请求方式
     */
    private String method = METHOD_GET;
    /**
     * 请求参数类型
     */
    private String paramType = PARAM_TYPE_PARAM;
    /**
     * 请求参数
     */
    private Map<String, String> params = new HashMap<>();
    /**
     * 请求数据
     */
    private String body = "";
    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<>();
    /**
     * 请求返回数据的初始字符集
     */
    private String desCharset = CHARSET_UTF8;
    /**
     * 请求返回数据的目标字符集
     */
    private String oriCharset = CHARSET_UTF8;
    /**
     * 连接超时时间
     */
    private int connectTimeout = 60000;
    /**
     * 数据传输超时时间
     */
    private int readTimeout = 60000;
    /**
     * 开始时间
     */
    public long startTime;
    /**
     * 结束时间
     */
    public long endTime;

    /**
     * 设置请求链接
     * @param url
     * @return
     */
    public HttpUtil setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 获取请求链接
     * @return
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * 获取带参数的请求链接
     * @return
     */
    public String getRequestUrl(String requestBody) {
        if (!METHOD_GET.equals(this.getMethod())) {
            return this.getUrl();
        }
        if (null != requestBody && !requestBody.equals("")) {
            return this.getUrl() + (this.getUrl().contains("?") ? "" : "?") + requestBody;
        }
        return this.getUrl();
    }

    /**
     * 设置请求方式
     * @param method
     * @return
     */
    public HttpUtil setMethod(String method) {
        String upMethod = method.toUpperCase();
        if (HttpUtil.methodScope.contains(upMethod)) {
            this.method = upMethod;
        }
        return this;
    }

    /**
     * 获取请求方式
     * @return
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * 设置请求参数方式
     * @param paramType
     * @return
     */
    public HttpUtil setParamType(String paramType) {
        String upParamType = paramType.toUpperCase();
        if (CONTENT_TYPE_MAP.containsKey(upParamType)) {
            this.paramType = paramType;
        }
        return this;
    }

    /**
     * 获取请求参数方式
     * @return
     */
    public String getParamType() {
        return this.paramType.toUpperCase();
    }

    /**
     * 获取请求头中需要的参数方式
     * @return
     */
    public String getContentType() {
        if (CONTENT_TYPE_MAP.containsKey(this.getParamType())) {
            return CONTENT_TYPE_MAP.get(this.getParamType());
        }
        return CONTENT_TYPE_PARAM;
    }

    /**
     * 设置请求数据
     * @param body
     * @return
     */
    public HttpUtil setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * 获取请求数据
     * @return
     */
    public String getBody() {
        return this.body;
    }

    /**
     * 获取请求体字符串
     * @return
     */
    public String bodyToStr() {
        return this.body.toString();
    }

    /**
     * 设置请求参数
     * @param params
     * @return
     */
    public HttpUtil setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * 设置请求参数
     * @param key
     * @param value
     * @return
     */
    public HttpUtil setParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    /**
     * 移除某个请求参数
     * @param key
     * @return
     */
    public HttpUtil removeParam(String key) {
        this.params.remove(key);
        return this;
    }

    /**
     * 清除请求参数
     * @return
     */
    public HttpUtil clearParam() {
        this.params.clear();
        return this;
    }

    /**
     * 获取请求参数
     * @return
     */
    public Map<String, String> getParams() {
        return this.params;
    }

    /**
     * 请求参数拼接
     * @return
     */
    public String paramsToUrlParams() {
        if (!this.params.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String key : this.params.keySet()) {
                stringBuffer.append("&");
                stringBuffer.append(key);
                stringBuffer.append("=");
                stringBuffer.append(this.params.get(key));
            }
            stringBuffer.deleteCharAt(0);
            return stringBuffer.toString();
        }
        return "";
    }

    /**
     * 设置请求头
     * @param headers
     * @return
     */
    public HttpUtil setHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置请求头
     * @param key
     * @param value
     * @return
     */
    public HttpUtil setHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * 移除某个请求头
     * @param key
     * @return
     */
    public HttpUtil removeHeader(String key) {
        this.headers.remove(key);
        return this;
    }

    /**
     * 清除请求头
     * @return
     */
    public HttpUtil clearHeader() {
        this.headers.clear();
        return this;
    }

    /**
     * 获取请求头
     * @return
     */
    public Map<String, String> getHeader() {
        return this.headers;
    }

    /**
     * 设置请求返回初始字符集
     * @param charset
     * @return
     */
    public HttpUtil setOriCharset(String charset) {
        this.oriCharset = charset;
        return this;
    }

    /**
     * 获取请求返回初始字符集
     * @return
     */
    public String getOriCharset() {
        return this.oriCharset;
    }

    /**
     * 设置请求返回目标字符集
     * @param charset
     * @return
     */
    public HttpUtil setDesCharset(String charset) {
        this.desCharset = charset;
        return this;
    }

    /**
     * 获取请求返回目标字符集
     * @return
     */
    public String getDesCharset() {
        return this.desCharset;
    }

    /**
     * 处理HTTP请求
     * @return
     */
    public HttpResponseDto request() throws IOException {
        //请求信息
        String requestBody = "";
        if (!this.getBody().equals("")) {
            requestBody = this.bodyToStr();
        }
        if (!this.getParams().isEmpty()) {
            requestBody = this.paramsToUrlParams();
        }
        this.startTime = System.currentTimeMillis();
        HttpURLConnection urlCon = null;

        try {
            //初始化
            URL urlObj = new URL(this.getRequestUrl(requestBody));
            urlCon = (HttpURLConnection) urlObj.openConnection();
            //设置超时时间
            urlCon.setReadTimeout(this.readTimeout);
            urlCon.setConnectTimeout(this.connectTimeout);
            //设置请求方法
            urlCon.setRequestMethod(this.getMethod());
            //添加请求头
            if (!this.headers.isEmpty()) {
                for (String key : this.headers.keySet()) {
                    urlCon.setRequestProperty(key, this.headers.get(key));
                }
            }
            if (!this.headers.containsKey(CONTENT_TYPE)) {
                urlCon.setRequestProperty(CONTENT_TYPE, this.getContentType());
            }
            //GET请求无需发送数据
            if (METHOD_GET.equals(this.getMethod())) {
                return this.readResponse(urlCon);
            }
            //允许输入输出
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlCon.getOutputStream(), CHARSET_UTF8));
            if (requestBody != null) {
                //写入请求内容
                bw.write(requestBody);
            }
            bw.close();
            return this.readResponse(urlCon);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(1, e.getMessage());
        }
    }

    /**
     * 读取接口返回数据
     * @param urlCon
     * @return
     * @throws IOException
     */
    private HttpResponseDto readResponse(HttpURLConnection urlCon) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), this.oriCharset));
            StringBuilder sb = new StringBuilder();
            String read = null;
            while ((read = br.readLine()) != null) {
                sb.append(read);
                sb.append("\n");
            }
            br.close();
            String response = sb.toString();
            if (!this.oriCharset.equals(this.desCharset)) {
                if (this.oriCharset.equals(CHARSET_GBK) && this.desCharset.equals(CHARSET_UTF8)) {
                    response = CharsetUtil.convertGBKToUtf8(response);
                }
            }
            Log.e("response", response);
            Log.e("header", urlCon.getHeaderFields().toString());
            return new HttpResponseDto(urlCon.getResponseCode(), urlCon.getResponseMessage(),
                    urlCon.getHeaderFields(), urlCon.getURL().toString(), response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(1, "q");
        } finally {
            urlCon.disconnect();
            this.endTime = System.currentTimeMillis();
        }
    }
}
