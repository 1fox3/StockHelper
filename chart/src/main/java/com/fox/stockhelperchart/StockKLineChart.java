package com.fox.stockhelperchart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.spider.stock.util.DateUtil;
import com.fox.stockhelperchart.adapter.StockKLineBarTypeAdapter;
import com.fox.stockhelperchart.chart.StockKLineBarCombinedChart;
import com.fox.stockhelperchart.chart.StockKLineLineCombinedChart;
import com.fox.stockhelperchart.listener.StockKLineDataVisibleListener;
import com.fox.stockhelperchart.listener.StockKLineDateLabelListener;
import com.fox.stockhelperchart.listener.StockKLineMarkerViewTextListener;
import com.fox.stockhelperchart.listener.StockKLineOnChartGestureListener;
import com.fox.stockhelperchart.markerview.StockMarkerView;
import com.fox.stockhelperchart.renderer.xaxis.StockKLineLineXAxisRenderer;
import com.fox.stockhelpercommon.entity.stock.po.StockKLineNodePo;
import com.fox.stockhelpercommon.entity.stock.po.StockKLinePo;
import com.fox.stockhelpercommon.spider.out.StockSpiderKLineApi;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StockKLineChart extends BaseStockChart implements StockKLineDataVisibleListener, StockKLineDateLabelListener, StockKLineMarkerViewTextListener {
    public static final int KLINE_BAR_TYPE_DEAL_NUM = 0;
    public static final int KLINE_BAR_TYPE_DEAL_MONEY = 1;
    public static final int KLINE_BAR_TYPE_MACD = 2;
    public static final int KLINE_BAR_TYPE_KDJ = 3;
    public static final int KLINE_BAR_TYPE_RSI = 4;
    public static final int KLINE_BAR_TYPE_BOLL = 5;
    /**
     * 日期类型
     */
    List<Integer> kLineSupportDateType = Arrays.asList(
            StockConst.DT_DAY,
            StockConst.DT_MONTH,
            StockConst.DT_MONTH
    );
    /**
     * 复权类型
     */
    Map<String, Integer> kLineFqTypeMap = new TreeMap<String, Integer>() {{
        put("除权", StockConst.SFQ_AFTER);
        put("前复权", StockConst.SFQ_BEFORE);
    }};

    /**
     * 柱状图类型
     */
    Map<String, Integer> kLineBarTypeMap = new LinkedHashMap<String, Integer>() {{
        put("成交量", KLINE_BAR_TYPE_DEAL_NUM);
        put("成交金额", KLINE_BAR_TYPE_DEAL_MONEY);
        put("MACD", KLINE_BAR_TYPE_MACD);
        put("KDJ", KLINE_BAR_TYPE_KDJ);
        put("RSI", KLINE_BAR_TYPE_RSI);
        put("BOLL", KLINE_BAR_TYPE_BOLL);
    }};

    StockKLineLineCombinedChart lineChart;

    StockKLineBarCombinedChart barChart;

    ListView stockKLineBarTypeLV;

    StockKLineBarTypeAdapter stockKLineBarTypeAdapter;

    TextView stockFQTypeNoTV;

    TextView stockFQTypeBeforeTV;

    int dateType = StockConst.DT_DAY;
    int fqType = StockConst.SFQ_BEFORE;
    int barType = KLINE_BAR_TYPE_DEAL_NUM;
    StockSpiderKLineApi stockSpiderKLineApi = new StockSpiderKLineApi();
    Thread loadDataThread;
    String loadDataStartDate;
    String loadDataEndDate;
    List<StockKLineNodePo> stockKLineNodePoList = new ArrayList<>();
    boolean totalDataLoaded = false;
    boolean isLoadingData = false;
    float beforeDataLen = 70f;
    float visibleEndPos = 0;

    public StockKLineChart(Context context) {
        super(context);
    }

    public StockKLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StockKLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StockKLineChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        //初始化线图
        initLineChart();
        //初始化柱图
        initBarChart();
        //设置数据选择监听器
        setValueSelectedListener();
        //设置操作同步
        setOnChartGestureListener();
        //加载数据
        loadMoreData();
        ((StockMarkerView) lineChart.getMarker()).setStockKLineMarkerViewTextListener(this, "line");
        ((StockMarkerView) barChart.getMarker()).setStockKLineMarkerViewTextListener(this, "bar");
    }

    /**
     * 绑定布局文件
     */
    private void bindLayout() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.stock_kline_chart, this, true
        );
        lineChart = findViewById(R.id.stockKLineLineCombinedChart);
        barChart = findViewById(R.id.stockKLineBarCombinedChart);
        stockKLineBarTypeLV = findViewById(R.id.stockKLineBarTypeLV);
        stockFQTypeNoTV = findViewById(R.id.stockFQTypeNoTV);
        stockFQTypeBeforeTV = findViewById(R.id.stockFQTypeBeforeTV);

        stockKLineBarTypeAdapter = new StockKLineBarTypeAdapter(getContext(), R.layout.stock_kline_bar_type_item);
        stockKLineBarTypeAdapter.setStockLineBarTypeList(new ArrayList<>(kLineBarTypeMap.keySet()));
        stockKLineBarTypeAdapter.setSelectColor(upColor);
        stockKLineBarTypeAdapter.setSelectedPosition(barType);
        stockKLineBarTypeLV.setAdapter(stockKLineBarTypeAdapter);
        stockKLineBarTypeLV.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        stockKLineBarTypeLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.kLineBarTypeItemTV);
                setBarType(textView.getText().toString());
                switchBarDataType();
            }
        });
        stockFQTypeNoTV.setOnClickListener(getFQTypeOnClickListener());
        stockFQTypeBeforeTV.setOnClickListener(getFQTypeOnClickListener());
        stockFQTypeBeforeTV.callOnClick();
    }

    /**
     * 设置日期类型
     *
     * @param dtType
     */
    public void setDateType(int dtType) {
        dateType = kLineSupportDateType.contains(dtType) ? dtType : dateType;
    }

    /**
     * 设置柱状图类型
     *
     * @param barTypeStr
     */
    public void setBarType(String barTypeStr) {
        barType = kLineBarTypeMap.containsKey(barTypeStr) ? kLineBarTypeMap.get(barTypeStr) : barType;
    }

    /**
     * 设置复权类型
     *
     * @param fqTypeStr
     */
    public void setFqType(String fqTypeStr) {
        fqType = kLineFqTypeMap.containsKey(fqTypeStr) ?
                kLineFqTypeMap.get(fqTypeStr) : fqType;
        stockFQTypeNoTV.setTextColor(Color.BLACK);
        stockFQTypeBeforeTV.setTextColor(Color.BLACK);
        if (fqType == StockConst.SFQ_AFTER) {
            stockFQTypeNoTV.setTextColor(upColor);
        }
        if (fqType == StockConst.SFQ_BEFORE) {
            stockFQTypeBeforeTV.setTextColor(upColor);
        }
    }

    /**
     * 监听复权类型切换
     *
     * @return
     */
    private OnClickListener getFQTypeOnClickListener() {
        return new OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                int oldFqType = fqType;
                setFqType((((TextView) v)).getText().toString());
                if (fqType != oldFqType) {
                    clearData();
                    loadMoreData();
                }
            }
        };
    }

    /**
     * 清除数据
     */
    private void clearData() {
        loadDataThread = null;
        loadDataStartDate = null;
        loadDataEndDate = null;
        beforeDataLen = (float) stockKLineNodePoList.size();
        stockKLineNodePoList = new ArrayList<>();
        totalDataLoaded = false;
        isLoadingData = false;
        visibleEndPos = 0;
    }

    /**
     * 设置数值选择监听器
     */
    private void setValueSelectedListener() {
        //移动十字标数据监听
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                lineChart.highlightValue(h);
                if (barChart.getData().getBarData().getDataSets().size() != 0) {
                    Highlight highlight = new Highlight(h.getX(), h.getDataSetIndex(), h.getStackIndex());
                    highlight.setDataIndex(h.getDataIndex());
                    barChart.highlightValues(new Highlight[]{highlight});
                } else {
                    Highlight highlight = new Highlight(h.getX(), 2, h.getStackIndex());
                    highlight.setDataIndex(0);
                    barChart.highlightValues(new Highlight[]{highlight});
                }
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValues(null);
            }
        });
        //移动十字标数据监听
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                barChart.highlightValue(h);
                Highlight highlight = new Highlight(h.getX(), 0, h.getStackIndex());
                highlight.setDataIndex(1);
                lineChart.highlightValues(new Highlight[]{highlight});
            }

            @Override
            public void onNothingSelected() {
                lineChart.highlightValues(null);
            }
        });
    }

    /**
     * 设置操作同步
     */
    private void setOnChartGestureListener() {
        lineChart.setOnChartGestureListener(
                new StockKLineOnChartGestureListener(lineChart, new Chart[]{barChart})
        );
        StockKLineOnChartGestureListener barStockKLineOnChartGestureListener =
                new StockKLineOnChartGestureListener(barChart, new Chart[]{lineChart});
        barStockKLineOnChartGestureListener.setCoupleClick(
                new StockKLineOnChartGestureListener.CoupleClick() {
                    @Override
                    public void singleClickListener() {
                        barType = barType < kLineBarTypeMap.keySet().size() - 1 ? ++barType : 0;
                        switchBarDataType();
                    }
                });
        barChart.setOnChartGestureListener(
                barStockKLineOnChartGestureListener
        );
    }

    /**
     * 初始化线图
     */
    private void initLineChart() {
        ((StockKLineLineXAxisRenderer) lineChart.getRendererXAxis())
                .setStockKLineDataVisibleListener(this);
        ((StockKLineLineXAxisRenderer) lineChart.getRendererXAxis())
                .setStockKLineDateLabelListener(this);
        //初始化线图X轴
        initLineXAxis();
        //初始化线图左Y轴
        initLineLeftYAxis();
        //初始化线图右Y轴
        initLineRightYAxis();
    }

    private float getStockScaleX() {
        return stockKLineNodePoList.size() / beforeDataLen;
    }

    /**
     * 初始化线图X轴
     */
    private void initLineXAxis() {
        lineX = lineChart.getXAxis();
        //设置默认值显示的刻度数量
        lineX.setLabelCount(X_LABEL_COUNT, true);
    }

    /**
     * 初始化线图左Y轴
     */
    private void initLineLeftYAxis() {
        lineLeftY = lineChart.getAxisLeft();
        //设置默认值显示的刻度数量
        lineLeftY.setLabelCount(LINE_Y_LABEL_COUNT, true);
    }

    /**
     * 初始化线图右Y轴
     */
    private void initLineRightYAxis() {
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
        kLineBarTypeMap.keySet().size();
    }

    /**
     * 初始化柱图X轴
     */
    private void initBarXAxis() {
        barX = barChart.getXAxis();
        //设置默认值显示的刻度数量
        barX.setLabelCount(X_LABEL_COUNT, true);
    }

    /**
     * 初始化柱图左Y轴
     */
    private void initBarLeftYAxis() {
        barLeftY = barChart.getAxisLeft();
        //设置默认值显示的刻度数量
        barLeftY.setLabelCount(BAR_Y_LABEL_COUNT, true);
    }

    /**
     * 初始化柱图右Y轴
     */
    private void initBarRightYAxis() {
    }

    /**
     * 加载更多数据
     */
    public void loadMoreData() {
        if (null == loadDataThread) {
            setLoadDataThread();
        }
        if (null != stockKLineNodePoList && stockKLineNodePoList.size() > 0) {
            beforeDataLen = (float) stockKLineNodePoList.size();
        }
        //日期范围
        getNextDateScope();
        loadDataThread.start();
    }

    /**
     * 设置数据加载线程
     */
    private void setLoadDataThread() {
        loadDataThread = new Thread(
                () -> {
                    if (!totalDataLoaded && !isLoadingData) {
                        isLoadingData = true;
                        StockKLinePo stockKLinePo = stockSpiderKLineApi.kLine(
                                stockVo,
                                dateType,
                                fqType,
                                loadDataStartDate,
                                loadDataEndDate
                        );
                        if (stockKLinePo != null) {
                            List<StockKLineNodePo> klineData = stockKLinePo.getKlineData();
                            if (null != klineData && !klineData.isEmpty()) {
                                stockKLineNodePoList.addAll(0, klineData);
                                freshChartData();
                            } else {
                                totalDataLoaded = true;
                            }
                        }
                        isLoadingData = false;
                    }
                }
        );
    }

    /**
     * 日期范围
     */
    private void getNextDateScope() {
        if (isLoadingData || totalDataLoaded) {
            return;
        }
        loadDataEndDate = null == loadDataEndDate
                ?
                DateUtil.getCurrentDate()
                :
                DateUtil.getRelateDate(
                        loadDataStartDate,
                        0,
                        0,
                        -1,
                        DateUtil.DATE_FORMAT_1
                );
        switch (dateType) {
            case StockConst.DT_WEEK:
                loadDataStartDate = DateUtil.getRelateDate(
                        loadDataEndDate,
                        -10,
                        0,
                        0,
                        DateUtil.DATE_FORMAT_1
                );
                break;
            case StockConst.DT_MONTH:
                loadDataStartDate = DateUtil.getRelateDate(
                        loadDataEndDate,
                        -50,
                        0,
                        0,
                        DateUtil.DATE_FORMAT_1
                );
                break;
            default:
                loadDataStartDate = DateUtil.getRelateDate(
                        loadDataEndDate,
                        -2,
                        0,
                        0,
                        DateUtil.DATE_FORMAT_1
                );
                break;
        }
    }

    /**
     * 刷新表格数据
     */
    private void freshChartData() {
        setLineData();
        setBarData();
    }

    /**
     * 获取当前可见的结束位置
     *
     * @return
     */
    private float getVisibleEndPos() {
        return 0 >= visibleEndPos ?
                stockKLineNodePoList.size() - 1
                :
                stockKLineNodePoList.size() - beforeDataLen + visibleEndPos;
    }

    /**
     * 设置线图数据
     */
    private void setLineData() {
        CombinedData combinedData = getLineCombinedData();
        lineChart.setData(combinedData);
        //计算缩放比例,以便可以左右滑动
        lineChart.zoom(getStockScaleX(), 0, 0, 0);
        //设置X轴的坐标范围，以便显示全部图表
        lineChart.getXAxis().setAxisMinimum(combinedData.getXMin() - 0.5f);
        lineChart.getXAxis().setAxisMaximum(combinedData.getXMax() + 0.5f);
        lineChart.notifyDataSetChanged();
        //滑动到尾部
        lineChart.moveViewToX(getVisibleEndPos());
    }

    /**
     * 获取线图的组合数据
     *
     * @return
     */
    private CombinedData getLineCombinedData() {
        CombinedData lineCombinedData = new CombinedData();
        lineCombinedData.setData(getLineCandleData());
        lineCombinedData.setData(getLinePriceData());
        return lineCombinedData;
    }

    /**
     * 线图的蜡烛线数据
     *
     * @return
     */
    private CandleData getLineCandleData() {
        ArrayList<CandleEntry> candleEntryList = new ArrayList<>();
        int i = 0;
        for (StockKLineNodePo stockKLineNodePo : stockKLineNodePoList) {
            candleEntryList.add(
                    new CandleEntry(
                            i,
                            stockKLineNodePo.getHighestPrice().floatValue(),
                            stockKLineNodePo.getLowestPrice().floatValue(),
                            stockKLineNodePo.getOpenPrice().floatValue(),
                            stockKLineNodePo.getClosePrice().floatValue()
                    )
            );
            i++;
        }
        CandleDataSet candleDataSet = new CandleDataSet(candleEntryList, "蜡烛线");
        candleDataSet.setDrawHorizontalHighlightIndicator(true);
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setHighLightColor(ContextCompat.getColor(getContext(), R.color.stockUp));
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setDecreasingColor(ContextCompat.getColor(getContext(), R.color.stockDown));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(ContextCompat.getColor(getContext(), R.color.stockUp));
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setNeutralColor(ContextCompat.getColor(getContext(), R.color.stockFlat));
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setValueTextSize(10);
        candleDataSet.setDrawValues(false);
        return new CandleData(candleDataSet);
    }

    /**
     * 线图的价格线数据
     *
     * @return
     */
    private LineData getLinePriceData() {
        List<Float> closePriceList = new ArrayList<>();
        for (StockKLineNodePo stockKLineNodePo : stockKLineNodePoList) {
            closePriceList.add(stockKLineNodePo.getClosePrice().floatValue());
        }
        ArrayList<Entry> line5Entries = new ArrayList<>();
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line20Entries = new ArrayList<>();
        for (int i = stockKLineNodePoList.size() - 1; i > 0; i--) {
            Float ma5Price = maPrice(closePriceList, i, 5);
            if (null != ma5Price) {
                line5Entries.add(new Entry(i + 1, ma5Price));
            }
            Float ma10Price = maPrice(closePriceList, i, 10);
            if (null != ma10Price) {
                line10Entries.add(new Entry(i + 1, ma10Price));
            }
            Float ma20Price = maPrice(closePriceList, i, 20);
            if (null != ma20Price) {
                line20Entries.add(new Entry(i + 1, ma20Price));
            }
        }
        Collections.reverse(line5Entries);
        Collections.reverse(line10Entries);
        Collections.reverse(line20Entries);
        List<ILineDataSet> lineDataMA = new ArrayList<>();
        lineDataMA.add(getLineMAData(line5Entries, "MA5", ma5Color));
        lineDataMA.add(getLineMAData(line10Entries, "MA10", ma10Color));
        lineDataMA.add(getLineMAData(line20Entries, "MA20", ma20Color));
        return new LineData(lineDataMA);
    }

    /**
     * 计算日均价
     *
     * @param priceList
     * @param endPos
     * @param step
     * @return
     */
    private Float maPrice(List<Float> priceList, int endPos, int step) {
        if (null == priceList || priceList.isEmpty()) {
            return null;
        }
        int priceLen = priceList.size();
        int startPos = endPos - step + 1;
        if (endPos >= priceLen - 1 || startPos < 0 || endPos < startPos) {
            return null;
        }
        BigDecimal price = BigDecimal.ZERO;
        for (int i = startPos; i <= endPos; i++) {
            price = price.add(new BigDecimal(priceList.get(i)));
        }
        return price.divide(BigDecimal.valueOf(step), 2, RoundingMode.HALF_UP).floatValue();
    }

    /**
     * 设置柱图数据
     */
    private void setBarData() {
        CombinedData combinedData = getBarCombinedData();
        barChart.setData(combinedData);
        //计算缩放比例,以便可以左右滑动
        barChart.zoom(getStockScaleX(), 0, 0, 0);
        //设置X轴的坐标范围，以便显示全部图表
        barChart.getXAxis().setAxisMinimum(combinedData.getXMin() - 0.5f);
        barChart.getXAxis().setAxisMaximum(combinedData.getXMax() + 0.5f);
        barChart.notifyDataSetChanged();
        //滑动到尾部
        barChart.moveViewToX(getVisibleEndPos());
        //设置与上边无间隔
        ViewPortHandler viewPortHandler = barChart.getViewPortHandler();
        barChart.setViewPortOffsets(
                viewPortHandler.offsetLeft(),
                0,
                viewPortHandler.offsetRight(),
                viewPortHandler.offsetBottom()
        );
    }

    /**
     * 更改柱图数据
     */
    private void switchBarDataType() {
        CombinedData combinedData = getBarCombinedData();
        barChart.setData(combinedData);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        stockKLineBarTypeAdapter.setSelectedPosition(barType);
        stockKLineBarTypeAdapter.notifyDataSetChanged();
    }

    /**
     * 获取柱图的组合数据
     *
     * @return
     */
    private CombinedData getBarCombinedData() {
        switch (barType) {
            case KLINE_BAR_TYPE_DEAL_MONEY:
                return getBarDealMoneyCombinedData();
            case KLINE_BAR_TYPE_MACD:
                return getBarMACDCombinedData();
            case KLINE_BAR_TYPE_KDJ:
                return getBarKDJCombinedData();
            case KLINE_BAR_TYPE_RSI:
                return getBarRSICombinedData();
            case KLINE_BAR_TYPE_BOLL:
                return getBarBollCombinedData();
            default:
                return getBarDealNumCombinedData();
        }
    }

    /**
     * 获取成交量柱状图数据
     *
     * @return
     */
    private CombinedData getBarDealNumCombinedData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>(stockKLineNodePoList.size());
        int[] colors = new int[stockKLineNodePoList.size()];
        int i = 0;
        for (StockKLineNodePo stockKLineNodePo : stockKLineNodePoList) {
            float openPrice = stockKLineNodePo.getOpenPrice().floatValue();
            float closePrice = stockKLineNodePo.getClosePrice().floatValue();
            int colorIdx = openPrice == closePrice ? 2 : openPrice > closePrice ? 1 : 0;
            barEntries.add(
                    new BarEntry(
                            (float) i, stockKLineNodePo.getDealNum().floatValue() / 100
                    )
            );
            colors[i] = colorArr[colorIdx];
            i++;
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(false);
        //设置数值选择是的颜色
        barDataSet.setHighLightColor(ContextCompat.getColor(getContext(), R.color.stockUp));
        barDataSet.setHighlightEnabled(true);
        BarData barData = new BarData(barDataSet);
        CombinedData barCombinedData = new CombinedData();
        barCombinedData.setData(barData);
        barCombinedData.setData(new LineData());
        barCombinedData.setData(new CandleData());
        return barCombinedData;
    }

    /**
     * 获取成交金额柱状图数据
     *
     * @return
     */
    private CombinedData getBarDealMoneyCombinedData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>(stockKLineNodePoList.size());
        int[] colors = new int[stockKLineNodePoList.size()];
        int i = 0;
        for (StockKLineNodePo stockKLineNodePo : stockKLineNodePoList) {
            float openPrice = stockKLineNodePo.getOpenPrice().floatValue();
            float closePrice = stockKLineNodePo.getClosePrice().floatValue();
            int colorIdx = openPrice == closePrice ? 2 : openPrice > closePrice ? 1 : 0;
            barEntries.add(
                    new BarEntry(
                            (float) i, stockKLineNodePo.getDealMoney().floatValue()
                    )
            );
            colors[i] = colorArr[colorIdx];
            i++;
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "成交金额");
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(false);
        //设置数值选择是的颜色
        barDataSet.setHighLightColor(ContextCompat.getColor(getContext(), R.color.stockUp));
        barDataSet.setHighlightEnabled(true);
        BarData barData = new BarData(barDataSet);
        CombinedData barCombinedData = new CombinedData();
        barCombinedData.setData(barData);
        barCombinedData.setData(new LineData());
        barCombinedData.setData(new CandleData());
        return barCombinedData;
    }

    /**
     * 获取MACD柱状图数据
     *
     * @return
     */
    private CombinedData getBarMACDCombinedData() {
        int mNum = 9;
        int shortNum = 12;
        int longNum = 26;
        int dataLen = stockKLineNodePoList.size();
        ArrayList<BarEntry> madcBarEntries = new ArrayList<>(dataLen);
        ArrayList<Entry> deaBarEntries = new ArrayList<>(dataLen);
        ArrayList<Entry> difBarEntries = new ArrayList<>(dataLen);

        List<Float> dEAs = new ArrayList<Float>();
        List<Float> dIFs = new ArrayList<Float>();
        List<Float> mACDs = new ArrayList<Float>();

        float eMAShort = 0.0f;
        float eMALong = 0.0f;
        float closePrice = 0f;
        float dIF = 0.0f;
        float dEA = 0.0f;
        float mACD = 0.0f;
        for (int i = 0; i < dataLen; i++) {
            StockKLineNodePo stockKLineNodePo = stockKLineNodePoList.get(i);
            closePrice = stockKLineNodePo.getClosePrice().floatValue();
            if (i == 0) {
                eMAShort = closePrice;
                eMALong = closePrice;
            } else {
                eMAShort = eMAShort * (1 - 2.0f / (shortNum + 1)) + closePrice * 2.0f / (shortNum + 1);
                eMALong = eMALong * (1 - 2.0f / (longNum + 1)) + closePrice * 2.0f / (longNum + 1);
            }
            dIF = eMAShort - eMALong;
            dEA = dEA * (1 - 2.0f / (mNum + 1)) + dIF * 2.0f / (mNum + 1);
            mACD = dIF - dEA;
            dEAs.add(dEA);
            dIFs.add(dIF);
            mACDs.add(mACD);
        }

        int[] colors = new int[dataLen];
        for (int i = 0; i < dEAs.size(); i++) {
            int colorIdx = mACDs.get(i) > 0 ? 0 : 1;
            colors[i] = colorArr[colorIdx];
            deaBarEntries.add(new Entry(i, dEAs.get(i)));
            difBarEntries.add(new Entry(i, dIFs.get(i)));
            madcBarEntries.add(new BarEntry(i, mACDs.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(madcBarEntries, "MACD");
        barDataSet.setDrawValues(false);
        //设置数值选择是的颜色
        barDataSet.setHighLightColor(ContextCompat.getColor(getContext(), R.color.stockUp));
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);
        CombinedData barCombinedData = new CombinedData();
        barCombinedData.setData(barData);

        List<ILineDataSet> lineDataMA = new ArrayList<>();
        lineDataMA.add(getLineMAData(deaBarEntries, "dea", ma5Color));
        lineDataMA.add(getLineMAData(difBarEntries, "dif", ma10Color));
        LineData lineData = new LineData(lineDataMA);

        barCombinedData.setData(lineData);
        barCombinedData.setData(new CandleData());
        return barCombinedData;
    }

    /**
     * 获取KDJ柱状图数据
     *
     * @return
     */
    private CombinedData getBarKDJCombinedData() {
        int n = 9;
        int m1 = 3;
        int m2 = 3;
        int dataLen = stockKLineNodePoList.size();
        ArrayList<Entry> kEntries = new ArrayList<>(dataLen);
        ArrayList<Entry> dEntries = new ArrayList<>(dataLen);
        ArrayList<Entry> jEntries = new ArrayList<>(dataLen);

        List<Float> kValues = new ArrayList<Float>();
        List<Float> dValues = new ArrayList<Float>();
        List<Float> jValues = new ArrayList<Float>();

        float k = 50.0f;
        float d = 50.0f;
        float j = 0.0f;
        float rSV = 0.0f;

        if (stockKLineNodePoList != null && stockKLineNodePoList.size() > 0) {
            float highPrice = 0f, lowPrice = 0f, currentHighPrice = 0f, currentLowPrice = 0f, closePrice = 0f;
            for (int i = 0; i < dataLen; i++) {
                StockKLineNodePo stockKLineNodePo = stockKLineNodePoList.get(i);
                currentHighPrice = stockKLineNodePo.getHighestPrice().floatValue();
                currentLowPrice = stockKLineNodePo.getLowestPrice().floatValue();
                closePrice = stockKLineNodePo.getClosePrice().floatValue();

                if (0 == i) {
                    highPrice = currentHighPrice;
                    lowPrice = currentLowPrice;
                }
                if (i > 0) {
                    if (n == 0) {
                        highPrice = highPrice > currentHighPrice ? highPrice : currentHighPrice;
                        lowPrice = lowPrice < currentLowPrice ? lowPrice : currentLowPrice;
                    } else {
                        int t = i - n + 1;
                        Float[] wrs = getHighAndLowByK(t, i);
                        highPrice = wrs[0];
                        lowPrice = wrs[1];
                    }
                }
                if (highPrice != lowPrice) {
                    rSV = (float) ((closePrice - lowPrice) / (highPrice - lowPrice) * 100);
                } else {
                    rSV = 0;
                }
                k = k * (m1 - 1.0f) / m1 + rSV / m1;
                d = d * (m2 - 1.0f) / m2 + k / m2;
                j = (3 * k) - (2 * d);

                //其他软件没有大于100小于0的值，但是我算出来确实有，其它软件在0和100的时候出现直线，怀疑也是做了处理
                j = j < 0 ? 0 : j;
                j = j > 100 ? 100 : j;

                kValues.add(k);
                dValues.add(d);
                jValues.add(j);
            }
            for (int i = 0; i < kValues.size(); i++) {
                kEntries.add(new Entry(i, kValues.get(i)));
                dEntries.add(new Entry(i, dValues.get(i)));
                jEntries.add(new Entry(i, jValues.get(i)));
            }
        }

        List<ILineDataSet> lineDataMA = new ArrayList<>();
        LineDataSet kILineDataSet = getLineMAData(kEntries, "K", ma5Color);
        kILineDataSet.setHighlightEnabled(true);
        kILineDataSet.setHighLightColor(colorArr[0]);
        kILineDataSet.setDrawVerticalHighlightIndicator(true);
        lineDataMA.add(kILineDataSet);
        lineDataMA.add(getLineMAData(dEntries, "D", ma10Color));
        lineDataMA.add(getLineMAData(jEntries, "J", ma20Color));
        LineData lineData = new LineData(lineDataMA);
        CombinedData barCombinedData = new CombinedData();
        barCombinedData.setData(new BarData());
        barCombinedData.setData(lineData);
        barCombinedData.setData(new CandleData());
        return barCombinedData;
    }

    /**
     * 得到某区间内最高价和最低价
     *
     * @param a 开始位置 可以为0
     * @param b 结束位置
     * @return
     */
    private Float[] getHighAndLowByK(Integer a, Integer b) {
        if (a < 0) {
            a = 0;
        }
        StockKLineNodePo stockKLineNodePo = stockKLineNodePoList.get(a);
        float high = stockKLineNodePo.getHighestPrice().floatValue();
        float low = stockKLineNodePo.getLowestPrice().floatValue();
        Float[] wrs = new Float[2];
        for (int i = a; i <= b; i++) {
            stockKLineNodePo = stockKLineNodePoList.get(i);
            float currentHighPrice = stockKLineNodePo.getHighestPrice().floatValue();
            float currentLowPrice = stockKLineNodePo.getLowestPrice().floatValue();
            high = high > currentHighPrice ? high : currentHighPrice;
            low = low < currentLowPrice ? low : currentLowPrice;
        }

        wrs[0] = high;
        wrs[1] = low;
        return wrs;
    }

    /**
     * 获取RSI柱状图数据
     *
     * @return
     */
    private CombinedData getBarRSICombinedData() {
        ArrayList<Entry> firstRSIEntries = getRSIEntryList(6, 0f);
        ArrayList<Entry> secondRSIEntries = getRSIEntryList(12, 0f);
        ArrayList<Entry> thirdRSIEntries = getRSIEntryList(24, 0f);

        List<ILineDataSet> lineDataMA = new ArrayList<>();
        lineDataMA.add(getLineMAData(firstRSIEntries, "D", ma5Color));
        lineDataMA.add(getLineMAData(secondRSIEntries, "J", ma10Color));
        LineDataSet kILineDataSet = getLineMAData(thirdRSIEntries, "K", ma20Color);
        kILineDataSet.setHighlightEnabled(true);
        kILineDataSet.setHighLightColor(colorArr[0]);
        kILineDataSet.setDrawVerticalHighlightIndicator(true);
        lineDataMA.add(kILineDataSet);
        LineData lineData = new LineData(lineDataMA);
        CombinedData barCombinedData = new CombinedData();
        barCombinedData.setData(new BarData());
        barCombinedData.setData(lineData);
        barCombinedData.setData(new CandleData());
        return barCombinedData;
    }

    /**
     * 获取RSI数值列表
     *
     * @param n
     * @param defaultValue
     * @return
     */
    private ArrayList<Entry> getRSIEntryList(int n, float defaultValue) {
        ArrayList<Entry> rsiEntries = new ArrayList<>(stockKLineNodePoList.size());
        float sum = 0.0f;
        float dif = 0.0f;
        float rs = 0.0f;
        float rsi = 0.0f;
        int index = n - 1;
        if (stockKLineNodePoList != null && stockKLineNodePoList.size() > 0) {
            for (int i = 0; i < stockKLineNodePoList.size(); i++) {
                if (n == 0) {
                    sum = 0.0f;
                    dif = 0.0f;
                } else {
                    int k = i - n + 1;
                    Float[] wrs = getAAndB(k, i);
                    sum = wrs[0];
                    dif = wrs[1];
                }
                if (dif != 0) {
                    float h = sum + dif;
                    rsi = sum / h * 100;
                } else {
                    rsi = 100;
                }

                if (i < index) {
                    rsi = defaultValue;
                }
                rsiEntries.add(new Entry(i, rsi));
            }
        }
        return rsiEntries;
    }

    /**
     * 获取证跌幅
     *
     * @param a
     * @param b
     * @return
     */
    private Float[] getAAndB(Integer a, Integer b) {
        if (a < 0) {
            a = 0;
        }
        float sum = 0.0f;
        float dif = 0.0f;
        float closeT, closeY;
        Float[] abs = new Float[2];
        for (int i = a; i <= b; i++) {
            if (i > a) {
                closeT = stockKLineNodePoList.get(i).getClosePrice().floatValue();
                closeY = stockKLineNodePoList.get(i - 1).getClosePrice().floatValue();

                float c = closeT - closeY;
                if (c > 0) {
                    sum = sum + c;
                } else {
                    dif = sum + c;
                }

                dif = Math.abs(dif);
            }
        }

        abs[0] = sum;
        abs[1] = dif;
        return abs;
    }

    /**
     * 获取ma线图数据
     *
     * @param entryList
     * @param label
     * @param color
     * @return
     */
    private LineDataSet getLineMAData(List<Entry> entryList, String label, int color) {
        LineDataSet lineDataSet = new LineDataSet(entryList, label);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(color);
        lineDataSet.setLineWidth(0.6f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSet;
    }

    /**
     * 获取Boll柱状图数据
     *
     * @return
     */
    private CombinedData getBarBollCombinedData() {
        float ma = 0.0f;
        float md = 0.0f;
        float mb = 0.0f;
        float up = 0.0f;
        float dn = 0.0f;
        int boolNum = 26;
        float initValue = 0;

        int dataLen = stockKLineNodePoList.size();
        ArrayList<Float> ups = new ArrayList<>(dataLen);
        ArrayList<Float> mbs = new ArrayList<>(dataLen);
        ArrayList<Float> dns = new ArrayList<>(dataLen);

        if (stockKLineNodePoList != null && dataLen > 0) {
            float closeSum = 0.0f;
            float sum = 0.0f;
            int index = 0;
            int index2 = boolNum - 1;
            for (int i = 0; i < dataLen; i++) {
                float closePrice = stockKLineNodePoList.get(i).getClosePrice().floatValue();
                int k = i - boolNum + 1;
                if (i >= boolNum) {
                    index = boolNum;
                } else {
                    index = i + 1;
                }
                closeSum = getSumClose(k, i);
                ma = closeSum / index;
                sum = getSum(k, i, ma);
                md = (float) Math.sqrt(sum / index);
                mb = ((closeSum - closePrice) / (index - 1));
                up = mb + (2 * md);
                dn = mb - (2 * md);

                if (i < index2) {
                    mb = initValue;
                    up = initValue;
                    dn = initValue;
                }
                ups.add(up);
                mbs.add(mb);
                dns.add(dn);
            }
        }

        ArrayList<Entry> upEntries = new ArrayList<>(dataLen);
        ArrayList<Entry> mbEntries = new ArrayList<>(dataLen);
        ArrayList<Entry> dnEntries = new ArrayList<>(dataLen);

        for (int i = 0; i < ups.size(); i++) {
            upEntries.add(new Entry(i, ups.get(i)));
            mbEntries.add(new Entry(i, mbs.get(i)));
            dnEntries.add(new Entry(i, dns.get(i)));
        }

        List<ILineDataSet> lineDataMA = new ArrayList<>();
        LineDataSet kILineDataSet = getLineMAData(dnEntries, "K", ma20Color);
        kILineDataSet.setHighlightEnabled(true);
        kILineDataSet.setHighLightColor(colorArr[0]);
        kILineDataSet.setDrawVerticalHighlightIndicator(true);
        lineDataMA.add(kILineDataSet);
        lineDataMA.add(getLineMAData(upEntries, "up", ma5Color));
        lineDataMA.add(getLineMAData(mbEntries, "mb", ma10Color));
        LineData lineData = new LineData(lineDataMA);

        CombinedData barCombinedData = new CombinedData();
        barCombinedData.setData(new BarData());
        barCombinedData.setData(lineData);
        barCombinedData.setData(getLineCandleData());
        return barCombinedData;
    }

    /**
     * 获取累计收盘价
     *
     * @param a
     * @param b
     * @return
     */
    private Float getSumClose(Integer a, Integer b) {
        if (a < 0) {
            a = 0;
        }

        float close = 0.0f;
        for (int i = a; i <= b; i++) {
            float closePrice = stockKLineNodePoList.get(i).getClosePrice().floatValue();
            close += closePrice;
        }

        return close;
    }

    /**
     * 获取收盘价与均价的差异累计
     *
     * @param a
     * @param b
     * @param ma
     * @return
     */
    private Float getSum(Integer a, Integer b, Float ma) {
        if (a < 0) {
            a = 0;
        }
        float sum = 0.0f;
        for (int i = a; i <= b; i++) {
            float closePrice = stockKLineNodePoList.get(i).getClosePrice().floatValue();
            sum += ((closePrice - ma) * (closePrice - ma));
        }
        return sum;
    }

    @Override
    public void dataVisibleScope(float startPos, float endPos) {
        visibleEndPos = endPos;
        if (startPos < 20 && startPos > 0) {
            loadMoreData();
        }
    }

    @Override
    public String getLabel(float pos) {
        return DateUtil.dateStrFormatChange(
                stockKLineNodePoList.get((int) pos).getDt(),
                DateUtil.DATE_FORMAT_1,
                DateUtil.DATE_FORMAT_3
        );
    }

    /**
     * 获取提示文案
     *
     * @param pos
     * @param sign
     * @return
     */
    @Override
    public String getMarkerViewText(float pos, String sign) {
        if ("line".equals(sign)) {
            return getLineTextViewText(pos);
        } else {
            return getBarTextViewText(pos);
        }
    }

    /**
     * 获取线图提示文案
     *
     * @param pos
     * @return
     */
    private String getLineTextViewText(float pos) {
        StockKLineNodePo stockKLineNodePo = stockKLineNodePoList.get((int) pos);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getLabel(pos));
        stringBuffer.append(" ");
        stringBuffer.append("收:");
        stringBuffer.append(stockKLineNodePo.getClosePrice());
        stringBuffer.append(" ");
        CombinedData combinedData = lineChart.getCombinedData();
        LineData lineData = combinedData.getLineData();
        for (int i = 0; i < lineData.getDataSetCount(); i++) {
            LineDataSet lineDataSet = (LineDataSet) lineData.getDataSetByIndex(i);
            List<Entry> entryList = lineDataSet.getEntriesForXValue(pos);
            if (entryList.size() > 0) {
                stringBuffer.append(lineDataSet.getLabel());
                stringBuffer.append(":");
                stringBuffer.append(entryList.get(0).getY());
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 获取柱图提示文案
     *
     * @param pos
     * @return
     */
    private String getBarTextViewText(float pos) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getLabel(pos));
        stringBuffer.append(" ");
        CombinedData combinedData = barChart.getCombinedData();
        BarData barData = combinedData.getBarData();
        for (int i = 0; i < barData.getDataSetCount(); i++) {
            BarDataSet barDataSet = (BarDataSet) barData.getDataSetByIndex(i);
            List<BarEntry> entryList = barDataSet.getEntriesForXValue(pos);
            if (entryList.size() > 0) {
                stringBuffer.append(barDataSet.getLabel());
                stringBuffer.append(":");
                stringBuffer.append(entryList.get(0).getY());
                stringBuffer.append(" ");
            }
        }
        LineData lineData = combinedData.getLineData();
        for (int i = 0; i < lineData.getDataSetCount(); i++) {
            LineDataSet lineDataSet = (LineDataSet) lineData.getDataSetByIndex(i);
            List<Entry> entryList = lineDataSet.getEntriesForXValue(pos);
            if (entryList.size() > 0) {
                stringBuffer.append(lineDataSet.getLabel());
                stringBuffer.append(":");
                stringBuffer.append(entryList.get(0).getY());
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();
    }
}
