package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * @author lusongsong
 * @date 2021/5/26 16:32
 */
@Data
public class StockKLinePo {
    /**
     * 股票所属交易所
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
     * 线图类型
     */
    Integer kLineType;
    /**
     * 线图的点数量
     */
    Integer nodeCount;
    /**
     * 昨日收盘价
     */
    BigDecimal preClosePrice;
    /**
     * 天粒度成交信息
     */
    List<StockKLineNodePo> klineData;
}