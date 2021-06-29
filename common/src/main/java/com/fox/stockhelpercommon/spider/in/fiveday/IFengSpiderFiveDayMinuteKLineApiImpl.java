package com.fox.stockhelpercommon.spider.in.fiveday;

import com.fox.spider.stock.api.ifeng.IFengFiveDayMinuteKLineApi;
import com.fox.spider.stock.entity.po.ifeng.IFengFiveDayMinuteKLinePo;
import com.fox.spider.stock.entity.po.ifeng.IFengFiveDayMinuteNodeDataPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLineNodePo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.in.StockSpiderFiveDayMinuteKLineApiInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 凤凰网股票近5个交易日分钟线图数据接口
 *
 * @author lusongsong
 * @date 2021/5/18 14:16
 */
public class IFengSpiderFiveDayMinuteKLineApiImpl implements StockSpiderFiveDayMinuteKLineApiInterface {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 凤凰网股票近5个交易日分钟线图数据接口
     */
    IFengFiveDayMinuteKLineApi iFengFiveDayMinuteKLineApi =
            new IFengFiveDayMinuteKLineApi();

    /**
     * 股票近5个交易日的分钟粒度数据
     *
     * @param stockVo
     * @return
     */
    @Override
    public List<StockMinuteKLinePo> fiveDayMinuteKLine(StockVo stockVo) {
        try {
            return convertObj(iFengFiveDayMinuteKLineApi.fiveDayKLine(stockVo));
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
        return IFengFiveDayMinuteKLineApi.isSupport(stockMarket);
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
     * @param iFengFiveDayMinuteKLinePoList
     * @return
     */
    private List<StockMinuteKLinePo> convertObj(
            List<IFengFiveDayMinuteKLinePo> iFengFiveDayMinuteKLinePoList
    ) {
        if (null == iFengFiveDayMinuteKLinePoList || iFengFiveDayMinuteKLinePoList.isEmpty()) {
            return null;
        }
        try {
            List<StockMinuteKLinePo> stockMinuteKLinePoList =
                    new ArrayList<>(iFengFiveDayMinuteKLinePoList.size());
            for (
                    IFengFiveDayMinuteKLinePo iFengFiveDayMinuteKLinePo
                    : iFengFiveDayMinuteKLinePoList
            ) {
                StockMinuteKLinePo stockMinuteKLinePo = new StockMinuteKLinePo();
                stockMinuteKLinePo.setStockMarket(iFengFiveDayMinuteKLinePo.getStockMarket());
                stockMinuteKLinePo.setStockCode(iFengFiveDayMinuteKLinePo.getStockCode());
                stockMinuteKLinePo.setPreClosePrice(iFengFiveDayMinuteKLinePo.getPreClosePrice());
                stockMinuteKLinePo.setDt(iFengFiveDayMinuteKLinePo.getDt());

                List<IFengFiveDayMinuteNodeDataPo> iFengFiveDayMinuteNodeDataPoList =
                        iFengFiveDayMinuteKLinePo.getKlineData();
                if (null != iFengFiveDayMinuteNodeDataPoList
                        && !iFengFiveDayMinuteNodeDataPoList.isEmpty()) {
                    List<StockMinuteKLineNodePo> stockRealtimeMinuteNodeDataPoList =
                            new ArrayList<>(iFengFiveDayMinuteNodeDataPoList.size());
                    for (
                            IFengFiveDayMinuteNodeDataPo iFengFiveDayMinuteNodeDataPo
                            : iFengFiveDayMinuteNodeDataPoList) {
                        if (null == iFengFiveDayMinuteNodeDataPo) {
                            continue;
                        }
                        StockMinuteKLineNodePo stockRealtimeMinuteNodeDataPo = new StockMinuteKLineNodePo();
                        stockRealtimeMinuteNodeDataPo.setTime(iFengFiveDayMinuteNodeDataPo.getTime());
                        stockRealtimeMinuteNodeDataPo.setPrice(iFengFiveDayMinuteNodeDataPo.getPrice());
                        stockRealtimeMinuteNodeDataPo.setDealNum(iFengFiveDayMinuteNodeDataPo.getDealMoney().divide(iFengFiveDayMinuteNodeDataPo.getAvgPrice(), 0, RoundingMode.HALF_UP).longValue());
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
