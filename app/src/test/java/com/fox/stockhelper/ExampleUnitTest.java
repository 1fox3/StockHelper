package com.fox.stockhelper;

import com.fox.stockhelper.api.stock.realtime.DealPriceLineApi;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        DealPriceLineApi dealInfoApi = new DealPriceLineApi();
        Map<String, Object> params = new HashMap<>();
        params.put("stockId", 52070);
        dealInfoApi.setParams(params);

        System.out.println(dealInfoApi.request());
    }
}