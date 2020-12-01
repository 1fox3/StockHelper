package com.fox.stockhelper.ui.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.fox.stockhelper.R;
import com.fox.stockhelper.util.DateUtil;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity基类
 *
 * @author lusongsong
 * @date 2020-08-19
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * 登录的session,sp保存key
     */
    static final String SP_LOGIN_SESSION = "login_session";
    /**
     * 登录的过期时间,sp保存key
     */
    static final String SP_LOGIN_EXPIRE_TIME = "login_expire_time";
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
     *
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
     *
     * @param sessionId
     * @param expireTime
     */
    protected void setLoginSession(String sessionId, int expireTime) {
        BaseActivity.sessionId = sessionId;
        BaseActivity.expireTime = DateUtil.secondTimestamp() + expireTime;
        spEditor.putString(BaseActivity.SP_LOGIN_SESSION, BaseActivity.sessionId);
        spEditor.putInt(BaseActivity.SP_LOGIN_EXPIRE_TIME, BaseActivity.expireTime);
        spEditor.commit();
    }

    /**
     * 获取登录sessionId
     *
     * @return
     */
    protected String getLoginSession() {
        Log.e("getLoginSession", BaseActivity.sessionId);
        return BaseActivity.sessionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改顶部状态栏颜色
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.main));

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name_en), MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
        sessionId = sharedPreferences.getString(BaseActivity.SP_LOGIN_SESSION, "");
        expireTime = sharedPreferences.getInt(BaseActivity.SP_LOGIN_EXPIRE_TIME, 0);
    }
}
