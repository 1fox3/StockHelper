package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.util.List;

import lombok.Data;

/**
 * Top售价
 * @author lusongsong
 * @date 2020/8/21 16:34
 */
@Data
public class TopDealPriceDto {
    /**
     * 卖
     */
    List<TopDealPriceSingleDto> sell;
    /**
     * 买
     */
    List<TopDealPriceSingleDto> buy;
}
