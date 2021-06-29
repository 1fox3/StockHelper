package com.fox.stockhelperchart.renderer.xaxis;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author lusongsong
 * @date 2021/3/15 17:04
 */
public class StockSingleDayMinuteLineXAxisRenderer extends StockMinuteLineXAxisRenderer {
    public StockSingleDayMinuteLineXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
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
        for (int i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            if (mViewPortHandler.isInBoundsX(x)) {
                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
                float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                if (0 == i) {
                    x += width / 2;
                } else if (i >= positions.length - 2) {
                    x -= width / 2;
                }
                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
            }
        }
    }
}
