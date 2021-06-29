package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author lusongsong
 * @date 2021/5/26 16:32
 */
@Data
public class StockKLineNodePo {
    /**
     * 日期
     */
    String dt;
    /**
     * 开盘价
     */
    BigDecimal openPrice;
    /**
     * 最高价
     */
    BigDecimal highestPrice;
    /**
     * 最低价
     */
    BigDecimal lowestPrice;
    /**
     * 收盘价
     */
    BigDecimal closePrice;
    /**
     * 成交量
     */
    Long dealNum;
    /**
     * 成交额
     */
    BigDecimal dealMoney;
}
