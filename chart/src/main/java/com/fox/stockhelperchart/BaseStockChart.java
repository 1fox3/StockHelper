package com.fox.stockhelperchart;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fox.spider.stock.entity.vo.StockVo;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.List;

/**
 * 股票图表基类
 *
 * @author lusongsong
 * @date 2021/2/26 16:08
 */
public class BaseStockChart extends LinearLayout {
    /**
     * X轴数据量
     */
    public static int X_NODE_COUNT = 241;
    /**
     * Y轴数据量
     */
    public static int Y_NODE_COUNT = 130;
    /**
     * X轴默认显示的刻度数
     */
    public static int X_LABEL_COUNT = 5;
    /**
     * 线图Y轴默认显示的刻度数
     */
    public static int LINE_Y_LABEL_COUNT = 5;
    /**
     * 柱图Y轴默认显示的刻度数
     */
    public static int BAR_Y_LABEL_COUNT = 3;
    /**
     * Y轴左侧最大值
     */
    public static int LEFT_Y_VALUE_MAX = 100;
    /**
     * Y轴左侧最小值
     */
    public static int LEFT_Y_VALUE_MIN = 80;
    /**
     * Y轴右侧最大值
     */
    public static int RIGHT_Y_VALUE_MAX = 10;
    /**
     * Y轴右侧最小值
     */
    public static int RIGHT_Y_VALUE_MIN = -10;
    /**
     * 无数据默认显示的字符串
     */
    public static final String NO_DATA_STR = "加载中...";
    /**
     * 图表价格线图名称
     */
    public static final String CHART_LABEL_PRICE_LINE = "价格";
    /**
     * 图表均价线图名称
     */
    public static final String CHART_LABEL_AVG_PRICE_LINE = "均价";
    /**
     * 图表成交量柱图名称
     */
    public static final String CHART_LABEL_DEAL_NUM_BAR = "成交量";
    /**
     * 长色
     */
    protected int upColor;
    /**
     * 降色
     */
    protected int downColor;
    /**
     * 平色
     */
    protected int flatColor;
    /**
     * 颜色数组
     */
    protected int[] colorArr;
    /**
     * ma颜色数组
     */
    protected int[] maColorArr;
    /**
     * 图标边框颜色
     */
    protected int borderColor;
    /**
     * 0线颜色
     */
    protected int zeroLineColor;
    /**
     * 价格线颜色
     */
    protected int priceLineColor;
    /**
     * 价格均值线颜色
     */
    protected int avgPriceLineColor;
    protected int ma5Color;
    protected int ma10Color;
    protected int ma20Color;
    /**
     * 高亮颜色
     */
    protected int highlightColor;
    /**
     * 线图X轴
     */
    XAxis lineX;
    /**
     * 线图左Y轴
     */
    YAxis lineLeftY;
    /**
     * 线图右Y轴
     */
    YAxis lineRightY;
    /**
     * 柱图X轴
     */
    XAxis barX;
    /**
     * 柱图左Y轴
     */
    YAxis barLeftY;
    /**
     * 柱图右Y轴
     */
    YAxis barRightY;

    /**
     * 股票对象
     */
    StockVo stockVo;

    public BaseStockChart(Context context) {
        super(context);
    }

    public BaseStockChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseStockChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseStockChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void initChart() {
        Context context = getContext();
        upColor = ContextCompat.getColor(context, R.color.stockUp);
        downColor = ContextCompat.getColor(context, R.color.stockDown);
        flatColor = ContextCompat.getColor(context, R.color.stockFlat);
        colorArr = new int[]{upColor, downColor, flatColor,};
        maColorArr = new int[]{
                ContextCompat.getColor(context, R.color.ma5),
                ContextCompat.getColor(context, R.color.ma10),
                ContextCompat.getColor(context, R.color.ma20),
                ContextCompat.getColor(context, R.color.ma30),
        };
        borderColor = ContextCompat.getColor(context, R.color.chartBorder);
        zeroLineColor = ContextCompat.getColor(context, R.color.zeroLine);
        priceLineColor = ContextCompat.getColor(context, R.color.priceLine);
        avgPriceLineColor = ContextCompat.getColor(context, R.color.avgPriceLine);
        ma5Color = ContextCompat.getColor(context, R.color.ma5);
        ma10Color = ContextCompat.getColor(context, R.color.ma10);
        ma20Color = ContextCompat.getColor(context, R.color.ma20);
        highlightColor = ContextCompat.getColor(context, R.color.highlight);
    }


    /**
     * 设置股票
     *
     * @param stock
     */
    public void setStockVo(StockVo stock) {
        stockVo = stock;
    }

    /**
     * 获取随机值
     *
     * @param min
     * @param max
     * @return
     */
    protected float random(double min, double max) {
        return (float) (Math.random() * (max - min) + min);
    }

    /**
     * 获取提示文案
     *
     * @param markerInfoList
     * @return
     */
    protected String getMarkerViewStr(List<Object> markerInfoList) {
        if (null == markerInfoList || markerInfoList.isEmpty()) {
            return "";
        }
        StringBuilder markerBuilder = new StringBuilder();
        for (int i = 0; i < markerInfoList.size(); i++) {
            Object infoObj = markerInfoList.get(i);
            if (0 != i) {
                markerBuilder.append(" ");
            }
            if (infoObj instanceof List) {
                markerBuilder.append((String)(((List) infoObj).get(0)));
                markerBuilder.append(":");
                markerBuilder.append((String)(((List) infoObj).get(1)));
            } else {
                markerBuilder.append((String) infoObj);
            }
        }
        return markerBuilder.toString();
    }
}
