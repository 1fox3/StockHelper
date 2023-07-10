package com.fox.stockhelpercommon.entity.stock.po;

import java.math.BigDecimal;

/**
 * 股票分钟交易数据
 *
 * @author lusongsong
 * @date 2021/4/26 15:14
 */
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getDealNum() {
        return dealNum;
    }

    public void setDealNum(Long dealNum) {
        this.dealNum = dealNum;
    }
}
