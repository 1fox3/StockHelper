package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;

/**
 * 股票交易价格
 * @author lusongsong
 * @date 2020/8/21 16:34
 */
public class TopDealPriceSingleDto {
    /**
     * 数量
     */
    Long num;
    /**
     * 价格
     */
    BigDecimal price;

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
