package com.fox.stockhelper.entity.dto.api.stock.realtime;

import lombok.Data;

/**
 * 股票排行接口数据格式
 * @author lusongsong
 * @date 2020/8/28 11:14
 */
@Data
public class RankApiDto {
    /**
     * 股票id
     */
    Integer stockId;
    /**
     * 股票代码
     */
    String stockCode;
    /**
     * 股票名称
     */
    String stockName;
    /**
     * 股票价格
     */
    Float price;
    /**
     * 涨幅
     */
    Float uptickRate;
    /**
     * 波动
     */
    Float surgeRate;
    /**
     * 成交量
     */
    Float dealNum;
    /**
     * 成交金额
     */
    Float dealMoney;
}
