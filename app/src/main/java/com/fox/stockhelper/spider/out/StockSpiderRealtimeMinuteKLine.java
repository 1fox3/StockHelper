package com.fox.stockhelper.spider.out;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.entity.po.stock.StockRealtimeMinuteKLinePo;
import com.fox.stockhelper.spider.in.StockSpiderRealtimeMinuteKLineApiInterface;
import com.fox.stockhelper.spider.in.realtime.minutekline.NetsSpiderRealtimeMinuteKLineApiImpl;
import com.fox.stockhelper.spider.in.realtime.minutekline.TencentSpiderRealtimeMinuteKLineApiImpl;

import java.util.Arrays;

/**
 * 股票最新交易日分钟线图数据
 *
 * @author lusongsong
 * @date 2021/2/1 15:53
 */
public class StockSpiderRealtimeMinuteKLine extends StockSpiderBase {
    /**
     * 对应接口
     */
    private static Class<StockSpiderRealtimeMinuteKLineApiInterface> clazz =
            StockSpiderRealtimeMinuteKLineApiInterface.class;

    /**
     * 注册接口实现类
     */
    public StockSpiderRealtimeMinuteKLine() {
        implClassList = Arrays.asList(
                Arrays.asList(NetsSpiderRealtimeMinuteKLineApiImpl.class, false),
                Arrays.asList(TencentSpiderRealtimeMinuteKLineApiImpl.class, true)
        );
    }

    /**
     * 股票最新交易日分钟线图数据
     *
     * @param stockVo
     * @return
     */
    public StockRealtimeMinuteKLinePo realtimeMinuteKLine(StockVo stockVo) {
        demoStockVo = stockVo;
        return getBean(clazz).realtimeMinuteKLine(stockVo);
    }
}
