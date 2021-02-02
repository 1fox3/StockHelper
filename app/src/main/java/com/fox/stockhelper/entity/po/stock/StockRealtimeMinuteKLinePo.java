package com.fox.stockhelper.entity.po.stock;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * 股票最新交易日分钟线图数据
 *
 * @author lusongsong
 * @date 2021/2/1 15:33
 */
@Data
public class StockRealtimeMinuteKLinePo {
    /**
     * 股票所属交易所
     */
    Integer stockMarket;
    /**
     * 股票代码
     */
    String stockCode;
    /**
     * 日期
     */
    String dt;
    /**
     * 线图的点数量
     */
    Integer nodeCount;
    /**
     * 上个交易日收盘价
     */
    BigDecimal preClosePrice;
    /**
     *
     */
    List<StockRealtimeMinuteNodeDataPo> klineData;
}
