package com.fox.stockhelper;

import com.fox.stockhelper.util.DateUtil;

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
        int minute = Integer.valueOf(DateUtil.getCurrentDate(DateUtil.MINUTE_FORMAT_1));
        int second = Integer.valueOf(DateUtil.getCurrentDate(DateUtil.SECOND_FORMAT_1));
        int s = 300 - second - (minute % 5) * 60;
        System.out.println(String.valueOf(minute));
        System.out.println(String.valueOf(second));
        System.out.println(String.valueOf(s));
    }
}