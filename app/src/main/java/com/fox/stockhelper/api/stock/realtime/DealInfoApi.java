package com.fox.stockhelper.api.stock.realtime;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;

/**
 * 实时交易信息
 * @author lusongsong
 * @date 2020/9/17 14:18
 */
public class DealInfoApi extends BaseApi {
    public DealInfoApi() {
        url = "stock/realtime/info";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockId",};
        dataClass = DealInfoApiDto.class;
    }
}
