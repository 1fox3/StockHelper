package com.fox.stockhelper.api.stock.offline;

import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.entity.dto.api.stock.offline.FiveDayMinApiDto;

/**
 * @author lusongsong
 * @date 2020/10/23 16:27
 */
public class FiveDayMinApi extends BaseApi {
    public FiveDayMinApi() {
        url = "stock/offline/fiveDayMin";
        method = METHOD_POST;
        requestParamKeys = new String[]{"stockId"};
        dataClass = FiveDayMinApiDto.class;
        isListData = true;
    }
}
