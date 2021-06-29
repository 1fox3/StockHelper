package com.fox.stockhelperchart.activity;

import android.os.Bundle;

import com.fox.stockhelperchart.R;
import com.fox.stockhelperchart.StockMultiDayMinuteChart;

/**
 * 对天分钟线图
 *
 * @author lusongsong
 * @date 2021/2/25 16:41
 */
public class MultiDayMinuteChartActivity extends StockChartBaseActivity {
    StockMultiDayMinuteChart stockMultiDayMinuteChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_kline);
        stockMultiDayMinuteChart = findViewById(R.id.stockMultiDayMinuteChart);
        stockMultiDayMinuteChart.initChart(SH_TEST_STOCK);
    }
}
