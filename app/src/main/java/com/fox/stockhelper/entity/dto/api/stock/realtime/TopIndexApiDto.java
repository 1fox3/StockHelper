package com.fox.stockhelper.entity.dto.api.stock.realtime;

import lombok.Data;

/**
 * Top指标
 * @author lusongsong
 * @date 2020/8/21 16:34
 */
@Data
public class TopIndexApiDto {
    //股票id
    private Integer stockId;
    //股票名称
    private String stockName;
    //今日开盘价
    private Float todayOpenPrice;
    //昨日收盘价
    private Float yesterdayClosePrice;
    //当前价格
    private Float currentPrice;
    //今日最高价
    private Float todayHighestPrice;
    //今日最低价
    private Float todayLowestPrice;
    //成交股数
    private Long dealNum;
    //成交金额
    private Double dealMoney;
}
