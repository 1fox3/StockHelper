package com.fox.stockhelpercommon.spider.out;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockKLinePo;
import com.fox.stockhelpercommon.spider.in.StockSpiderKLineApiInterface;
import com.fox.stockhelpercommon.spider.in.kline.TencentKLineApiImpl;

import java.util.Arrays;

/**
 * 股票K线图接口
 *
 * @author lusongsong
 * @date 2021/5/26 15:02
 */
public class StockSpiderKLineApi extends StockSpiderBaseApi {
    /**
     * 对应接口
     */
    private static Class<StockSpiderKLineApiInterface> clazz =
            StockSpiderKLineApiInterface.class;

    /**
     * 注册接口实现类
     */
    public StockSpiderKLineApi() {
        implClassList = Arrays.asList(
                Arrays.asList(TencentKLineApiImpl.class, true)
        );
    }

    /**
     * 股票K线图数据
     *
     * @param stockVo
     * @return
     */
    public StockKLinePo kLine(StockVo stockVo, int dateType, int fqType, String startDate, String endDate) {
        demoStockVo = stockVo;
        return getBean(clazz).kLine(stockVo, dateType, fqType, startDate, endDate);
    }
}
