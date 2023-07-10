package com.fox.stockhelper.entity.po.stock;

import java.math.BigDecimal;

/**
 * 股票最新交易日分钟交易数据
 *
 * @author lusongsong
 * @date 2021/2/1 15:42
 */
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
