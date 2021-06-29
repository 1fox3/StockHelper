package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 股票分钟交易数据
 *
 * @author lusongsong
 * @date 2021/4/26 15:14
 */
@Data
public class StockMinuteKLineNodePo {
    /**
     * 时间
     */
    String time;
    /**
     * 价格
     */
    BigDecimal price;
    /**
     * 成交量
     */
    Long dealNum;
}
