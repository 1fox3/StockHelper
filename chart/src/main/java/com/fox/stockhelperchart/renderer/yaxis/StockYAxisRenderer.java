package com.fox.stockhelperchart.renderer.yaxis;

import android.graphics.Canvas;
import android.graphics.Path;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockYAxisRenderer extends YAxisRenderer {
    /**
     * 是否画中间的网格线
     */
    private boolean drawMiddleGridLine = true;

    public StockYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }

    /**
     * 设置是否画中间的网格线
     * @param isDraw
     */
    public void setDrawMiddleGridLine(boolean isDraw) {
        drawMiddleGridLine = isDraw;
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
        for (int i = from; i < to; i++) {

            String text = mYAxis.getFormattedLabel(i);

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

    @Override
    public void renderGridLines(Canvas c) {

        if (!mYAxis.isEnabled())
            return;

        if (mYAxis.isDrawGridLinesEnabled()) {

            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());

            float[] positions = getTransformedPositions();

            mGridPaint.setColor(mYAxis.getGridColor());
            mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
            mGridPaint.setPathEffect(mYAxis.getGridDashPathEffect());

            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();

            // draw the grid 不显示第一条和最后一条
            int start = mYAxis.isForceLabelsEnabled() ? 2 : 0;
            int end = mYAxis.isForceLabelsEnabled() ? positions.length - 2 : positions.length;
            for (int i = start; i < end; i += 2) {
                //不画中间的线
                if (!drawMiddleGridLine && i == (int) ((positions.length - 1) / 2)) {
                    continue;
                }
                // draw a path because lines don't support dashing on lower android versions
                c.drawPath(linePath(gridLinePath, i, positions), mGridPaint);
                gridLinePath.reset();
            }

            c.restoreToCount(clipRestoreCount);
        }

        if (mYAxis.isDrawZeroLineEnabled()) {
            drawZeroLine(c);
        }
    }
}
