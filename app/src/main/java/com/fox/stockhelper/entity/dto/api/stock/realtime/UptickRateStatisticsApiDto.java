package com.fox.stockhelper.entity.dto.api.stock.realtime;

import lombok.Data;

/**
 * 涨跌统计
 * @author lusongsong
 * @date 2020/8/25 20:27
 */
@Data
public class UptickRateStatisticsApiDto {
    /**
     * 涨
     */
    Integer up;
    /**
     * 涨停
     */
    Integer upLimit;
    /**
     * 跌
     */
    Integer down;
    /**
     * 跌停
     */
    Integer downLimit;
    /**
     * 平
     */
    Integer flat;
    /**
     * 停牌
     */
    Integer stop;
}
