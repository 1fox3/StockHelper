package com.fox.stockhelper.api.stock.offline;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.offline.DealDayApiDto;

/**
 * 图片历史交易按天数据
 * @author lusongsong
 * @date 2020/9/30 16:41
 */
public class DealDayApi extends BaseApi {
    public DealDayApi() {
        url = "/stock/offline/day";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockId","fqType"};
        dataClass = DealDayApiDto.class;
        isListData = true;
    }
}
