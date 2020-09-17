package com.fox.stockhelper.api.stock.realtime;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealPriceLineApiDto;

/**
 * @author lusongsong
 * @date 2020/9/17 17:30
 */
public class DealPriceLineApi extends BaseApi {
    public DealPriceLineApi() {
        url = "stock/realtime/line";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockId",};
        dataClass = DealPriceLineApiDto.class;
    }
}
