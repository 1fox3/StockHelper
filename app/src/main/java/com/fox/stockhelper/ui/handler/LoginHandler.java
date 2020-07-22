package com.fox.stockhelper.ui.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.ui.activity.LoginActivity;

import androidx.annotation.NonNull;

/**
 * 登录相关的消息处理
 */
public class LoginHandler extends Handler {
    /**
     * 登录界面
     */
    private LoginActivity activity;
    public LoginHandler(LoginActivity activity) {
        this.activity = activity;
    }

    /**
     * 消息处理
     * @param msg
     */
    @Override
    public void handleMessage(@NonNull Message msg) {
        Bundle bundle = msg.getData();
        String message = bundle.getString("message");
        switch (msg.what) {
            case MsgWhatConfig.SEND_CODE:
                this.activity.toast(message);
                break;
            case MsgWhatConfig.LOGIN:
                this.activity.toast(message);
                this.activity.loginFinish();
                break;
        }
    }
}
