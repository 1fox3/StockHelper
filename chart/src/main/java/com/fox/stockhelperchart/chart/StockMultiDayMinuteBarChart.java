package com.fox.stockhelperchart.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.fox.stockhelperchart.R;
import com.fox.stockhelperchart.formatter.StockXAxisFormatter;
import com.fox.stockhelperchart.markerview.StockMarkerView;
import com.fox.stockhelperchart.renderer.chart.StockMultiDayMinuteBarChartRenderer;
import com.fox.stockhelperchart.renderer.xaxis.StockMultiDayMinuteLineXAxisRenderer;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

/**
 * @author lusongsong
 * @date 2021/3/26 15:09
 */
public class StockMultiDayMinuteBarChart extends StockMinuteBarChart {
    public StockMultiDayMinuteBarChart(Context context) {
        super(context);
        initSelf();
    }

    public StockMultiDayMinuteBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelf();
    }

    public StockMultiDayMinuteBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSelf();
    }

    /**
     * 初始化
     */
    protected void initSelf() {
        initChart();
        initXAxis();
        initLeftYAxis();
        initRightYAxis();
    }

    /**
     * 初始化图表
     */
    private void initChart() {
        //不显示线图描述文案
        Description description = new Description();
        description.setEnabled(false);
        setDescription(description);
        //不显示数据集合名称
        getLegend().setEnabled(false);
        //设置渲染器
        setRenderer(
                new StockMultiDayMinuteBarChartRenderer(
                        this,
                        getAnimator(),
                        getViewPortHandler()
                )
        );
        //设置提示
        StockMarkerView stockMarkerView =
                new StockMarkerView(getContext(), R.layout.markerview_str);
        stockMarkerView.setChartView(this);
        setMarker(stockMarkerView);
    }

    /**
     * 初始化X轴
     */
    private void initXAxis() {
        XAxis xAxis = getXAxis();
        //不显示刻度值
        xAxis.setDrawLabels(false);
        //设置位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴渲染器
        StockMultiDayMinuteLineXAxisRenderer stockMultiDayMinuteLineXAxisRenderer = new StockMultiDayMinuteLineXAxisRenderer(
                getViewPortHandler(),
                xAxis,
                getTransformer(YAxis.AxisDependency.LEFT)
        );
        setXAxisRenderer(stockMultiDayMinuteLineXAxisRenderer);
        //设置X轴Label格式器
        StockXAxisFormatter stockXAxisFormatter = new StockXAxisFormatter();
        xAxis.setValueFormatter(stockXAxisFormatter);
    }

    /**
     * 初始化左Y轴
     */
    private void initLeftYAxis() {
        YAxis leftYAxis = getAxisLeft();
        //显示刻度值
        leftYAxis.setDrawLabels(true);
        //刻度显示再里边
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
    }

    /**
     * 初始化右Y轴
     */
    private void initRightYAxis() {
        YAxis rightYAxis = getAxisRight();
        //不显示刻度值
        rightYAxis.setDrawLabels(false);
    }
}
