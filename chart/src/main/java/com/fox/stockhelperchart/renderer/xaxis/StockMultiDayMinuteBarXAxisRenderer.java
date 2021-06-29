package com.fox.stockhelperchart.renderer.xaxis;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StockMultiDayMinuteBarXAxisRenderer extends StockMinuteBarXAxisRenderer {
    public StockMultiDayMinuteBarXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }
}
