package com.fox.stockhelperchart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelperchart.chart.StockSingleDayMinuteBarChart;
import com.fox.stockhelperchart.chart.StockSingleDayMinuteLineChart;
import com.fox.stockhelperchart.formatter.StockPriceFormatter;
import com.fox.stockhelperchart.markerview.StockMarkerView;
import com.fox.stockhelperchart.renderer.yaxis.StockSingleDayMinuteBarYAxisRenderer;
import com.fox.stockhelperchart.renderer.yaxis.StockSingleDayMinuteLineYAxisRenderer;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLineNodePo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.out.StockSpiderRealtimeMinuteKLineApi;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.SneakyThrows;

/**
 * 股票分钟力度数据
 *
 * @author lusongsong
 * @date 2021/2/26 15:54
 */
public class StockSingleDayMinuteChart extends BaseStockChart {
    /**
     * 分钟线图数据
     */
    StockSingleDayMinuteLineChart lineChart;

    /**
     * 分钟柱图数据
     */
    StockSingleDayMinuteBarChart barChart;

    StockSpiderRealtimeMinuteKLineApi stockSpiderRealtimeMinuteKLineApi =
            new StockSpiderRealtimeMinuteKLineApi();
    StockMinuteKLinePo stockMinuteKLinePo;

    public StockSingleDayMinuteChart(Context context) {
        super(context);
    }

    public StockSingleDayMinuteChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StockSingleDayMinuteChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StockSingleDayMinuteChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化图表
     */
    public void initChart(StockVo stock) {
        //父类的初始化图表
        super.initChart();
        //设置股票
        setStockVo(stock);
        //绑定视图
        bindLayout();
        //设置数据选择监听器
        setValueSelectedListener();
        //初始化线图
        initLineChart();
        //初始化柱图
        initBarChart();
        //刷新数据
        freshData();
    }

