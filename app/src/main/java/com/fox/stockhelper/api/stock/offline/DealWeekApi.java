package com.fox.stockhelper.api.stock.offline;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.offline.DealDayApiDto;

/**
 * 股票历史交易按周数据
 * @author lusongsong
 * @date 2020/10/22 19:48
 */
public class DealWeekApi extends BaseApi {
    public DealWeekApi() {
        url = "stock/offline/week";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockId","fqType"};
        dataClass = DealDayApiDto.class;
        isListData = true;
    }
}
