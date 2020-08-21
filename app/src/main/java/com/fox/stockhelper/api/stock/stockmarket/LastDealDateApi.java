package com.fox.stockhelper.api.stock.stockmarket;

import com.fox.stockhelper.api.BaseApi;

/**
 * 上一个交易日
 * @author lusongsong
 * @date 2020/8/20 15:03
 */
public class LastDealDateApi extends BaseApi {
    public LastDealDateApi() {
        url = "stock/stockMarket/lastDealDate";
        method = METHOD_POST;
        requestParamKeys = new String[]{};
        dataClass = String.class;
    }
}
