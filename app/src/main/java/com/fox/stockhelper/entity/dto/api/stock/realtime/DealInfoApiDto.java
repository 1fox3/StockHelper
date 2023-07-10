package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;
import java.util.List;

/**
 * 股票实时交易信息
 * @author lusongsong
 * @date 2020/8/28 11:14
 */
public class DealInfoApiDto {
    /**
     * 股票id
     */
    Integer stockId;
    /**
     * 股票代码
     */
    String stockCode;
    /**
     * 股票名称
     */
    String stockName;
    /**
     * 股票英文名
     */
    String stockNameEn;
    /**
     * 今日开盘价
     */
    BigDecimal openPrice;
    /**
     * 昨日收盘价
     */
    BigDecimal preClosePrice;
    /**
     * 当前价格
     */
    BigDecimal currentPrice;
    /**
     * 今日最高价
     */
    BigDecimal highestPrice;
    /**
     * 今日最低价
     */
    BigDecimal lowestPrice;
    /**
     * 价格涨幅
     */
    BigDecimal uptickPrice;
    /**
     * 增长率
     */
    BigDecimal uptickRate;
    /**
     * 波动
     */
    BigDecimal surgeRate;
    /**
     * 竞买价
     */
    BigDecimal competeBuyPrice;
    /**
     * 竞卖价
     */
    BigDecimal competeSellPrice;
    /**
     * 当前分钟最高价
     */
    BigDecimal minuteHighestPrice;
    /**
     * 当前分钟最低价
     */
    BigDecimal minuteLowestPrice;
    /**
     * 成交股数
     */
    Long dealNum;
    /**
     * 成交金额
     */
    BigDecimal dealMoney;
    /**
     * 排名靠前的5个买方报价
     */
    List<TopDealPriceSingleDto> buyPriceList;
    /**
     * 排名靠前的5个卖方报价
     */
    List<TopDealPriceSingleDto> sellPriceList;
    /**
     * 当前日期
     */
    String currentDate;
    /**
     * 当前时间
     */
    String currentTime;
    /**
     * 交易状态
     */
    String dealStatus;
    /**
     * 未知的数据列表
     */
    List<String> unknownKeyList;

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
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

    public String getStockNameEn() {
        return stockNameEn;
    }

    public void setStockNameEn(String stockNameEn) {
        this.stockNameEn = stockNameEn;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(BigDecimal preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public BigDecimal getUptickPrice() {
        return uptickPrice;
    }

    public void setUptickPrice(BigDecimal uptickPrice) {
        this.uptickPrice = uptickPrice;
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

    public BigDecimal getCompeteBuyPrice() {
        return competeBuyPrice;
    }

    public void setCompeteBuyPrice(BigDecimal competeBuyPrice) {
        this.competeBuyPrice = competeBuyPrice;
    }

    public BigDecimal getCompeteSellPrice() {
        return competeSellPrice;
    }

    public void setCompeteSellPrice(BigDecimal competeSellPrice) {
        this.competeSellPrice = competeSellPrice;
    }

    public BigDecimal getMinuteHighestPrice() {
        return minuteHighestPrice;
    }

    public void setMinuteHighestPrice(BigDecimal minuteHighestPrice) {
        this.minuteHighestPrice = minuteHighestPrice;
    }

    public BigDecimal getMinuteLowestPrice() {
        return minuteLowestPrice;
    }

    public void setMinuteLowestPrice(BigDecimal minuteLowestPrice) {
        this.minuteLowestPrice = minuteLowestPrice;
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

    public List<TopDealPriceSingleDto> getBuyPriceList() {
        return buyPriceList;
    }

    public void setBuyPriceList(List<TopDealPriceSingleDto> buyPriceList) {
        this.buyPriceList = buyPriceList;
    }

    public List<TopDealPriceSingleDto> getSellPriceList() {
        return sellPriceList;
    }

    public void setSellPriceList(List<TopDealPriceSingleDto> sellPriceList) {
        this.sellPriceList = sellPriceList;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public List<String> getUnknownKeyList() {
        return unknownKeyList;
    }

    public void setUnknownKeyList(List<String> unknownKeyList) {
        this.unknownKeyList = unknownKeyList;
    }
}
