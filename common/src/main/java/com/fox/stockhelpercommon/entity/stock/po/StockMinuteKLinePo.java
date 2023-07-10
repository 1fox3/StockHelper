package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;
import java.util.List;


/**
 * 股票分钟交易线图数据
 *
 * @author lusongsong
 * @date 2021/4/26 15:13
 */
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

    public Integer getStockMarket() {
        return stockMarket;
    }

    public void setStockMarket(Integer stockMarket) {
        this.stockMarket = stockMarket;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public BigDecimal getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(BigDecimal preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public List<StockMinuteKLineNodePo> getKlineData() {
        return klineData;
    }

    public void setKlineData(List<StockMinuteKLineNodePo> klineData) {
        this.klineData = klineData;
    }
}
