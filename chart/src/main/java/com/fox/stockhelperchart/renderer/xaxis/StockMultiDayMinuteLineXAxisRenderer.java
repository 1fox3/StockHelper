package com.fox.stockhelperchart.renderer.xaxis;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockMultiDayMinuteLineXAxisRenderer extends StockMinuteLineXAxisRenderer {
    public StockMultiDayMinuteLineXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }
}
