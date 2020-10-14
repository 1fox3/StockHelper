package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Top指标
 * @author lusongsong
 * @date 2020/8/21 16:34
 */
@Data
public class TopIndexApiDto {
    //股票id
    Integer stockId;
    //股票名称
    String stockName;
    //当前价格
    BigDecimal currentPrice;
    //今日开盘价
    BigDecimal openPrice;
    //今日最高价
    BigDecimal highestPrice;
    //今日最低价
    BigDecimal lowestPrice;
    //昨日收盘价
    BigDecimal preClosePrice;
    //成交股数
    Long dealNum;
    //成交金额
    BigDecimal dealMoney;
}
