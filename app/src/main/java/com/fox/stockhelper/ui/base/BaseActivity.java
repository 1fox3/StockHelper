package com.fox.stockhelper.ui.base;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity基类
 * @author lusongsong
 */
public class BaseActivity  extends AppCompatActivity {
    /**
     * 用户登录的sessionId
     */
    private String sessionId = "";
    /**
     * 登录过期时间
     */
    private long expireTime;

    /**
     * 用户是否已登录
     * @return
     */
    protected boolean isLogin() {
        return sessionId.equals("") ? false : true;
    }

    /**
     * 设置登录信息
     * @param sessionId
     * @param expireTime
     */
    protected void setLoginSession(String sessionId, long expireTime) {
        this.sessionId = sessionId;
        this.expireTime = expireTime;
    }
}
