package com.fox.stockhelper.spider.in;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.entity.po.stock.StockRealtimeMinuteKLinePo;

/**
 * 股票最新交易日分钟线图数据
 *
 * @author lusongsong
 * @date 2021/2/1 15:30
 */
public interface StockSpiderRealtimeMinuteKLineApiInterface extends StockSpiderApiBaseInterface{
    /**
     * 股票最新交易日分钟线图数据
     *
     * @param stockVo
     * @return
     */
    StockRealtimeMinuteKLinePo realtimeMinuteKLine(StockVo stockVo);
}
