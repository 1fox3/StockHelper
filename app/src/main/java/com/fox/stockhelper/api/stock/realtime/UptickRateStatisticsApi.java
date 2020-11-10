package com.fox.stockhelper.api.stock.realtime;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.realtime.UptickRateStatisticsApiDto;

/**
 * 涨跌统计
 * @author lusongsong
 * @date 2020/8/21 16:36
 */
public class UptickRateStatisticsApi extends BaseApi {
    public UptickRateStatisticsApi() {
        url = "stock/realtime/uptickRateStatistics";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockMarket",};
        dataClass = UptickRateStatisticsApiDto.class;
    }
}
