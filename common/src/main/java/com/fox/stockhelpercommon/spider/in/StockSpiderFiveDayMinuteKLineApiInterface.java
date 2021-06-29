package com.fox.stockhelpercommon.spider.in;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;

import java.util.List;

/**
 * 股票近5个交易日的分钟粒度数据
 *
 * @author lusongsong
 * @date 2021/5/18 14:00
 */
public interface StockSpiderFiveDayMinuteKLineApiInterface extends StockSpiderApiBaseInterface {
    /**
     * 股票近5个交易日的分钟粒度数据
     *
     * @param stockVo
     * @return
     */
    List<StockMinuteKLinePo> fiveDayMinuteKLine(StockVo stockVo);
}
