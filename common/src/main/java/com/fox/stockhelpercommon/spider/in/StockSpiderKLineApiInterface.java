package com.fox.stockhelpercommon.spider.in;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockKLinePo;

/**
 * k线图接口
 *
 * @author lusongsong
 * @date 2021/5/26 16:20
 */
public interface StockSpiderKLineApiInterface extends StockSpiderApiBaseInterface {
    /**
     * K线图接口
     *
     * @param stockVo
     * @param dateType
     * @param fqType
     * @param startDate
     * @param endDate
     * @return
     */
    StockKLinePo kLine(StockVo stockVo, int dateType, int fqType, String startDate, String endDate);
}
