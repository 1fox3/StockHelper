package com.fox.stockhelper.api.stock.offline;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.offline.DealDayApiDto;

/**
 * 股票历史交易按月数据
 * @author lusongsong
 * @date 2020/10/22 19:54
 */
public class DealMonthApi extends BaseApi {
    public DealMonthApi() {
        url = "stock/offline/month";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockId","fqType"};
        dataClass = DealDayApiDto.class;
        isListData = true;
    }
}
