package com.fox.stockhelper.config;

/**
 * 消息的what定义
 * @author lusongsong
 */
public class MsgWhatConfig {
    /**
     * 发送验证码
     */
    public static final int SEND_CODE = 1;
    /**
     * 登录
     */
    public static final int LOGIN = 2;
    /**
     * 股市状态
     */
    public static final int SM_STATUS = 3;
    /**
     * 重点指数
     */
    public static final int TOP_INDEX = 4;
    /**
     * 涨跌幅统计
     */
    public static final int UPTICK_RATE_STATISTICS = 5;
    /**
     * 股票排行
     */
    public static final int STOCK_RANK = 6;
    /**
     * 刷新股票排行
     */
    public static final int STOCK_RANK_FRESH = 7;
    /**
     * 股票实时交易信息
     */
    public static final int STOCK_DEAL_INFO = 8;
    /**
     * 股票实时交易线图信息
     */
    public static final int STOCK_DEAL_PRICE_LINE = 9;
}
