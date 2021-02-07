package com.fox.stockhelper;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.api.login.SendCodeApi;
import com.fox.stockhelper.spider.out.StockSpiderBaseApi;
import com.fox.stockhelper.spider.out.StockSpiderRealtimeMinuteKLineApi;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
        StockSpiderRealtimeMinuteKLineApi stockSpiderRealtimeMinuteKLine =
                new StockSpiderRealtimeMinuteKLineApi();
        stockSpiderRealtimeMinuteKLine.setChooseMethod(StockSpiderBaseApi.CHOOSE_METHOD_POLL);
//        StockSpiderRealtimeDealInfo stockSpiderRealtimeDealInfo = new StockSpiderRealtimeDealInfo();
//        stockSpiderRealtimeDealInfo.setChooseMethod(StockSpiderBase.CHOOSE_METHOD_POLL);
        for (int i = 0; i < 10; i++) {
            System.out.println(stockSpiderRealtimeMinuteKLine.realtimeMinuteKLine(stockVo));
//            System.out.println(stockSpiderRealtimeDealInfo.realtimeDealInfo(stockVo));
        }
    }

    private void getStrShowLen(String str) {
        Paint paint= new Paint();
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int strwidth = rect.width();
        int strheight = rect.height();
        float strwith = paint.measureText(str);
        System.out.println(str + ":" + strwidth + ":" + strheight + ":" + strwith);
    }
    @Test
    public void stockValueUtilTest() {
        TreeMap<BigDecimal, Long> priceMap = new TreeMap<>();
        priceMap.put(new BigDecimal("1"), 1121l);
        priceMap.put(new BigDecimal("4"), 112l);
        priceMap.put(new BigDecimal("2"), 1l);
        System.out.println(priceMap);
    }
}
