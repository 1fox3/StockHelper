package com.fox.stockhelpercommon.spider.out;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockRealtimeDealInfoPo;
import com.fox.stockhelpercommon.spider.in.StockSpiderRealtimeDealInfoApiInterface;
import com.fox.stockhelpercommon.spider.in.realtime.dealinfo.NetsSpiderRealtimeDealInfoApiImpl;
import com.fox.stockhelpercommon.spider.in.realtime.dealinfo.SinaSpiderRealtimeDealInfoApiImpl;
import com.fox.stockhelpercommon.spider.in.realtime.dealinfo.TencentSpiderRealtimeDealInfoApiImpl;
import com.fox.stockhelpercommon.spider.in.realtime.minutekline.NetsSpiderRealtimeMinuteKLineApiImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 股票最新交易日交易数据
 *
 * @author lusongsong
 * @date 2021/1/26 16:53
 */
public class StockSpiderRealtimeDealInfoApi extends StockSpiderBaseApi {
    /**
     * 对应接口
     */
    private static Class<StockSpiderRealtimeDealInfoApiInterface> clazz =
            StockSpiderRealtimeDealInfoApiInterface.class;

    /**
     * 对应实现类接口
     */
    public StockSpiderRealtimeDealInfoApi() {
        implClassList = Arrays.asList(
                Arrays.asList(NetsSpiderRealtimeDealInfoApiImpl.class, false),
                Arrays.asList(SinaSpiderRealtimeDealInfoApiImpl.class, true),
                Arrays.asList(TencentSpiderRealtimeDealInfoApiImpl.class, false)
        );
    }

    /**
     * 获取单只股票的实时交易信息
     *
     * @param stockVo
     * @return
     */
    public StockRealtimeDealInfoPo realtimeDealInfo(StockVo stockVo) {
        demoStockVo = stockVo;
        return getBean(clazz).realtimeDealInfo(stockVo);
    }

    /**
     * 批量获取股票的实时交易信息
     *
     * @param stockVoList
     * @return
     */
    public Map<String, StockRealtimeDealInfoPo> batchRealtimeDealInfo(List<StockVo> stockVoList) {
        if (null == stockVoList || stockVoList.isEmpty()) {
            return null;
        }
        demoStockVo = stockVoList.get(0);
        return getBean(clazz).batchRealtimeDealInfo(stockVoList);
    }
}
