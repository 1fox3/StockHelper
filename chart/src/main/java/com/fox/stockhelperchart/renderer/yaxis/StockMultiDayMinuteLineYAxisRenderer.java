package com.fox.stockhelperchart.renderer.yaxis;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockMultiDayMinuteLineYAxisRenderer extends StockMinuteLineYAxisRenderer {
    public StockMultiDayMinuteLineYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }
}
