package com.fox.stockhelpercommon.spider.in.fiveday;

import com.fox.spider.stock.api.tencent.TencentFiveDayMinuteKLineApi;
import com.fox.spider.stock.entity.po.tencent.TencentDayMinKLinePo;
import com.fox.spider.stock.entity.po.tencent.TencentFiveDayMinuteKLinePo;
import com.fox.spider.stock.entity.po.tencent.TencentRealtimeMinuteNodeDataPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLineNodePo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.in.StockSpiderFiveDayMinuteKLineApiInterface;
import com.fox.stockhelpercommon.util.SelfBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 腾讯股票近5个交易日分钟线图数据接口
 *
 * @author lusongsong
 * @date 2021/5/18 14:10
 */
public class TencentSpiderFiveDayMinuteKLineApiImpl implements StockSpiderFiveDayMinuteKLineApiInterface {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 腾讯股票近5个交易日分钟线图数据接口
     */
    private TencentFiveDayMinuteKLineApi tencentFiveDayMinuteKLineApi =
            new TencentFiveDayMinuteKLineApi();

    /**
     * 股票近5个交易日的分钟粒度数据
     *
     * @param stockVo
     * @return
     */
    @Override
    public List<StockMinuteKLinePo> fiveDayMinuteKLine(StockVo stockVo) {
        try {
            return convertObj(tencentFiveDayMinuteKLineApi.fiveDayKLine(stockVo));
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
        return TencentFiveDayMinuteKLineApi.isSupport(stockMarket);
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
     * @param tencentFiveDayMinuteKLinePo
     * @return
     */
    private List<StockMinuteKLinePo> convertObj(TencentFiveDayMinuteKLinePo tencentFiveDayMinuteKLinePo) {
        if (null == tencentFiveDayMinuteKLinePo
                || !(tencentFiveDayMinuteKLinePo instanceof TencentFiveDayMinuteKLinePo)) {
            return null;
        }
        try {
            List<TencentDayMinKLinePo> tencentDayMinKLinePoList =
                    tencentFiveDayMinuteKLinePo.getKlineData();
            if (null == tencentDayMinKLinePoList || tencentDayMinKLinePoList.isEmpty()) {
                return null;
            }
            Integer stockMarket = tencentFiveDayMinuteKLinePo.getStockMarket();
            String stockCode = tencentFiveDayMinuteKLinePo.getStockCode();
            List<StockMinuteKLinePo> stockMinuteKLinePoList =
                    new ArrayList<>(tencentDayMinKLinePoList.size());
            for (
                    TencentDayMinKLinePo tencentDayMinKLinePo
                    : tencentDayMinKLinePoList
            ) {
                StockMinuteKLinePo stockMinuteKLinePo = new StockMinuteKLinePo();
                stockMinuteKLinePo.setStockMarket(stockMarket);
                stockMinuteKLinePo.setStockCode(stockCode);
                stockMinuteKLinePo.setPreClosePrice(tencentDayMinKLinePo.getPreClosePrice());
                stockMinuteKLinePo.setDt(tencentDayMinKLinePo.getDt());

                List<TencentRealtimeMinuteNodeDataPo> tencentRealtimeMinuteNodeDataPoList =
                        tencentDayMinKLinePo.getKlineData();
                if (null != tencentRealtimeMinuteNodeDataPoList
                        && !tencentRealtimeMinuteNodeDataPoList.isEmpty()) {
                    List<StockMinuteKLineNodePo> stockRealtimeMinuteNodeDataPoList =
                            new ArrayList<>(tencentRealtimeMinuteNodeDataPoList.size());
                    for (
                            TencentRealtimeMinuteNodeDataPo tencentRealtimeMinuteNodeDataPo
                            : tencentRealtimeMinuteNodeDataPoList) {
                        if (null == tencentRealtimeMinuteNodeDataPo) {
                            continue;
                        }
                        StockMinuteKLineNodePo stockRealtimeMinuteNodeDataPo = new StockMinuteKLineNodePo();
                        SelfBeanUtil.copyProperties(tencentRealtimeMinuteNodeDataPo, stockRealtimeMinuteNodeDataPo);
                        stockRealtimeMinuteNodeDataPoList.add(stockRealtimeMinuteNodeDataPo);
                    }
                    stockMinuteKLinePo.setNodeCount(stockRealtimeMinuteNodeDataPoList.size());
                    stockMinuteKLinePo.setKlineData(stockRealtimeMinuteNodeDataPoList);
                }
                stockMinuteKLinePoList.add(stockMinuteKLinePo);
            }
            return stockMinuteKLinePoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
