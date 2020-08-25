package com.fox.stockhelper.api.stock.realtime;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.realtime.TopIndexApiDto;

/**
 * 重点指标
 * @author lusongsong
 * @date 2020/8/21 16:36
 */
public class TopIndexApi extends BaseApi {
    public TopIndexApi() {
        url = "stock/realtime/topIndex";
        method = METHOD_POST;
        requestParamKeys = new String[]{};
        dataClass = TopIndexApiDto.class;
        isListData = true;
    }
}