    /**
     * 绑定布局文件
     */
    private void bindLayout() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.stock_single_day_minute_chart, this, true
        );
        lineChart = findViewById(R.id.stockSingleDayMinuteLineChart);
        barChart = findViewById(R.id.stockSingleDayMinuteBarChart);
    }

    /**
     * 初始化线图
     */
    private void initLineChart() {
        //初始化线图X轴
        initLineXAxis();
        //初始化线图左Y轴
        initLineLeftYAxis();
        //初始化线图右Y轴
        initLineRightYAxis();
    }

    /**
     * 初始化线图X轴
     */
    private void initLineXAxis() {
        lineX = lineChart.getXAxis();
        //X轴设置最大的显示点数
        lineX.setAxisMaximum(X_NODE_COUNT);
        //设置默认值显示的刻度数量
        lineX.setLabelCount(X_LABEL_COUNT, true);
    }

    /**
     * 初始化线图左Y轴
     */
    private void initLineLeftYAxis() {
        lineLeftY = lineChart.getAxisLeft();
        //左Y轴显示在图标内部
        lineLeftY.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //左Y轴不显示网格线
        lineLeftY.setDrawGridLines(false);
        //设置默认值显示的刻度数量
        lineLeftY.setLabelCount(LINE_Y_LABEL_COUNT, true);
        //设置左Y轴渲染器
        StockSingleDayMinuteLineYAxisRenderer stockSingleDayMinuteLineYAxisRenderer = new StockSingleDayMinuteLineYAxisRenderer(
                lineChart.getViewPortHandler(),
                lineLeftY,
                lineChart.getTransformer(YAxis.AxisDependency.LEFT)
        );
        stockSingleDayMinuteLineYAxisRenderer.setLabelColorArr(colorArr);
        stockSingleDayMinuteLineYAxisRenderer.setFlatValue((LEFT_Y_VALUE_MAX + LEFT_Y_VALUE_MIN) / 2);
        lineChart.setRendererLeftYAxis(stockSingleDayMinuteLineYAxisRenderer);
        //设置右Y轴数值格式器
        lineLeftY.setValueFormatter(
                new StockPriceFormatter()
                        .setNumberFormatter(false)
                        .initFormatter()
        );
    }

    /**
     * 初始化线图右Y轴
     */
    private void initLineRightYAxis() {
        lineRightY = lineChart.getAxisRight();
        //画0线
        lineRightY.setDrawZeroLine(true);
        lineRightY.setZeroLineColor(zeroLineColor);
        lineRightY.setZeroLineWidth(1);
        //设置默认值显示的刻度数量
        lineRightY.setLabelCount(LINE_Y_LABEL_COUNT, true);
        //右Y轴显示网格线
        lineRightY.setDrawGridLines(true);
        //设置右Y轴渲染器
        StockSingleDayMinuteLineYAxisRenderer stockSingleDayMinuteLineYAxisRenderer =
                new StockSingleDayMinuteLineYAxisRenderer(
                        lineChart.getViewPortHandler(),
                        lineRightY,
                        lineChart.getTransformer(YAxis.AxisDependency.RIGHT)
                );
        stockSingleDayMinuteLineYAxisRenderer.setLabelColorArr(colorArr);
        stockSingleDayMinuteLineYAxisRenderer.setFlatValue(0);
        lineChart.setRendererRightYAxis(stockSingleDayMinuteLineYAxisRenderer);
    }

    /**
     * 初始化柱图
     */
    private void initBarChart() {
        //设置边框颜色
        barChart.setBorderColor(borderColor);
        //设置无数据时显示的文案
        barChart.setNoDataText(NO_DATA_STR);
        //初始化柱图X轴
        initBarXAxis();
        //初始化柱图左Y轴
        initBarLeftYAxis();
        //初始化柱图右Y轴
        initBarRightYAxis();
    }

    /**
     * 初始化柱图X轴
     */
    private void initBarXAxis() {
        barX = barChart.getXAxis();
        //设置默认值显示的刻度数量
        barX.setLabelCount(X_LABEL_COUNT, true);
        //X轴设置最大的显示点数
        barX.setAxisMaximum(X_NODE_COUNT);
    }

    /**
     * 初始化柱图左Y轴
     */
    private void initBarLeftYAxis() {
        barLeftY = barChart.getAxisLeft();
        //设置默认值显示的刻度数量
        barLeftY.setLabelCount(BAR_Y_LABEL_COUNT, true);
        //设置左Y轴渲染器
        StockSingleDayMinuteBarYAxisRenderer stockSingleDayMinuteBarYAxisRenderer =
                new StockSingleDayMinuteBarYAxisRenderer(
                        barChart.getViewPortHandler(),
                        barLeftY,
                        barChart.getTransformer(YAxis.AxisDependency.LEFT)
                );
        //设置左Y轴刻度值
        String[] labelArr = new String[BAR_Y_LABEL_COUNT];
        for (int i = 0; i < BAR_Y_LABEL_COUNT; i++) {
            if (i == 0) {
                labelArr[i] = "万手";
            } else if (i == BAR_Y_LABEL_COUNT - 1) {
                labelArr[i] = "1234";
            } else {
                labelArr[i] = "";
            }
        }
        stockSingleDayMinuteBarYAxisRenderer.setLabels(labelArr);
        barChart.setRendererLeftYAxis(stockSingleDayMinuteBarYAxisRenderer);
    }

    /**
     * 初始化柱图右Y轴
     */
    private void initBarRightYAxis() {
        barRightY = barChart.getAxisRight();
        //设置默认值显示的刻度数量
        barRightY.setLabelCount(BAR_Y_LABEL_COUNT, true);
    }

    /**
     * 设置数值选择监听器
     */
    private void setValueSelectedListener() {
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                lineChart.highlightValue(h);
                barChart.highlightValue(new Highlight(h.getX(), h.getDataSetIndex(), -1));
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValues(null);
            }
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                barChart.highlightValue(h);
                lineChart.highlightValue(new Highlight(h.getX(), h.getDataSetIndex(), -1));
            }

            @Override
            public void onNothingSelected() {
                lineChart.highlightValues(null);
            }
        });
    }

    /**
     * 设置股票分钟线图数据
     */
    public void setStockMinuteKLineData(StockMinuteKLinePo stockMinuteKLineData) {
        if (null != stockMinuteKLineData && null != stockMinuteKLineData.getKlineData()
                && !stockMinuteKLineData.getKlineData().isEmpty()) {
            int nodeLen = stockMinuteKLineData.getKlineData().size();
            String[] timeMarkerStrArr = new String[nodeLen];
            String[] barMarkerStrArr = new String[nodeLen];
            List<Entry> priceLine = new ArrayList<>(nodeLen);
            List<Entry> avgPriceLine = new ArrayList<>(nodeLen);
            List<BarEntry> barEntryList = new ArrayList<>(nodeLen);
            int[] barColors = new int[nodeLen];
            BigDecimal totalDealNum = BigDecimal.ZERO;
            BigDecimal totalDealMoney = BigDecimal.ZERO;
            BigDecimal upPrice = stockMinuteKLineData.getPreClosePrice();
            BigDecimal highPrice = upPrice;
            BigDecimal lowPrice = upPrice;
            Long highDealNum = 0l;
            for (int i = 0; i < nodeLen; i++) {
                StockMinuteKLineNodePo stockChartMinuteNodeDataPo =
                        stockMinuteKLineData.getKlineData().get(i);
                String currentTime = stockChartMinuteNodeDataPo.getTime();
                BigDecimal currentPrice = stockChartMinuteNodeDataPo.getPrice();
                Long currentDealNum = stockChartMinuteNodeDataPo.getDealNum();
                priceLine.add(new Entry(i, currentPrice.floatValue()));
                totalDealNum = totalDealNum.add(
                        new BigDecimal(String.valueOf(currentDealNum)));
                totalDealMoney = totalDealMoney.add(
                        currentPrice.multiply(
                                new BigDecimal(currentDealNum)
                        )
                );
                float avgPrice = totalDealMoney.divide(
                        totalDealNum, 2, RoundingMode.HALF_UP
                ).floatValue();
                avgPriceLine.add(new Entry(i, avgPrice));
                int priceCompare = currentPrice.compareTo(upPrice);
                if (priceCompare >= 1) {
                    barColors[i] = upColor;
                } else if (priceCompare <= -1) {
                    barColors[i] = downColor;
                } else {
                    barColors[i] = flatColor;
                }
                float dealNum = currentDealNum / 100;
                barEntryList.add(new BarEntry(i, dealNum));
                int highPriceCompare = currentPrice.compareTo(highPrice);
                if (highPriceCompare >= 1) {
                    highPrice = currentPrice;
                }
                int lowPriceCompare = currentPrice.compareTo(lowPrice);
                if (lowPriceCompare <= -1) {
                    lowPrice = currentPrice;
                }
                if (highDealNum < currentDealNum) {
                    highDealNum = currentDealNum;
                }
                upPrice = currentPrice;
                //创建提示文案
                List<Object> timeMarkerList = new ArrayList<>();
                timeMarkerList.add(currentTime);
                timeMarkerList.add(Arrays.asList(CHART_LABEL_PRICE_LINE, currentPrice.toString()));
                timeMarkerList.add(Arrays.asList(CHART_LABEL_AVG_PRICE_LINE, String.valueOf(avgPrice)));
                timeMarkerStrArr[i] = getMarkerViewStr(timeMarkerList);
                List<Object> barMarkerList = new ArrayList<>();
                barMarkerList.add(currentTime);
                barMarkerList.add(Arrays.asList(CHART_LABEL_DEAL_NUM_BAR, String.valueOf((int) dealNum)));
                barMarkerStrArr[i] = getMarkerViewStr(barMarkerList);
            }
            //设置线图数据
            setLineDataSet(priceLine, avgPriceLine);
            //设置柱状图数据
            setBarDataSet(barEntryList, barColors);
            //设置线图昨日收盘价
            setLinePreClosePrice(stockMinuteKLineData.getPreClosePrice());
            //设置价格显示范围
            setLineChartValueScope(stockMinuteKLineData.getPreClosePrice(), highPrice, lowPrice);
            //设置成交量范围
            setBarChartDealNumScope(highDealNum);
            //设置与上边无间隔
            ViewPortHandler viewPortHandler = barChart.getViewPortHandler();
            barChart.setViewPortOffsets(
                    viewPortHandler.offsetLeft(),
                    5,
                    viewPortHandler.offsetRight(),
                    viewPortHandler.offsetBottom()
            );
            lineChart.notifyDataSetChanged();
            barChart.notifyDataSetChanged();
            lineChart.postInvalidate();
            barChart.postInvalidate();
            ((StockMarkerView) lineChart.getMarker()).setMarkerStrArr(timeMarkerStrArr);
            ((StockMarkerView) barChart.getMarker()).setMarkerStrArr(barMarkerStrArr);
        }
    }

    /**
     * 设置线图数据
     *
     * @param priceLine
     * @param avgPriceLine
     */
    private void setLineDataSet(List<Entry> priceLine, List<Entry> avgPriceLine) {
        //价格线
        LineDataSet priceLineDataSet = new LineDataSet(priceLine, CHART_LABEL_PRICE_LINE);
        //不显示圆圈
        priceLineDataSet.setDrawCircles(false);
        //不显示数值
        priceLineDataSet.setDrawValues(false);
        //设置线图颜色
        priceLineDataSet.setColor(priceLineColor);
        //允许高亮
        priceLineDataSet.setHighlightEnabled(true);
        //设置数值选择是的颜色
        priceLineDataSet.setHighLightColor(highlightColor);
        //均值线
        LineDataSet avgPriceLineDataSet = new LineDataSet(avgPriceLine, "均价");
        //不显示圆圈
        avgPriceLineDataSet.setDrawCircles(false);
        //不显示数值
        avgPriceLineDataSet.setDrawValues(false);
        //设置线图颜色
        avgPriceLineDataSet.setColor(avgPriceLineColor);
        avgPriceLineDataSet.setHighlightEnabled(false);
        LineData lineData = new LineData();
        lineData.addDataSet(priceLineDataSet);
        lineData.addDataSet(avgPriceLineDataSet);
        //设置显示数据
        lineChart.setData(lineData);
    }

    /**
     * 设置柱状图数据
     *
     * @param barEntryList
     * @param barColors
     */
    private void setBarDataSet(List<BarEntry> barEntryList, int[] barColors) {
        BarDataSet barDataSet = new BarDataSet(barEntryList, "成交量");
        barDataSet.setColors(barColors);
        barDataSet.setHighlightEnabled(true);
        //设置数值选择是的颜色
        barDataSet.setHighLightColor(highlightColor);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
    }

    /**
     * 设置价格区间
     *
     * @param preClosePrice
     * @param highPrice
     * @param lowPrice
     */
    private void setLineChartValueScope(
            BigDecimal preClosePrice, BigDecimal highPrice, BigDecimal lowPrice
    ) {
        BigDecimal highPriceScope = highPrice.subtract(preClosePrice).abs();
        BigDecimal lowPriceScope = lowPrice.subtract(preClosePrice).abs();
        BigDecimal priceScope = highPriceScope.subtract(lowPriceScope).compareTo(BigDecimal.ZERO) >= 1
                ? highPriceScope : lowPriceScope;
        lineLeftY.setAxisMinimum(preClosePrice.subtract(priceScope).floatValue());
        lineLeftY.setAxisMaximum(preClosePrice.add(priceScope).floatValue());
        BigDecimal uptickRate = priceScope
                .multiply(new BigDecimal(100))
                .divide(preClosePrice, 2, RoundingMode.HALF_UP);
        lineRightY.setAxisMaximum(uptickRate.floatValue());
        lineRightY.setAxisMinimum(
                uptickRate
                        .divide(new BigDecimal("-1"), 2, RoundingMode.HALF_UP)
                        .floatValue()
        );
    }

    /**
     * 线图设置昨日收盘价
     *
     * @param preClosePrice
     */
    private void setLinePreClosePrice(BigDecimal preClosePrice) {
        StockSingleDayMinuteLineYAxisRenderer stockSingleDayMinuteLineYAxisRenderer = (StockSingleDayMinuteLineYAxisRenderer) lineChart.getRendererLeftYAxis();
        stockSingleDayMinuteLineYAxisRenderer.setFlatValue(preClosePrice.floatValue());
    }

    /**
     * 设置柱状图显示范围
     *
     * @param highDealNum
     */
    private void setBarChartDealNumScope(Long highDealNum) {
        //设置最小值为0
        barLeftY.setAxisMinimum(0f);
        //设置最大值为分钟最大的交易量
        barLeftY.setAxisMaximum((float) highDealNum / 100);
        //设置左Y轴渲染器
        StockSingleDayMinuteBarYAxisRenderer stockSingleDayMinuteBarYAxisRenderer =
                (StockSingleDayMinuteBarYAxisRenderer) barChart.getRendererLeftYAxis();
        //设置左Y轴刻度值
        String[] labelArr = new String[BAR_Y_LABEL_COUNT];
        for (int i = 0; i < BAR_Y_LABEL_COUNT; i++) {
            if (i == 0) {
                labelArr[i] = "手";
            } else if (i == BAR_Y_LABEL_COUNT - 1) {
                labelArr[i] = String.valueOf(highDealNum / 100);
            } else {
                labelArr[i] = "";
            }
        }
        stockSingleDayMinuteBarYAxisRenderer.setLabels(labelArr);
    }

    /**
     * 刷新数据
     */
    public void freshData() {
        Runnable stockMinuteKLineRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                stockMinuteKLinePo = stockSpiderRealtimeMinuteKLineApi.realtimeMinuteKLine(stockVo);
                setStockMinuteKLineData(stockMinuteKLinePo);
            }
        };
        Thread thread = new Thread(stockMinuteKLineRunnable);
        thread.start();
    }
}
