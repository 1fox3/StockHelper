package com.fox.stockhelper.spider.out;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.entity.po.stock.StockRealtimeDealInfoPo;
import com.fox.stockhelper.spider.in.StockSpiderRealtimeDealInfoApiInterface;
import com.fox.stockhelper.spider.in.realtime.dealinfo.NetsSpiderRealtimeDealInfoApiImpl;
import com.fox.stockhelper.spider.in.realtime.dealinfo.SinaSpiderRealtimeDealInfoApiImpl;
import com.fox.stockhelper.spider.in.realtime.dealinfo.TencentSpiderRealtimeDealInfoApiImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 股票最新交易日交易数据
 *
 * @author lusongsong
 * @date 2021/1/26 16:53
 */
public class StockSpiderRealtimeDealInfo extends StockSpiderBase {
    /**
     * 对应接口
     */
    private static Class<StockSpiderRealtimeDealInfoApiInterface> clazz =
            StockSpiderRealtimeDealInfoApiInterface.class;

    /**
     * 对应实现类接口
     */
    public StockSpiderRealtimeDealInfo() {
        implClassList = Arrays.asList(
                Arrays.asList(NetsSpiderRealtimeDealInfoApiImpl.class, false),
                Arrays.asList(SinaSpiderRealtimeDealInfoApiImpl.class, false),
                Arrays.asList(TencentSpiderRealtimeDealInfoApiImpl.class, true)
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
