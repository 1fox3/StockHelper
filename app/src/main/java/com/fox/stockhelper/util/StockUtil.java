package com.fox.stockhelper.util;

import com.fox.stockhelper.api.stock.stockmarket.LastDealDateApi;

/**
 * 股市工具类
 * @author lusongsong
 * @date 2020/8/20 15:06
 */
public class StockUtil {

    /**
     * 最新交易日
     * @return
     */
    public static String lastDealDate() {
        LastDealDateApi lastDealDateApi = new LastDealDateApi();
        return (String) lastDealDateApi.request();
    }
}
