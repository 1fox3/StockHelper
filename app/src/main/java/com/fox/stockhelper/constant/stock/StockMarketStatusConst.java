package com.fox.stockhelper.constant.stock;

import com.fox.stockhelper.R;

/**
 * 股市状态相关常量
 * @author lusongsong
 * @date 2020/8/20 10:20
 */
public class StockMarketStatusConst {
    /**
     * 未知的状态
     */
    public static final int UNKNOWN = 0;
    /**
     * 开盘
     */
    public static final int OPEN = 1;
    /**
     * 收盘
     */
    public static final int CLOSE = 2;
    /**
     * 休市
     */
    public static final int REST = 3;
    /**
     * 竞价
     */
    public static final int COMPETE = 4;
    /**
     * 未开盘
     */
    public static final int INIT = 5;
    /**
     * 中午休息
     */
    public static final int NOON = 6;
    /**
     * 即将开盘
     */
    public static final int SOON = 7;

    /**
     * 状态信息
     */
    public static final int STATUS_INFO[][] =  new int[][] {
            {R.drawable.sm_status_unknown, R.string.sm_status_unknown},
            {R.drawable.sm_status_open, R.string.sm_status_open},
            {R.drawable.sm_status_close, R.string.sm_status_close},
            {R.drawable.sm_status_open, R.string.sm_status_rest},
            {R.drawable.sm_status_compete, R.string.sm_status_compete},
            {R.drawable.sm_status_init, R.string.sm_status_init},
            {R.drawable.sm_status_noon, R.string.sm_status_noon},
            {R.drawable.sm_status_soon, R.string.sm_status_soon},
    };

    /**
     * 获取状态信息
     * @param status
     * @return
     */
    public static final int[] getStatusInfo(int status) {
        status = 0 <= status && status < STATUS_INFO.length ? status : UNKNOWN;
        return STATUS_INFO[status];
    }
}
