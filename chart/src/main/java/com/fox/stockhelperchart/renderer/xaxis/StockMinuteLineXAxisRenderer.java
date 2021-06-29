package com.fox.stockhelperchart.renderer.xaxis;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockMinuteLineXAxisRenderer extends StockXAxisRenderer {
    /**
     * 网格线位置
     */
    private int[] gradLinePos = new int[]{0, 61, 121, 181, 241};
    /**
     * 坐标显示的位置
     */
    private int[] labelPos = new int[]{0, 61, 121, 181, 241};

    public StockMinuteLineXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    public void setGradLinePos(int[] pos) {
        gradLinePos = pos;
    }

    public void setLabelPos(int[] labelPosTree) {
        labelPos = labelPosTree;
    }

    /**
     * Computes the axis values.
     *
     * @param min - the minimum value in the data object for this axis
     * @param max - the maximum value in the data object for this axis
     */
    public void computeAxis(float min, float max, boolean inverted) {
        mXAxis.mEntries = new float[labelPos.length];
        for (int i = 0; i < labelPos.length; i++) {
            mXAxis.mEntries[i] = (float)labelPos[i];
        }
        mAxis.mCenteredEntries = new float[]{};
        mAxis.mEntryCount = mAxis.mEntries.length;
    }

    @Override
    protected float[] getGridLinePos() {
        float[] positions = new float[gradLinePos.length * 2];

        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = gradLinePos[i / 2];
            positions[i + 1] = gradLinePos[i / 2];
        }
        return positions;
    }
}
