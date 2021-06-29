package com.fox.stockhelperchart.renderer.yaxis;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockBarYAxisRenderer extends YAxisRenderer {
    protected String[] labelArr = new String[0];

    public StockBarYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }

    public void setLabels(String[] labels) {
        labelArr = labels;
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        float xOffset = mYAxis.getLabelXOffset();
        float yOffset;
        // draw
        for (int i = from; i < to; i += 1) {
            String text = i < labelArr.length ? labelArr[i] : "";
            if (i == 0) {
                yOffset = -offset;
            } else if (i == to - 1) {
                yOffset = 4 * offset;
            } else {
                yOffset = offset;
            }
            c.drawText(text,
                    fixedPosition + xOffset,
                    positions[i * 2 + 1] + yOffset,
                    mAxisLabelPaint);
        }
    }
}
