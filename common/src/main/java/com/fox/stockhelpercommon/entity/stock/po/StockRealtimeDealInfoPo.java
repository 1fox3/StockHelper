package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;
import java.util.TreeMap;

import lombok.Data;

/**
 * 股票最新交易日交易信息
 *
 * @author lusongsong
 * @date 2021/4/26 15:21
 */
@Data
public class StockRealtimeDealInfoPo {
    /**
     * 股票交易所
     */
    Integer stockMarket;
    /**
     * 股票代码
     */
    String stockCode;
    /**
     * 股票名称
     */
    String stockName;
    /**
     * 当前日期
     */
    String dt;
    /**
     * 当前时间
     */
    String time;
    /**
     * 当前价格
     */
    BigDecimal currentPrice;
    /**
     * 今日开盘价
     */
    BigDecimal openPrice;
    /**
     * 今日最高价
     */
    BigDecimal highestPrice;
    /**
     * 今日最低价
     */
    BigDecimal lowestPrice;
    /**
     * 昨日收盘价
     */
    BigDecimal preClosePrice;
    /**
     * 成交股数
     */
    Long dealNum;
    /**
     * 成交金额
     */
    BigDecimal dealMoney;
    /**
     * 排名靠前的5个买方报价
     */
    TreeMap<BigDecimal, Long> buyPriceMap;
    /**
     * 排名靠前的5个卖方报价
     */
    TreeMap<BigDecimal, Long> sellPriceMap;
}
