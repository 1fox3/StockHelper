package com.fox.stockhelper.util;

import android.util.Log;

/**
 * 日志工具类
 *
 * @author lusongsong
 * @date 2020/11/27 17:27
 */
public class LogUtil {
    /**
     * 记录错误日志
     *
     * @param tag
     * @param msg
     */
    private static void logError(String tag, String msg) {
        Log.e(tag, msg);
    }

    /**
     * 日志tag
     *
     * @param stackLevel
     * @param tag
     * @return
     */
    private static String tag(int stackLevel, String tag) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3 + stackLevel];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stackTraceElement.getClassName());
        stringBuilder.append("/");
        stringBuilder.append(stackTraceElement.getMethodName());
        stringBuilder.append("/");
        stringBuilder.append(stackTraceElement.getLineNumber());
        if (null != tag && !tag.isEmpty()) {
            stringBuilder.append("/");
            stringBuilder.append(tag);
        }
        return stringBuilder.toString();
    }

    /**
     * 记录错误日志
     *
     * @param tag
     * @param object
     */
    public static void error(String tag, Object object) {
        logError(tag(0, tag), object.toString());
    }

    /**
     * 记录错误日志
     *
     * @param object
     */
    public static void error(Object object) {
        logError(tag(1, ""), object.toString());
    }
}
