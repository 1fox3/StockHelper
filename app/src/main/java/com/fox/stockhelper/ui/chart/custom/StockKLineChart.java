package com.fox.stockhelper.ui.chart.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.fox.stockhelper.ui.listener.ListChooseListener;
import com.github.mikephil.charting.stockChart.KLineChart;

import androidx.annotation.Nullable;

/**
 * @author lusongsong
 * @date 2020/10/1 18:14
 */
public class StockKLineChart extends KLineChart {
    /**
     * 数据选中监听器
     */
    private ListChooseListener listChooseListener;

    public StockKLineChart(Context context) {
        super(context);
    }

    public StockKLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置数据选中监听器
     * @param listener
     */
    public void setListChooseListener(ListChooseListener listener) {
        listChooseListener = listener;
    }

    /**
     * 移动十字标更新数据
     * @param index
     * @param isSelect
     */
    public void updateText(int index, boolean isSelect) {
        super.updateText(index, isSelect);
        listChooseListener.choose(index);
    }
}
