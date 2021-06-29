package com.fox.stockhelperchart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelperchart.chart.StockMultiDayMinuteBarChart;
import com.fox.stockhelperchart.chart.StockMultiDayMinuteLineChart;
import com.fox.stockhelperchart.formatter.StockPriceFormatter;
import com.fox.stockhelperchart.formatter.StockXAxisFormatter;
import com.fox.stockhelperchart.markerview.StockMarkerView;
import com.fox.stockhelperchart.renderer.chart.StockMultiDayMinuteLineChartRenderer;
import com.fox.stockhelperchart.renderer.xaxis.StockMultiDayMinuteLineXAxisRenderer;
import com.fox.stockhelperchart.renderer.yaxis.StockMultiDayMinuteBarYAxisRenderer;
import com.fox.stockhelperchart.renderer.yaxis.StockMultiDayMinuteLineYAxisRenderer;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLineNodePo;
import com.fox.stockhelpercommon.entity.stock.po.StockMinuteKLinePo;
import com.fox.stockhelpercommon.spider.out.StockSpiderFiveDayMinuteKLineApi;
import com.github.mikephil.charting.components.LimitLine;
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
import java.util.TreeMap;

import lombok.SneakyThrows;

/**
 * @author lusongsong
 * @date 2021/3/26 18:07
 */
public class StockMultiDayMinuteChart extends BaseStockChart {
    StockMultiDayMinuteLineChart lineChart;

    StockMultiDayMinuteBarChart barChart;

    StockSpiderFiveDayMinuteKLineApi stockSpiderFiveDayMinuteKLineApi =
            new StockSpiderFiveDayMinuteKLineApi();

    List<StockMinuteKLinePo> stockMinuteKLinePoList;

    public StockMultiDayMinuteChart(Context context) {
        super(context);
    }

    public StockMultiDayMinuteChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StockMultiDayMinuteChart(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
    }

