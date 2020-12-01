package com.fox.stockhelper.api.stock.stockmarket;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.stockmarket.AroundDealDateApiDto;

/**
 * 股市交易日接口
 *
 * @author lusongsong
 * @date 2020/11/30 16:54
 */
public class AroundDealDateApi extends BaseApi {
    public AroundDealDateApi() {
        url = "stock/stockMarket/aroundDealDate";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockMarket"};
        dataClass = AroundDealDateApiDto.class;
    }
}