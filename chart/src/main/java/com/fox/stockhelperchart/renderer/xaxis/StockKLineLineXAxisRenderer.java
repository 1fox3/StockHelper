package com.fox.stockhelperchart.renderer.xaxis;

import android.graphics.Canvas;

import com.fox.stockhelperchart.listener.StockKLineDataVisibleListener;
import com.fox.stockhelperchart.listener.StockKLineDateLabelListener;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockKLineLineXAxisRenderer extends StockXAxisRenderer {
    StockKLineDataVisibleListener stockKLineDataVisibleListener;
    StockKLineDateLabelListener stockKLineDateLabelListener;
    public StockKLineLineXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    public void setStockKLineDataVisibleListener(StockKLineDataVisibleListener listener) {
        stockKLineDataVisibleListener = listener;
    }

    public void setStockKLineDateLabelListener(StockKLineDateLabelListener listener) {
        stockKLineDateLabelListener = listener;
    }

    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();
        float[] positions = new float[mXAxis.mEntryCount * 2];
        for (int i = 0; i < positions.length; i += 2) {
            // only fill x values
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }
        mTrans.pointValuesToPixel(positions);
        float startScope = 0, endScope = 0;
        for (int i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            if (mViewPortHandler.isInBoundsX(x)) {
                String label = stockKLineDateLabelListener.getLabel(mXAxis.mEntries[i / 2]);
                if (i == 0) {
                    startScope = mXAxis.mEntries[i / 2];
                }
                endScope = mXAxis.mEntries[i / 2];
                float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                if (0 == i) {
                    x += width / 2;
                } else if (i >= positions.length - 2) {
                    x -= width / 2;
                }
                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
            }
        }
        stockKLineDataVisibleListener.dataVisibleScope(startScope, endScope);
    }
}