    public StockMultiDayMinuteChart(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes
    ) {
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
                R.layout.stock_multi_day_minute_chart, this, true
        );
        lineChart = findViewById(R.id.stockMultiDayMinuteLineChart);
        barChart = findViewById(R.id.stockMultiDayMinuteBarChart);
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
    }

    /**
     * 初始化线图左Y轴
     */
    private void initLineLeftYAxis() {
        lineLeftY = lineChart.getAxisLeft();
        //左Y轴显示在图标内部
        lineLeftY.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //Y轴不显示网格线
        lineLeftY.setDrawGridLines(true);
        //设置默认值显示的刻度数量
        lineLeftY.setLabelCount(LINE_Y_LABEL_COUNT, true);
        //设置左Y轴渲染器
        StockMultiDayMinuteLineYAxisRenderer stockMultiDayMinuteLineYAxisRenderer =
                new StockMultiDayMinuteLineYAxisRenderer(
                        lineChart.getViewPortHandler(),
                        lineLeftY,
                        lineChart.getTransformer(YAxis.AxisDependency.LEFT)
                );
        stockMultiDayMinuteLineYAxisRenderer.setLabelColorArr(colorArr);
        stockMultiDayMinuteLineYAxisRenderer.setLabelStep(1);
        lineChart.setRendererLeftYAxis(stockMultiDayMinuteLineYAxisRenderer);
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
        //添加增幅为0的提示线
        LimitLine zeroLimitLine = new LimitLine(0);
        zeroLimitLine.enableDashedLine(10f, 10f, 0);
        zeroLimitLine.setLineColor(borderColor);
        lineRightY.addLimitLine(zeroLimitLine);
        //设置默认值显示的刻度数量
        lineRightY.setLabelCount(LINE_Y_LABEL_COUNT, true);
        //设置右Y轴渲染器
        StockMultiDayMinuteLineYAxisRenderer stockMultiDayMinuteLineYAxisRenderer =
                new StockMultiDayMinuteLineYAxisRenderer(
                        lineChart.getViewPortHandler(),
                        lineRightY,
                        lineChart.getTransformer(YAxis.AxisDependency.RIGHT)
                );
        stockMultiDayMinuteLineYAxisRenderer.setLabelColorArr(colorArr);
        stockMultiDayMinuteLineYAxisRenderer.setFlatValue(0);
        stockMultiDayMinuteLineYAxisRenderer.setLabelStep(1);
        lineChart.setRendererRightYAxis(stockMultiDayMinuteLineYAxisRenderer);
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
    }

    /**
     * 初始化柱图左Y轴
     */
    private void initBarLeftYAxis() {
        barLeftY = barChart.getAxisLeft();
        //设置默认值显示的刻度数量
        barLeftY.setLabelCount(BAR_Y_LABEL_COUNT, true);
        //设置左Y轴渲染器
        StockMultiDayMinuteBarYAxisRenderer stockMultiDayMinuteBarYAxisRenderer =
                new StockMultiDayMinuteBarYAxisRenderer(
                        barChart.getViewPortHandler(),
                        barLeftY,
                        barChart.getTransformer(YAxis.AxisDependency.LEFT)
                );
        barChart.setRendererLeftYAxis(stockMultiDayMinuteBarYAxisRenderer);
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
                barChart.highlightValue(
                        new Highlight(h.getX(), h.getDataSetIndex(), -1)
                );
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
                lineChart.highlightValue(
                        new Highlight(h.getX(), h.getDataSetIndex(), -1)
                );
            }

            @Override
            public void onNothingSelected() {
                lineChart.highlightValues(null);
            }
        });
    }

    /**
     * 设置分钟线图数据
     *
     * @param stockMinuteKLinePoList
     */
    public void setStockMinuteKLineData(List<StockMinuteKLinePo> stockMinuteKLinePoList) {
        if (null != stockMinuteKLinePoList && !stockMinuteKLinePoList.isEmpty()) {
            for (StockMinuteKLinePo stockMinuteKLinePo : stockMinuteKLinePoList) {
                if (null == stockMinuteKLinePo) {
                    stockMinuteKLinePoList.remove(null);
                    continue;
                }
                List<StockMinuteKLineNodePo> klineData = stockMinuteKLinePo.getKlineData();
                if (null == klineData || klineData.isEmpty()) {
                    stockMinuteKLinePoList.remove(stockMinuteKLinePo);
                }
            }
            //天数
            int dayNum = stockMinuteKLinePoList.size();
            //每天的时刻点数
            int singleDayNodeCount;
            if (dayNum > 1) {
                singleDayNodeCount = stockMinuteKLinePoList.get(dayNum - 2).getKlineData().size();
            } else {
                singleDayNodeCount =
                        Math.max(
                                stockMinuteKLinePoList.get(dayNum - 1).getKlineData().size(),
                                X_NODE_COUNT
                        );
            }
            //当前点数
            int pos = 0;
            //时间提示文案数组
            List<String> timeMarkerStrList = new ArrayList<>();
            //柱状图提示文案数组
            List<String> barMarkerStrList = new ArrayList<>();
            //价格线图数据
            List<Entry> priceLine = new ArrayList<>();
            //均价线图数据
            List<Entry> avgPriceLine = new ArrayList<>();
            //柱状图数据
            List<BarEntry> barEntryList = new ArrayList<>();
            List<Integer> barColors = new ArrayList<>();
            List<String> dateList = new ArrayList<>();
            BigDecimal highPrice = null;
            BigDecimal lowPrice = null;
            Long highDealNum = 0l;
            for (StockMinuteKLinePo stockMinuteKLinePo : stockMinuteKLinePoList) {
                List<StockMinuteKLineNodePo> klineData = stockMinuteKLinePo.getKlineData();
                dateList.add(stockMinuteKLinePo.getDt());
                BigDecimal totalDealNum = BigDecimal.ZERO;
                BigDecimal totalDealMoney = BigDecimal.ZERO;
                BigDecimal upPrice = stockMinuteKLinePo.getPreClosePrice();
                highPrice = null == highPrice ? upPrice : highPrice;
                lowPrice = null == lowPrice ? upPrice : lowPrice;
                for (StockMinuteKLineNodePo stockMinuteKLineNodePo : klineData) {
                    String currentTime = stockMinuteKLineNodePo.getTime();
                    BigDecimal currentPrice = stockMinuteKLineNodePo.getPrice();
                    Long currentDealNum = stockMinuteKLineNodePo.getDealNum();
                    priceLine.add(new Entry(pos, currentPrice.floatValue()));
                    totalDealNum = totalDealNum.add(
                            new BigDecimal(String.valueOf(currentDealNum)));
                    totalDealMoney = totalDealMoney.add(
                            currentPrice.multiply(
                                    new BigDecimal(currentDealNum)
                            )
                    );
                    float avgPrice = BigDecimal.ZERO.compareTo(totalDealNum) == 0 ?
                            currentPrice.floatValue()
                            :
                            totalDealMoney.divide(totalDealNum, 2, RoundingMode.HALF_UP)
                                    .floatValue();
                    avgPriceLine.add(new Entry(pos, avgPrice));
                    int priceCompare = currentPrice.compareTo(upPrice);
                    if (priceCompare >= 1) {
                        barColors.add(upColor);
                    } else if (priceCompare <= -1) {
                        barColors.add(downColor);
                    } else {
                        barColors.add(flatColor);
                    }
                    float dealNum = (float) currentDealNum / 100;
                    barEntryList.add(new BarEntry(pos, dealNum));
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
                    timeMarkerList.add(stockMinuteKLinePo.getDt());
                    timeMarkerList.add(currentTime);
                    timeMarkerList.add(
                            Arrays.asList(CHART_LABEL_PRICE_LINE, currentPrice.toString())
                    );
                    timeMarkerList.add(
                            Arrays.asList(CHART_LABEL_AVG_PRICE_LINE, String.valueOf(avgPrice))
                    );
                    timeMarkerStrList.add(getMarkerViewStr(timeMarkerList));
                    List<Object> barMarkerList = new ArrayList<>();
                    barMarkerList.add(stockMinuteKLinePo.getDt());
                    barMarkerList.add(currentTime);
                    barMarkerList.add(
                            Arrays.asList(CHART_LABEL_DEAL_NUM_BAR, String.valueOf((int) dealNum))
                    );
                    barMarkerStrList.add(getMarkerViewStr(barMarkerList));
                    pos++;
                }
            }
            //设置线图x轴
            setLineXAxis(dayNum, singleDayNodeCount, dateList);
            //设置柱图x轴
            setBarXAxis(dayNum, singleDayNodeCount, dateList);
            //设置价格显示范围
            setLineChartValueScope(stockMinuteKLinePoList.get(0).getPreClosePrice(), highPrice, lowPrice);
            //设置线图数据
            setLineDataSet(priceLine, avgPriceLine);
            //设置柱状图数据
            setBarDataSet(barEntryList, barColors);
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
            ((StockMarkerView) lineChart.getMarker())
                    .setMarkerStrArr(stringListToArray(timeMarkerStrList));
            ((StockMarkerView) barChart.getMarker())
                    .setMarkerStrArr(stringListToArray(barMarkerStrList));
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
    private void setBarDataSet(List<BarEntry> barEntryList, List<Integer> barColors) {
        BarDataSet barDataSet = new BarDataSet(barEntryList, "成交量");
        barDataSet.setColors(barColors);
        barDataSet.setHighlightEnabled(true);
        //设置数值选择是的颜色
        barDataSet.setHighLightColor(highlightColor);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
    }

    /**
     * 设置线图x轴
     *
     * @param dayNum
     * @param singleDayNodeCount
     */
    private void setLineXAxis(int dayNum, int singleDayNodeCount, List<String> dateList) {
        //X轴设置最大的显示点数
        lineX.setAxisMaximum(dayNum * singleDayNodeCount);
        //设置默认值显示的刻度数量
        lineX.setLabelCount(dayNum, true);
        //指定需要断开的点
        List<Integer> breakPosList = new ArrayList<>(dayNum);
        for (int i = 0; i < dayNum - 1; i++) {
            breakPosList.add(singleDayNodeCount * (i + 1) + 1);
        }
        ((StockMultiDayMinuteLineChartRenderer) lineChart.getRenderer())
                .setBreakPos(breakPosList);
        //设置X轴需要的位置坐标
        int[] gradLinePos = new int[dayNum + 1];
        int[] labelPos = new int[dayNum];
        TreeMap<Integer, String> labelMap = new TreeMap<>();
        for (int i = 0; i <= dayNum; i++) {
            gradLinePos[i] = singleDayNodeCount * i;
        }
        for (int i = 0; i < dayNum; i++) {
            labelMap.put(singleDayNodeCount / 2 + singleDayNodeCount * i, dateList.get(i));
            labelPos[i] = singleDayNodeCount / 2 + singleDayNodeCount * i;
        }
        ((StockMultiDayMinuteLineXAxisRenderer) lineChart.getRendererXAxis())
                .setGradLinePos(gradLinePos);
        ((StockMultiDayMinuteLineXAxisRenderer) lineChart.getRendererXAxis()).setLabelPos(labelPos);
        ((StockXAxisFormatter) lineX.getValueFormatter()).setLabels(labelMap);
    }

    /**
     * 设置柱图x轴
     *
     * @param dayNum
     * @param singleDayNodeCount
     */
    private void setBarXAxis(int dayNum, int singleDayNodeCount, List<String> dateList) {
        //X轴设置最大的显示点数
        barX.setAxisMaximum(dayNum * singleDayNodeCount);
        //设置默认值显示的刻度数量
        barX.setLabelCount(dayNum, true);
        int[] gradLinePos = new int[dayNum + 1];
        int[] labelPos = new int[dayNum];
        TreeMap<Integer, String> labelMap = new TreeMap<>();
        for (int i = 0; i <= dayNum; i++) {
            gradLinePos[i] = singleDayNodeCount * i;
        }
        for (int i = 0; i < dayNum; i++) {
            labelMap.put(singleDayNodeCount / 2 + singleDayNodeCount * i, dateList.get(i));
            labelPos[i] = singleDayNodeCount / 2 + singleDayNodeCount * i;
        }
        ((StockMultiDayMinuteLineXAxisRenderer) barChart.getRendererXAxis())
                .setGradLinePos(gradLinePos);
        ((StockMultiDayMinuteLineXAxisRenderer) barChart.getRendererXAxis()).setLabelPos(labelPos);
        ((StockXAxisFormatter) barX.getValueFormatter()).setLabels(labelMap);
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
        ((StockMultiDayMinuteLineYAxisRenderer) lineChart.getRendererLeftYAxis())
                .setFlatValue(preClosePrice.floatValue());
    }

    /**
     * list转数组
     *
     * @param stringList
     * @return
     */
    private String[] stringListToArray(List<String> stringList) {
        if (null != stringList) {
            String[] stringArr = new String[stringList.size()];
            for (int i = 0; i < stringList.size(); i++) {
                stringArr[i] = stringList.get(i);
            }
            return stringArr;
        }
        return null;
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
        StockMultiDayMinuteBarYAxisRenderer stockMultiDayMinuteBarYAxisRenderer =
                (StockMultiDayMinuteBarYAxisRenderer) barChart.getRendererLeftYAxis();
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
        stockMultiDayMinuteBarYAxisRenderer.setLabels(labelArr);
    }

    /**
     * 刷新数据
     */
    public void freshData() {
        Runnable stockMinuteKLineRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                stockMinuteKLinePoList = stockSpiderFiveDayMinuteKLineApi.fiveDayMinuteKLine(stockVo);
                setStockMinuteKLineData(stockMinuteKLinePoList);
            }
        };
        Thread thread = new Thread(stockMinuteKLineRunnable);
        thread.start();
    }
}
