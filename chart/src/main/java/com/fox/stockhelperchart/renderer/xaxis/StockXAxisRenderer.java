package com.fox.stockhelperchart.renderer.xaxis;

import android.graphics.Canvas;
import android.graphics.Path;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockXAxisRenderer extends XAxisRenderer {
    public StockXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    /**
     * 获取网格线位置
     * @return
     */
    protected float[] getGridLinePos() {
        float[] positions = new float[mXAxis.mEntryCount * 2];
        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = mXAxis.mEntries[i / 2];
            positions[i + 1] = mXAxis.mEntries[i / 2];
        }
        return positions;
    }

    /**
     * 画网格线，不画第一条和最后一条
     * @param c
     */
    @Override
    public void renderGridLines(Canvas c) {

        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;

        int clipRestoreCount = c.save();
        c.clipRect(getGridClippingRect());

        float[] positions = getGridLinePos();

        mTrans.pointValuesToPixel(positions);

        setupGridPaint();

        Path gridLinePath = mRenderGridLinesPath;
        gridLinePath.reset();

        //不画第一条和最后一条
        for (int i = 2; i < positions.length - 2; i += 2) {

            drawGridLine(c, positions[i], positions[i + 1], gridLinePath);
        }

        c.restoreToCount(clipRestoreCount);
    }
}
