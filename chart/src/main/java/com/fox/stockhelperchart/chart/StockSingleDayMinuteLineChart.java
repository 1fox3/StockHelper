package com.fox.stockhelperchart.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.fox.stockhelperchart.R;
import com.fox.stockhelperchart.formatter.StockPercentFormatter;
import com.fox.stockhelperchart.formatter.StockPriceFormatter;
import com.fox.stockhelperchart.formatter.StockXAxisFormatter;
import com.fox.stockhelperchart.markerview.StockMarkerView;
import com.fox.stockhelperchart.renderer.xaxis.StockSingleDayMinuteLineXAxisRenderer;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import static com.fox.stockhelperchart.BaseStockChart.NO_DATA_STR;

/**
 * 股票单天分钟粒度线图
 *
 * @author lusongsong
 * @date 2021/2/25 18:04
 */
public class StockSingleDayMinuteLineChart extends StockMinuteLineChart {
    public StockSingleDayMinuteLineChart(Context context) {
        super(context);
        initSelf();
    }

    public StockSingleDayMinuteLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelf();
    }

    public StockSingleDayMinuteLineChart(Context context, AttributeSet attrs, int defStyle) {
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
        //画外框线
        setDrawBorders(true);
        //设置边框颜色
        setBorderColor(getContext().getColor(R.color.chartBorder));
        //设置无数据时的显示文案
        setNoDataText(NO_DATA_STR);
        //不显示线图描述文案
        Description description = new Description();
        description.setEnabled(false);
        setDescription(description);
        //不显示数据集合名称
        getLegend().setEnabled(false);
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
        //X轴显示在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //X轴不显示坐标
        xAxis.setDrawLabels(true);
        //X轴显示网格线
        xAxis.setDrawGridLines(true);
        //设置X轴渲染器
        StockSingleDayMinuteLineXAxisRenderer stockSingleDayMinuteLineXAxisRenderer = new StockSingleDayMinuteLineXAxisRenderer(
                getViewPortHandler(),
                xAxis,
                getTransformer(YAxis.AxisDependency.LEFT)
        );
        setXAxisRenderer(stockSingleDayMinuteLineXAxisRenderer);
        //设置X轴Label格式器
        xAxis.setValueFormatter(new StockXAxisFormatter());
    }

    /**
     * 初始化左Y轴
     */
    private void initLeftYAxis() {
        YAxis leftYAxis = getAxisLeft();
        //左Y轴显示在图标内部
        leftYAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //Y轴不显示网格线
        leftYAxis.setDrawGridLines(true);
        //设置右Y轴数值格式器
        leftYAxis.setValueFormatter(
                new StockPriceFormatter()
                        .setNumberFormatter(false)
                        .initFormatter()
        );
    }

    /**
     * 初始化右Y轴
     */
    private void initRightYAxis() {
        YAxis rightYAxis = getAxisRight();
        //右Y轴显示在图标内部
        rightYAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //Y轴不显示网格线
        rightYAxis.setDrawGridLines(false);
        //设置右Y轴数值格式器
        rightYAxis.setValueFormatter(
                new StockPercentFormatter()
                        .setNumberFormatter(false)
                        .initFormatter()
        );
    }
}
