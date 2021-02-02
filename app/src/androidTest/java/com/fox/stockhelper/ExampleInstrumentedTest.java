package com.fox.stockhelper;

import android.content.Context;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.api.login.SendCodeApi;
import com.fox.stockhelper.spider.out.StockSpiderBase;
import com.fox.stockhelper.spider.out.StockSpiderRealtimeMinuteKLine;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

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

        assertEquals("com.fox.stockhelper", appContext.getPackageName());
    }

    @Test
    public void apiTest() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", "lusongsong@jd.com");
        SendCodeApi sendCodeApi = new SendCodeApi();
        sendCodeApi.setParams(params);
        Object object = sendCodeApi.request();
    }

    @Test
    public void spiderApiTest() {
        StockVo stockVo = new StockVo("603383", StockConst.SM_SH);
        StockSpiderRealtimeMinuteKLine stockSpiderRealtimeMinuteKLine =
                new StockSpiderRealtimeMinuteKLine();
        stockSpiderRealtimeMinuteKLine.setChooseMethod(StockSpiderBase.CHOOSE_METHOD_POLL);
//        StockSpiderRealtimeDealInfo stockSpiderRealtimeDealInfo = new StockSpiderRealtimeDealInfo();
//        stockSpiderRealtimeDealInfo.setChooseMethod(StockSpiderBase.CHOOSE_METHOD_POLL);
        for (int i = 0; i < 10; i++) {
            System.out.println(stockSpiderRealtimeMinuteKLine.realtimeMinuteKLine(stockVo));
//            System.out.println(stockSpiderRealtimeDealInfo.realtimeDealInfo(stockVo));
        }
    }
}
