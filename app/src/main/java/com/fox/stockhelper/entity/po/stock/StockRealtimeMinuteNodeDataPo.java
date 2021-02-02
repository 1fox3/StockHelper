package com.fox.stockhelper.entity.po.stock;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 股票最新交易日分钟交易数据
 *
 * @author lusongsong
 * @date 2021/2/1 15:42
 */
@Data
public class StockRealtimeMinuteNodeDataPo {
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
