package com.fox.stockhelper.spider.in.realtime.minutekline;

import com.fox.spider.stock.api.nets.NetsRealtimeMinuteKLineApi;
import com.fox.spider.stock.entity.po.nets.NetsRealtimeMinuteKLinePo;
import com.fox.spider.stock.entity.po.nets.NetsRealtimeMinuteNodeDataPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.entity.po.stock.StockRealtimeMinuteKLinePo;
import com.fox.stockhelper.entity.po.stock.StockRealtimeMinuteNodeDataPo;
import com.fox.stockhelper.spider.in.StockSpiderRealtimeMinuteKLineApiInterface;
import com.fox.stockhelper.util.SelfBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 网易股票最新交易日分钟线图数据接口
 *
 * @author lusongsong
 * @date 2021/2/1 15:37
 */
public class NetsSpiderRealtimeMinuteKLineApiImpl
        implements StockSpiderRealtimeMinuteKLineApiInterface {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 网易股票最新交易日分钟线图数据接口
     */
    NetsRealtimeMinuteKLineApi netsRealtimeMinuteKLineApi = new NetsRealtimeMinuteKLineApi();

    /**
     * 股票最新交易日分钟线图数据
     *
     * @param stockVo
     * @return
     */
    @Override
    public StockRealtimeMinuteKLinePo realtimeMinuteKLine(StockVo stockVo) {
        try {
            return convertObj(netsRealtimeMinuteKLineApi.realtimeMinuteKLine(stockVo));
        } catch (Exception e) {
            logger.error(stockVo.toString(), e);
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
        return NetsRealtimeMinuteKLineApi.isSupport(stockMarket);
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
     * @param netsRealtimeMinuteKLinePo
     * @return
     */
    private StockRealtimeMinuteKLinePo convertObj(
            NetsRealtimeMinuteKLinePo netsRealtimeMinuteKLinePo) {
        if (null == netsRealtimeMinuteKLinePo
                || !(netsRealtimeMinuteKLinePo instanceof NetsRealtimeMinuteKLinePo)) {
            return null;
        }
        try {
            StockRealtimeMinuteKLinePo stockRealtimeMinuteKLinePo =
                    new StockRealtimeMinuteKLinePo();
            SelfBeanUtil.copyPropertiesExclude(
                    netsRealtimeMinuteKLinePo,
                    stockRealtimeMinuteKLinePo,
                    new String[]{"klineData"}
            );
            List<NetsRealtimeMinuteNodeDataPo> netsRealtimeMinuteNodeDataPoList
                    = netsRealtimeMinuteKLinePo.getKlineData();
            if (null != netsRealtimeMinuteNodeDataPoList
                    && !netsRealtimeMinuteNodeDataPoList.isEmpty()) {
                List<StockRealtimeMinuteNodeDataPo> stockRealtimeMinuteNodeDataPoList =
                        new ArrayList<>(netsRealtimeMinuteNodeDataPoList.size());
                for (NetsRealtimeMinuteNodeDataPo netsRealtimeMinuteNodeDataPo
                        : netsRealtimeMinuteNodeDataPoList) {
                    if (null == netsRealtimeMinuteNodeDataPo) {
                        continue;
                    }
                    StockRealtimeMinuteNodeDataPo stockRealtimeMinuteNodeDataPo =
                            new StockRealtimeMinuteNodeDataPo();
                    SelfBeanUtil.copyProperties(
                            netsRealtimeMinuteNodeDataPo,
                            stockRealtimeMinuteNodeDataPo
                    );
                    stockRealtimeMinuteNodeDataPoList.add(stockRealtimeMinuteNodeDataPo);
                }
                stockRealtimeMinuteKLinePo.setNodeCount(stockRealtimeMinuteNodeDataPoList.size());
                stockRealtimeMinuteKLinePo.setKlineData(stockRealtimeMinuteNodeDataPoList);
            }
            return stockRealtimeMinuteKLinePo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
