package com.fox.stockhelperchart.renderer.yaxis;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Path;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockMinuteLineYAxisRenderer extends StockYAxisRenderer {
    protected boolean isLabelValueInside = true;
    protected float flatValue;
    protected int[] labelColorArr;
    protected int labelStep = 2;

    public StockMinuteLineYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        setDrawMiddleGridLine(false);
    }

    public void setLabelValueInside(boolean enabled) {
        isLabelValueInside = enabled;
    }

    public void setLabelColorArr(int[] colorArr) {
        labelColorArr = colorArr;
    }

    public void setFlatValue(float value) {
        flatValue = value;
    }

    public int getLabelColor(int pos) {
        float labelValue = mYAxis.mEntries[pos];
        int colorPos = 2;
        if (labelValue > flatValue) {
            colorPos = 0;
        } else if (labelValue < flatValue) {
            colorPos = 1;
        } else {
            colorPos = 2;
        }

        return colorPos < labelColorArr.length ? labelColorArr[colorPos] : 0;
    }

    public void setLabelStep(int ls) {
        labelStep = ls;
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        float xOffset = mYAxis.getLabelXOffset();
        float yOffset;
        int oriLabelColor = mAxisLabelPaint.getColor();
        int labelColor;
        // draw
        for (int i = from; i < to; i += labelStep) {
            String text = mYAxis.getFormattedLabel(i);
            if (i == 0) {
                yOffset = -offset;
            } else if (i == to - 1) {
                yOffset = 4 * offset;
            } else {
                yOffset = offset;
            }
            labelColor = getLabelColor(i);
            labelColor = 0 == labelColor ? oriLabelColor : labelColor;
            mAxisLabelPaint.setColor(labelColor);
            c.drawText(text,
                    fixedPosition + xOffset,
                    positions[i * 2 + 1] + yOffset,
                    mAxisLabelPaint);
        }
    }

    /**
     * Draws the zero line.
     */
    @Override
    protected void drawZeroLine(Canvas c) {

        int clipRestoreCount = c.save();
        mZeroLineClippingRect.set(mViewPortHandler.getContentRect());
        mZeroLineClippingRect.inset(0.f, -mYAxis.getZeroLineWidth());
        c.clipRect(mZeroLineClippingRect);

        // draw zero line
        MPPointD pos = mTrans.getPixelForValues(0f, 0f);

        mZeroLinePaint.setColor(mYAxis.getZeroLineColor());
        mZeroLinePaint.setStrokeWidth(mYAxis.getZeroLineWidth());
        mZeroLinePaint.setPathEffect(
                new DashPathEffect(new float[]{10, 10}, 0)
        );

        Path zeroLinePath = mDrawZeroLinePath;
        zeroLinePath.reset();

        zeroLinePath.moveTo(mViewPortHandler.contentLeft(), (float) pos.y);
        zeroLinePath.lineTo(mViewPortHandler.contentRight(), (float) pos.y);

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(zeroLinePath, mZeroLinePaint);

        c.restoreToCount(clipRestoreCount);
    }
}
