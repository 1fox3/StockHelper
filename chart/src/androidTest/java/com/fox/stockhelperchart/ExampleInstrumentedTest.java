package com.fox.stockhelperchart;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.out.StockSpiderRealtimeMinuteKLineApi;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.fox.stockhelperchart", appContext.getPackageName());
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
