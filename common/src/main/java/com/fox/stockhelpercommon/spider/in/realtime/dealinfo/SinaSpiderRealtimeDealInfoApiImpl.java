package com.fox.stockhelpercommon.spider.in.realtime.dealinfo;

import com.fox.spider.stock.api.sina.SinaRealtimeDealInfoApi;
import com.fox.spider.stock.entity.po.sina.SinaRealtimeDealInfoPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockRealtimeDealInfoPo;
import com.fox.stockhelpercommon.spider.in.StockSpiderRealtimeDealInfoApiInterface;
import com.fox.stockhelpercommon.util.SelfBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新浪股票最新交易日交易信息接口对接
 *
 * @author lusongsong
 * @date 2021/1/26 16:36
 */
public class SinaSpiderRealtimeDealInfoApiImpl implements StockSpiderRealtimeDealInfoApiInterface {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 新浪股票最新交易日交易信息接口
     */
    SinaRealtimeDealInfoApi sinaRealtimeDealInfoApi = new SinaRealtimeDealInfoApi();

    /**
     * 获取单只股票的实时交易信息
     *
     * @param stockVo
     * @return
     */
    @Override
    public StockRealtimeDealInfoPo realtimeDealInfo(StockVo stockVo) {
        try {
            System.out.println("sina");
            return convertObj(sinaRealtimeDealInfoApi.realtimeDealInfo(stockVo));
        } catch (Exception e) {
            logger.error(stockVo.toString(), e);
        }
        return null;
    }

    /**
     * 批量获取股票的实时交易信息
     *
     * @param stockVoList
     * @return
     */
    @Override
    public Map<String, StockRealtimeDealInfoPo> batchRealtimeDealInfo(List<StockVo> stockVoList) {
        try {
            Map<String, SinaRealtimeDealInfoPo> sinaBatchResult = sinaRealtimeDealInfoApi.batchRealtimeDealInfo(stockVoList);
            if (null != sinaBatchResult && !sinaBatchResult.isEmpty()) {
                Map<String, StockRealtimeDealInfoPo> batchResult = new HashMap<>(sinaBatchResult.size());
                for (String stockCode : sinaBatchResult.keySet()) {
                    batchResult.put(stockCode, convertObj(sinaBatchResult.get(stockCode)));
                }
                return batchResult;
            }
        } catch (Exception e) {
            logger.error(stockVoList.toString(), e);
        }
        return null;
    }

    /**
     * 是否支持该证券所
     *
     * @param stockMarket
     * @return
     */
    @Override
    public boolean isSupport(int stockMarket) {
        return SinaRealtimeDealInfoApi.isSupport(stockMarket);
    }

    /**
     * 权重
     *
     * @return
     */
    @Override
    public int weight() {
        return 1;
    }

    /**
     * 对象转换
     *
     * @param sinaRealtimeDealInfoPo
     * @return
     */
    private StockRealtimeDealInfoPo convertObj(SinaRealtimeDealInfoPo sinaRealtimeDealInfoPo) {
        if (sinaRealtimeDealInfoPo instanceof SinaRealtimeDealInfoPo) {
            StockRealtimeDealInfoPo stockRealtimeDealInfoPo = new StockRealtimeDealInfoPo();
            try {
                SelfBeanUtil.copyProperties(sinaRealtimeDealInfoPo, stockRealtimeDealInfoPo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stockRealtimeDealInfoPo;
        }
        return null;
    }
}
