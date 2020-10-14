package com.fox.stockhelper.entity.dto.api.login;

import lombok.Data;

/**
 * 登录
 * @author lusongsong
 * @date 2020/7/21 17:58
 */
@Data
public class LoginApiDto {
    /**
     * 登录session
     */
    String sessionid;
    /**
     * 失效时间
     */
    Integer expireTime;
}
