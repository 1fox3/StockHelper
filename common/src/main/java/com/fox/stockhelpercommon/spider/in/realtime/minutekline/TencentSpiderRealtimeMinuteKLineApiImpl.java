package com.fox.stockhelpercommon.spider.in.realtime.minutekline;

import com.fox.spider.stock.api.tencent.TencentRealtimeMinuteKLineApi;
import com.fox.spider.stock.entity.po.tencent.TencentRealtimeMinuteKLinePo;
import com.fox.spider.stock.entity.po.tencent.TencentRealtimeMinuteNodeDataPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLineNodePo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.in.StockSpiderRealtimeMinuteKLineApiInterface;
import com.fox.stockhelpercommon.util.SelfBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 腾讯股票最新交易日分钟线图数据接口对接
 *
 * @author lusongsong
 * @date 2021/2/1 15:47
 */
public class TencentSpiderRealtimeMinuteKLineApiImpl
        implements StockSpiderRealtimeMinuteKLineApiInterface {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 腾讯股票近5个交易日分钟线图数据接口
     */
    TencentRealtimeMinuteKLineApi tencentRealtimeMinuteKLineApi =
            new TencentRealtimeMinuteKLineApi();

    /**
     * 股票最新交易日分钟线图数据
     *
     * @param stockVo
     * @return
     */
    @Override
    public StockMinuteKLinePo realtimeMinuteKLine(StockVo stockVo) {
        try {
            return convertObj(tencentRealtimeMinuteKLineApi.realtimeMinuteKLine(stockVo));
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
        return TencentRealtimeMinuteKLineApi.isSupport(stockMarket);
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
     * @param tencentRealtimeMinuteKLinePo
     * @return
     */
    private StockMinuteKLinePo convertObj(TencentRealtimeMinuteKLinePo tencentRealtimeMinuteKLinePo) {
        if (null == tencentRealtimeMinuteKLinePo
                || !(tencentRealtimeMinuteKLinePo instanceof TencentRealtimeMinuteKLinePo)) {
            return null;
        }
        try {
            StockMinuteKLinePo stockRealtimeMinuteKLinePo = new StockMinuteKLinePo();
            SelfBeanUtil.copyPropertiesExclude(
                    tencentRealtimeMinuteKLinePo,
                    stockRealtimeMinuteKLinePo,
                    new String[]{"klineData"}
            );
            List<TencentRealtimeMinuteNodeDataPo> tencentRealtimeMinuteNodeDataPoList =
                    tencentRealtimeMinuteKLinePo.getKlineData();
            if (null != tencentRealtimeMinuteNodeDataPoList && !tencentRealtimeMinuteNodeDataPoList.isEmpty()) {
                List<StockMinuteKLineNodePo> stockRealtimeMinuteNodeDataPoList =
                        new ArrayList<>(tencentRealtimeMinuteNodeDataPoList.size());
                for (TencentRealtimeMinuteNodeDataPo tencentRealtimeMinuteNodeDataPo : tencentRealtimeMinuteNodeDataPoList) {
                    if (null == tencentRealtimeMinuteNodeDataPo) {
                        continue;
                    }
                    StockMinuteKLineNodePo stockRealtimeMinuteNodeDataPo = new StockMinuteKLineNodePo();
                    SelfBeanUtil.copyProperties(tencentRealtimeMinuteNodeDataPo, stockRealtimeMinuteNodeDataPo);
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
