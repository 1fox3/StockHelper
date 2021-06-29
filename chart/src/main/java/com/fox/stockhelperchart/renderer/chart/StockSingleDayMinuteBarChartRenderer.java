package com.fox.stockhelperchart.renderer.chart;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockSingleDayMinuteBarChartRenderer extends StockMinuteBarChartRenderer {
    public StockSingleDayMinuteBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }
}
