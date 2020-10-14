package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * 股票实时交易信息
 * @author lusongsong
 * @date 2020/8/28 11:14
 */
@Data
public class DealInfoApiDto {
    /**
     * 股票id
     */
    Integer stockId;
    /**
     * 股票代码
     */
    String stockCode;
    /**
     * 股票名称
     */
    String stockName;
    /**
     * 股票英文名
     */
    String stockNameEn;
    /**
     * 今日开盘价
     */
    BigDecimal openPrice;
    /**
     * 昨日收盘价
     */
    BigDecimal preClosePrice;
    /**
     * 当前价格
     */
    BigDecimal currentPrice;
    /**
     * 今日最高价
     */
    BigDecimal highestPrice;
    /**
     * 今日最低价
     */
    BigDecimal lowestPrice;
    /**
     * 价格涨幅
     */
    BigDecimal uptickPrice;
    /**
     * 增长率
     */
    BigDecimal uptickRate;
    /**
     * 波动
     */
    BigDecimal surgeRate;
    /**
     * 竞买价
     */
    BigDecimal competeBuyPrice;
    /**
     * 竞卖价
     */
    BigDecimal competeSellPrice;
    /**
     * 当前分钟最高价
     */
    BigDecimal minuteHighestPrice;
    /**
     * 当前分钟最低价
     */
    BigDecimal minuteLowestPrice;
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
    List<TopDealPriceSingleDto> buyPriceList;
    /**
     * 排名靠前的5个卖方报价
     */
    List<TopDealPriceSingleDto> sellPriceList;
    /**
     * 当前日期
     */
    String currentDate;
    /**
     * 当前时间
     */
    String currentTime;
    /**
     * 交易状态
     */
    String dealStatus;
    /**
     * 未知的数据列表
     */
    List<String> unknownKeyList;
}
