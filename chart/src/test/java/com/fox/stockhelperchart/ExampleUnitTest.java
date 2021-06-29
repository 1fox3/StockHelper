package com.fox.stockhelperchart;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.out.StockSpiderRealtimeMinuteKLineApi;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void spiderTest() {
        StockSpiderRealtimeMinuteKLineApi stockSpiderRealtimeMinuteKLineApi =
                new StockSpiderRealtimeMinuteKLineApi();
        StockMinuteKLinePo stockMinuteKLinePo =
                stockSpiderRealtimeMinuteKLineApi
                        .realtimeMinuteKLine(new StockVo("603383", StockConst.SM_A));
        System.out.println(stockMinuteKLinePo);
    }
}