package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 分钟线图点
 * @author lusongsong
 * @date 2020/9/17 17:33
 */
@Data
public class MinuteDealPriceInfoDto {
    /**
     * 时间(分)
     */
    String time;
    /**
     * 价格
     */
    BigDecimal price;
    /**
     * 均价
     */
    BigDecimal avgPrice;
    /**
     * 成交量
     */
    Long dealNum;
}
