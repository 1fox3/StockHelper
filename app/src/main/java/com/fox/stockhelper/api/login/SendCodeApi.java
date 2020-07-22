package com.fox.stockhelper.api.login;

import com.fox.stockhelper.api.BaseApi;

/**
 * 登录发送验证码
 * @author lusongsong
 */
public class SendCodeApi extends BaseApi {
    public SendCodeApi() {
        url = "login/sendCode";
        method = METHOD_POST;
        requestParamKeys = new String[]{"account"};
        dataClass = Boolean.class;
    }
}
