package com.fox.stockhelper.api.stock.realtime;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.realtime.TopDealPriceDto;

/**
 * 股票排行
 * @author lusongsong
 * @date 2020/8/28 11:11
 */
public class PriceListApi extends BaseApi {
    public PriceListApi() {
        url = "stock/realtime/priceList";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockId",};
        dataClass = TopDealPriceDto.class;
    }
}
