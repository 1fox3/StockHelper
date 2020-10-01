package com.fox.stockhelper.api.stock.realtime;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;

/**
 * 股票排行
 * @author lusongsong
 * @date 2020/8/28 11:11
 */
public class RankApi extends BaseApi {
    public RankApi() {
        url = "stock/realtime/rank";
        method = METHOD_POST;
        requestParamKeys = new String[]{"type", "sortType", "pageNum", "pageSize"};
        dataClass = RankApiDto.class;
        isListObject = true;
    }
}
