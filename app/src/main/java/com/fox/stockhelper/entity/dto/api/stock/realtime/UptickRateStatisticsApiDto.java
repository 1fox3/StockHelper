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
    private Integer up;
    /**
     * 涨停
     */
    private Integer upLimit;
    /**
     * 跌
     */
    private Integer down;
    /**
     * 跌停
     */
    private Integer downLimit;
    /**
     * 平
     */
    private Integer flat;
    /**
     * 停牌
     */
    private Integer stop;
}
