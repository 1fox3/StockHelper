package com.fox.stockhelper.ui.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.fox.stockhelper.R;
import com.fox.stockhelper.util.DateUtil;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity基类
 * @author lusongsong
 */
public class BaseActivity  extends AppCompatActivity {
    /**
     * 登录的session，sp保存key
     */
    final static String SP_LOGIN_SESSION = "login_session";
    /**
     * 登录的过期时间，sp保存key
     */
    final static String SP_LOGIN_EXPIRE_TIME = "login_expire_time";
    /**
     * 数据记录SharedPreferences
     */
    protected SharedPreferences sharedPreferences;
    /**
     * 数据记录SharedPreferences.Editor
     */
    protected SharedPreferences.Editor spEditor;
    /**
     * 用户登录的sessionId
     */
    private static String sessionId = "";
    /**
     * 登录过期时间
     */
    private static int expireTime;

    /**
     * 用户是否已登录
     * @return
     */
    protected boolean isLogin() {
        //sessionid为空时，未登录
        if (null == sessionId || sessionId.equals("")) {
            return false;
        }
        //过期时间在一天内，未登录
        if (expireTime - 86400 < DateUtil.secondTimestamp()) {
            return false;
        }
        return true;
    }

    /**
     * 设置登录信息
     * @param sessionId
     * @param expireTime
     */
    protected void setLoginSession(String sessionId, int expireTime) {
        Log.e("setSession", sessionId);
        Log.e("setSession", String.valueOf(expireTime));
        this.sessionId = sessionId;
        this.expireTime = DateUtil.secondTimestamp() + expireTime;
        spEditor.putString(BaseActivity.SP_LOGIN_SESSION, this.sessionId);
        spEditor.putInt(BaseActivity.SP_LOGIN_EXPIRE_TIME, this.expireTime);
        spEditor.commit();
    }

    /**
     * 获取登录sessionId
     * @return
     */
    protected String getLoginSession() {
        Log.e("getLoginSession", this.sessionId);
        return this.sessionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name_en), MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
        sessionId = sharedPreferences.getString(BaseActivity.SP_LOGIN_SESSION, "");
        expireTime = sharedPreferences.getInt(BaseActivity.SP_LOGIN_EXPIRE_TIME, 0);
    }
}
