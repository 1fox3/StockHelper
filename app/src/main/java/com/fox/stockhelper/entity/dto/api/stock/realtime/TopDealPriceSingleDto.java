package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 股票交易价格
 * @author lusongsong
 * @date 2020/8/21 16:34
 */
@Data
public class TopDealPriceSingleDto {
    /**
     * 数量
     */
    Long num;
    /**
     * 价格
     */
    BigDecimal price;
}
