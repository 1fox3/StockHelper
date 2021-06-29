package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * 股票分钟交易线图数据
 *
 * @author lusongsong
 * @date 2021/4/26 15:13
 */
@Data
public class StockMinuteKLinePo {
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
    List<StockMinuteKLineNodePo> klineData;
}
