package com.fox.stockhelper.entity.dto.api.login;

import lombok.Data;

/**
 * 登录
 * @author lusongsong
 */
@Data
public class LoginApiDto {
    String sessionid;
    int expireTime;
}
