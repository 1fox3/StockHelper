package com.fox.stockhelper.entity.dto.api.login;

/**
 * 登录
 * @author lusongsong
 * @date 2020/7/21 17:58
 */
public class LoginApiDto {
    /**
     * 登录session
     */
    String sessionid;
    /**
     * 失效时间
     */
    Integer expireTime;

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }
}
