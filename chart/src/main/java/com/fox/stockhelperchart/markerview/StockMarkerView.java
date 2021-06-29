package com.fox.stockhelperchart.markerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.widget.TextView;

import com.fox.stockhelperchart.R;
import com.fox.stockhelperchart.listener.StockKLineMarkerViewTextListener;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * 股票文案提示
 *
 * @author lusongsong
 * @date 2021/3/25 17:13
 */
public class StockMarkerView extends MarkerView {
    /**
     * 提示文案数组
     */
    String[] markerStrArr = null;
    /**
     * 提示文案回调
     */
    StockKLineMarkerViewTextListener stockKLineMarkerViewTextListener = null;
    String sign = "";
    /**
     * 提示文案的显示组件
     */
    TextView markerViewStrTV;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public StockMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        markerViewStrTV = findViewById(R.id.markerViewStrTV);
    }

    /**
     * 设置提示文案
     *
     * @param markerStrArr
     */
    public void setMarkerStrArr(String[] markerStrArr) {
        this.markerStrArr = markerStrArr;
    }

    /**
     * 设置提示文案回调
     *
     * @param stockKLineMarkerViewTextListener
     */
    public void setStockKLineMarkerViewTextListener(StockKLineMarkerViewTextListener stockKLineMarkerViewTextListener, String sign) {
        this.stockKLineMarkerViewTextListener = stockKLineMarkerViewTextListener;
        this.sign = sign;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String markerViewText = "";
        if (null != markerStrArr && markerStrArr.length > 0) {
            markerViewText = markerStrArr[(int) e.getX()];
        } else if (null != stockKLineMarkerViewTextListener) {
            markerViewText = stockKLineMarkerViewTextListener.getMarkerViewText(e.getX(), sign);
        } else {
            ChartData chartData = (ChartData) getChartView().getData();
            int setCount = chartData.getDataSetCount();
            for (int i = 0; i < setCount; i++) {
                DataSet dataSet = (DataSet) chartData.getDataSetByIndex(i);
                markerViewText += dataSet.getLabel() + "" + dataSet.getEntryForIndex((int) e.getX()).getY();
            }
        }
        markerViewStrTV.setText(markerViewText);
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        MPPointF mpPointF = getOffset();

        int saveId = canvas.save();
        // translate to the correct position and draw
        canvas.translate(mpPointF.x, mpPointF.y);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }

    @Override
    public MPPointF getOffset() {
        RectF rectF = getChartView().getViewPortHandler().getContentRect();
        return new MPPointF(
                (rectF.left + rectF.right) / 2 - (int) (getWidth() / 2),
                rectF.top
        );
    }
}
