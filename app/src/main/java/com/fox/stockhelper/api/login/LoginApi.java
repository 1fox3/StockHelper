package com.fox.stockhelper.api.login;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.login.LoginApiDto;

/**
 * 登录
 * @author lusongsong
 */
public class LoginApi extends BaseApi {
    public LoginApi() {
        url = "stockHelper/login/login";
        method = METHOD_POST;
        requestParamKeys = new String[]{"account", "verifyCode"};
        dataClass = LoginApiDto.class;
    }
}
