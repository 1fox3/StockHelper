package com.fox.stockhelper.entity.dto.api.stock.realtime;

/**
 * 涨跌统计
 * @author lusongsong
 * @date 2020/8/25 20:27
 */
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

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public Integer getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(Integer upLimit) {
        this.upLimit = upLimit;
    }

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public Integer getDownLimit() {
        return downLimit;
    }

    public void setDownLimit(Integer downLimit) {
        this.downLimit = downLimit;
    }

    public Integer getFlat() {
        return flat;
    }

    public void setFlat(Integer flat) {
        this.flat = flat;
    }

    public Integer getStop() {
        return stop;
    }

    public void setStop(Integer stop) {
        this.stop = stop;
    }
}
