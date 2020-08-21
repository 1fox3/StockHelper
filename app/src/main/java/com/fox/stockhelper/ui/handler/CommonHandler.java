package com.fox.stockhelper.ui.handler;

import android.os.Handler;
import android.os.Message;

import com.fox.stockhelper.ui.listener.CommonHandleListener;

import androidx.annotation.NonNull;

/**
 * 通用handler类
 * @author lusongsong
 * @date 2020/8/21 13:43
 */
public class CommonHandler extends Handler {
    /**
     * 消息处理对象
     */
    private CommonHandleListener commonHandleListener;

    /**
     * 构造函数
     * @param commonHandleListener
     */
    public CommonHandler(CommonHandleListener commonHandleListener) {
        this.commonHandleListener = commonHandleListener;
    }

    /**
     * 消息处理
     * @param message
     */
    @Override
    public void handleMessage(@NonNull Message message) {
        this.commonHandleListener.handleMessage(message);
    }
}
