package com.fox.stockhelpercommon.spider.out;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.in.StockSpiderFiveDayMinuteKLineApiInterface;
import com.fox.stockhelpercommon.spider.in.fiveday.IFengSpiderFiveDayMinuteKLineApiImpl;
import com.fox.stockhelpercommon.spider.in.fiveday.TencentSpiderFiveDayMinuteKLineApiImpl;

import java.util.Arrays;
import java.util.List;

/**
 * 5日分钟数据
 *
 * @author lusongsong
 * @date 2021/5/18 13:57
 */
public class StockSpiderFiveDayMinuteKLineApi extends StockSpiderBaseApi {
    /**
     * 对应接口
     */
    private static Class<StockSpiderFiveDayMinuteKLineApiInterface> clazz =
            StockSpiderFiveDayMinuteKLineApiInterface.class;

    /**
     * 注册接口实现类
     */
    public StockSpiderFiveDayMinuteKLineApi() {
        implClassList = Arrays.asList(
//                Arrays.asList(IFengSpiderFiveDayMinuteKLineApiImpl.class, true)
                Arrays.asList(TencentSpiderFiveDayMinuteKLineApiImpl.class, true)
        );
    }

    /**
     * 股票最新交易日分钟线图数据
     *
     * @param stockVo
     * @return
     */
    public List<StockMinuteKLinePo> fiveDayMinuteKLine(StockVo stockVo) {
        demoStockVo = stockVo;
        return getBean(clazz).fiveDayMinuteKLine(stockVo);
    }
}
