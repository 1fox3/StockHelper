package com.fox.stockhelperchart.activity;

import android.os.Bundle;

import com.fox.stockhelperchart.R;
import com.fox.stockhelperchart.StockSingleDayMinuteChart;

/**
 * 分钟粒度K线图界面
 *
 * @author lusongsong
 * @date 2021/2/25 16:40
 */
public class SingleDayMinuteChartActivity extends StockChartBaseActivity {
    StockSingleDayMinuteChart stockSingleDayMinuteChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minute_kline);
        stockSingleDayMinuteChart = findViewById(R.id.stockSingleDayMinuteChart);
        stockSingleDayMinuteChart.initChart(SH_TEST_STOCK);
    }
}
