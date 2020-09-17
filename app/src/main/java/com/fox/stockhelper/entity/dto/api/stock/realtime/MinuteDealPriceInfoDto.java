package com.fox.stockhelper.entity.dto.api.stock.realtime;

import lombok.Data;

/**
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
    Float price;
    /**
     * 均价
     */
    Float avgPrice;
    /**
     * 成交量
     */
    Long dealNum;
}
