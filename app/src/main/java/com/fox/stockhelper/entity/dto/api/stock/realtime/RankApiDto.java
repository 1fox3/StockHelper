package com.fox.stockhelper.entity.dto.api.stock.realtime;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 股票排行接口数据格式
 * @author lusongsong
 * @date 2020/8/28 11:14
 */
@Data
public class RankApiDto {
    /**
     * 股市
     */
    Integer stockMarket;
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
    BigDecimal currentPrice;
    /**
     * 涨幅
     */
    BigDecimal uptickRate;
    /**
     * 波动
     */
    BigDecimal surgeRate;
    /**
     * 成交量
     */
    Long dealNum;
    /**
     * 成交金额
     */
    BigDecimal dealMoney;
}
