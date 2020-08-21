package com.fox.stockhelper.ui.listener;

import android.os.Message;

/**
 * 通用的消息梳理
 * @author lusongsong
 * @date 2020/8/21 13:45
 */
public interface CommonHandleListener {
    /**
     * 消息处理
     */
    void handleMessage(Message message);
}
