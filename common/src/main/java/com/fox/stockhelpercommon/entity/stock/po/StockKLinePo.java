package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lusongsong
 * @date 2021/5/26 16:32
 */
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

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Integer getkLineType() {
        return kLineType;
    }

    public void setkLineType(Integer kLineType) {
        this.kLineType = kLineType;
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

    public List<StockKLineNodePo> getKlineData() {
        return klineData;
    }

    public void setKlineData(List<StockKLineNodePo> klineData) {
        this.klineData = klineData;
    }
}