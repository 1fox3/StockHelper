package com.fox.stockhelper.serv.stock;

import com.fox.stockhelper.constant.stock.StockMarketStatusConst;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelper.util.StockUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * 股市交易状态
 * @author lusongsong
 * @date 2020/8/20 14:01
 */
public class StockMarketStatusServ {

    /**
     * 获取股市交易状态
     * @return
     */
    public static int getStockMarketStatus() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        //交易日状态
        if (DateUtil.getCurrentDate().equals(StockUtil.lastDealDate())) {
            if (0 <= hour && hour < 9) {
                return StockMarketStatusConst.INIT;
            } else if (9 <= hour && hour < 10) {
                if (0 <= minutes && minutes < 15) {
                    return StockMarketStatusConst.INIT;
                } else if (15 <= minutes && minutes < 25) {
                    return StockMarketStatusConst.COMPETE;
                } else if (25 <= minutes && minutes < 30) {
                    return StockMarketStatusConst.SOON;
                } else {
                    return StockMarketStatusConst.OPEN;
                }
            } else if (10 <= hour && hour < 11) {
                return StockMarketStatusConst.OPEN;
            } else if (11 <= hour && hour < 12) {
                if (30 <= minutes) {
                    return StockMarketStatusConst.OPEN;
                } else {
                    return StockMarketStatusConst.NOON;
                }
            } else if (12 <= hour && hour < 13) {
                return StockMarketStatusConst.NOON;
            } else if (13 <= hour && hour < 15) {
                return StockMarketStatusConst.OPEN;
            } else {
                return StockMarketStatusConst.CLOSE;
            }
        }
        return StockMarketStatusConst.REST;
    }
}
