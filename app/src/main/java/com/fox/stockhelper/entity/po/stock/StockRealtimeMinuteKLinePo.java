package com.fox.stockhelper.entity.po.stock;

import java.math.BigDecimal;
import java.util.List;

/**
 * 股票最新交易日分钟线图数据
 *
 * @author lusongsong
 * @date 2021/2/1 15:33
 */
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

    public List<StockRealtimeMinuteNodeDataPo> getKlineData() {
        return klineData;
    }

    public void setKlineData(List<StockRealtimeMinuteNodeDataPo> klineData) {
        this.klineData = klineData;
    }
}
