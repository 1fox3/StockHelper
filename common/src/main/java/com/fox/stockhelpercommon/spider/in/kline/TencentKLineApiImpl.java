package com.fox.stockhelpercommon.spider.in.kline;

import com.fox.spider.stock.api.tencent.TencentKLineApi;
import com.fox.spider.stock.entity.po.tencent.TencentKLineNodeDataPo;
import com.fox.spider.stock.entity.po.tencent.TencentKLinePo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockKLineNodePo;
import com.fox.stockhelpercommon.entity.stock.po.StockKLinePo;
import com.fox.stockhelpercommon.spider.in.StockSpiderKLineApiInterface;
import com.fox.stockhelpercommon.util.SelfBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 腾讯k线图接口
 *
 * @author lusongsong
 * @date 2021/5/26 16:21
 */
public class TencentKLineApiImpl implements StockSpiderKLineApiInterface {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 腾讯k线接口
     */
    TencentKLineApi tencentKLineApi = new TencentKLineApi();

    /**
     * K线图接口
     *
     * @param stockVo
     * @param dateType
     * @param fqType
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public StockKLinePo kLine(StockVo stockVo, int dateType, int fqType, String startDate, String endDate) {
        try {
            return convertObj(tencentKLineApi.kLine(stockVo, dateType, fqType, startDate, endDate));
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
        return TencentKLineApi.isSupport(stockMarket);
    }

    /**
     * 权重
     *
     * @return
     */
    @Override
    public int weight() {
        return 0;
    }

    /**
     * 对象转换
     *
     * @param tencentKLinePo
     * @return
     */
    private StockKLinePo convertObj(TencentKLinePo tencentKLinePo) {
        if (tencentKLinePo == null) {
            return null;
        }
        try {
            StockKLinePo stockKLinePo = new StockKLinePo();
            SelfBeanUtil.copyPropertiesExclude(
                    tencentKLinePo,
                    stockKLinePo,
                    new String[]{"klineData"}
            );
            List<StockKLineNodePo> stockKLineNodePoList = new ArrayList<>();
            for (TencentKLineNodeDataPo tencentKLineNodeDataPo : tencentKLinePo.getKlineData()) {
                StockKLineNodePo stockKLineNodePo = new StockKLineNodePo();
                SelfBeanUtil.copyProperties(
                        tencentKLineNodeDataPo,
                        stockKLineNodePo
                );
                stockKLineNodePoList.add(stockKLineNodePo);
            }
            stockKLinePo.setKlineData(stockKLineNodePoList);
            return stockKLinePo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
