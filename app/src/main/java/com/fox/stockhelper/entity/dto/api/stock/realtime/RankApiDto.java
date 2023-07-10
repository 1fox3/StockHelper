package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;

/**
 * 股票排行接口数据格式
 * @author lusongsong
 * @date 2020/8/28 11:14
 */
public class RankApiDto {
    /**
     * 股市
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
     * 股票价格
     */
    BigDecimal currentPrice;
    /**
     * 涨幅
     */
    BigDecimal uptickRate;
    /**
     * 波动
     */
    BigDecimal surgeRate;
    /**
     * 成交量
     */
    Long dealNum;
    /**
     * 成交金额
     */
    BigDecimal dealMoney;

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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getUptickRate() {
        return uptickRate;
    }

    public void setUptickRate(BigDecimal uptickRate) {
        this.uptickRate = uptickRate;
    }

    public BigDecimal getSurgeRate() {
        return surgeRate;
    }

    public void setSurgeRate(BigDecimal surgeRate) {
        this.surgeRate = surgeRate;
    }

    public Long getDealNum() {
        return dealNum;
    }

    public void setDealNum(Long dealNum) {
        this.dealNum = dealNum;
    }

    public BigDecimal getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(BigDecimal dealMoney) {
        this.dealMoney = dealMoney;
    }
}
