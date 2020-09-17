package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.util.List;

import lombok.Data;

/**
 * 股票排行接口数据格式
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
    Float todayOpenPrice;
    /**
     * 昨日收盘价
     */
    Float yesterdayClosePrice;
    /**
     * 当前价格
     */
    Float currentPrice;
    /**
     * 今日最高价
     */
    Float todayHighestPrice;
    /**
     * 今日最低价
     */
    Float todayLowestPrice;
    /**
     * 价格涨幅
     */
    Float uptickPrice;
    /**
     * 增长率
     */
    Float uptickRate;
    /**
     * 波动
     */
    Float surgeRate;
    /**
     * 竞买价
     */
    Float competeBuyPrice;
    /**
     * 竞卖价
     */
    Float competeSellPrice;
    /**
     * 当前分钟最高价
     */
    Float minuteHighestPrice;
    /**
     * 当前分钟最低价
     */
    Float minuteLowestPrice;
    /**
     * 成交股数
     */
    Long dealNum;
    /**
     * 成交金额
     */
    Double dealMoney;
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
