package com.fox.stockhelpercommon;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockKLinePo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.out.StockSpiderFiveDayMinuteKLineApi;
import com.fox.stockhelpercommon.spider.out.StockSpiderKLineApi;
import com.fox.stockhelpercommon.spider.out.StockSpiderRealtimeMinuteKLineApi;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static final StockVo SH_TEST_STOCK = new StockVo("603383", StockConst.SM_A);

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void spiderRealtimeMinuteKLineApiTest() {
        StockSpiderRealtimeMinuteKLineApi stockSpiderRealtimeMinuteKLineApi =
                new StockSpiderRealtimeMinuteKLineApi();
        StockMinuteKLinePo stockMinuteKLinePo =
                stockSpiderRealtimeMinuteKLineApi
                        .realtimeMinuteKLine(SH_TEST_STOCK);
        System.out.println(stockMinuteKLinePo);
    }

    @Test
    public void spiderFiveDayMinuteKLineApiTest() {
        StockSpiderFiveDayMinuteKLineApi stockSpiderFiveDayMinuteKLineApi =
                new StockSpiderFiveDayMinuteKLineApi();
        List<StockMinuteKLinePo> stockMinuteKLinePoList =
                stockSpiderFiveDayMinuteKLineApi
                        .fiveDayMinuteKLine(SH_TEST_STOCK);
        System.out.println(stockMinuteKLinePoList);
    }

    @Test
    public void spiderKLineApiTest() {
        StockSpiderKLineApi stockSpiderKLineApi = new StockSpiderKLineApi();
        StockKLinePo stockKLinePo = stockSpiderKLineApi.kLine(
                SH_TEST_STOCK,
                StockConst.DT_DAY,
                StockConst.SFQ_AFTER,
                "2021-05-20",
                "2021-05-25"
        );
        System.out.println(stockKLinePo);
    }
}