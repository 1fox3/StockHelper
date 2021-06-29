package com.fox.stockhelperchart.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.TreeMap;

/**
 * @author lusongsong
 * @date 2021/3/15 18:01
 */
public class StockXAxisFormatter implements IAxisValueFormatter {
    private TreeMap<Integer, String> labelMap = new TreeMap<Integer, String>() {
        {
            put(0, "9:30");
            put(61, "10:30");
            put(121, "11:30/13:00");
            put(181, "14:00");
            put(241, "15:00");
        }
    };

    public StockXAxisFormatter() {
    }

    public void setLabels(TreeMap<Integer, String> labels) {
        labelMap = labels;
    }

    public String getFormattedValue(float value, AxisBase axis) {
        return labelMap.get((int)value);
    }

}
