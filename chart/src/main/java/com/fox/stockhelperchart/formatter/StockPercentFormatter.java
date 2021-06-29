package com.fox.stockhelperchart.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * 股票百分比格式器
 *
 * @author lusongsong
 * @date 2021/3/15 15:38
 */
public class StockPercentFormatter implements IValueFormatter, IAxisValueFormatter {
    /**
     * 数值个数化
     */
    protected DecimalFormat mFormat;
    /**
     * 小数点位数
     */
    protected int decimalDigits = 2;
    /**
     * 数值是否需要格式化
     */
    protected boolean numberFormatter = true;
    /**
     * 数值是分隔符
     */
    protected String numberSeparator = ",";
    /**
     * 百分比单位后缀
     */
    protected String percentSuffix = "%";
    /**
     * 是否显示百分号
     */
    protected boolean showPercentSuffix = true;
    /**
     * 是否显示正号
     */
    protected boolean showPlusSign = true;

    /**
     * 无参构造函数
     */
    public StockPercentFormatter() {
        initFormatter();
    }

    /**
     * 制定格式器的构造函数
     *
     * @param format
     */
    public StockPercentFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    /**
     * 设置小数点位数
     *
     * @param dd
     * @return
     */
    public StockPercentFormatter setDecimalDigits(int dd) {
        decimalDigits = dd >= 0 ? dd : decimalDigits;
        return this;
    }

    /**
     * 整数值是否需要格式化
     *
     * @param nf
     * @return
     */
    public StockPercentFormatter setNumberFormatter(boolean nf) {
        numberFormatter = nf;
        return this;
    }

    /**
     * 设置分隔符
     *
     * @param ns
     * @return
     */
    public StockPercentFormatter setNumberSeparator(String ns) {
        numberSeparator = ns;
        return this;
    }

    /**
     * 百分数单位后缀
     *
     * @param ps
     * @return
     */
    public StockPercentFormatter setPercentSuffix(String ps) {
        percentSuffix = ps;
        return this;
    }

    /**
     * 是否显示百分数单位
     *
     * @param sps
     * @return
     */
    public StockPercentFormatter setShowPercentSuffix(boolean sps) {
        showPercentSuffix = sps;
        return this;
    }

    /**
     * 是指是否显示正号
     *
     * @param sps
     * @return
     */
    public StockPercentFormatter setShowPlusSign(boolean sps) {
        showPlusSign = sps;
        return this;
    }

    /**
     * 初始化格式器
     *
     * @return
     */
    public StockPercentFormatter initFormatter() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i <= 2; i++) {
            if (i < 2) {
                stringBuffer.append("###");
                stringBuffer.append(numberSeparator);
            } else {
                stringBuffer.append("##0");
            }
        }
        if (decimalDigits > 0) {
            stringBuffer.append('.');
        }
        for (int i = 0; i < decimalDigits; i++) {
            stringBuffer.append('0');
        }
        mFormat = new DecimalFormat(stringBuffer.toString());
        return this;
    }

    /**
     * 获取正号
     *
     * @param value
     * @return
     */
    private String getPlusSignStr(float value) {
        return value > 0 ? "+" : "";
    }

    /**
     * 获取单位文案
     *
     * @return
     */
    private String getUnitStr() {
        return showPercentSuffix ? percentSuffix : "";
    }

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return getPlusSignStr(value) + mFormat.format(value) + getUnitStr();
    }

    /**
     * Called when a value (from labels inside the chart) is formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value           the value to be formatted
     * @param entry           the entry the value belongs to - in e.g. BarChart, this is of class BarEntry
     * @param dataSetIndex    the index of the DataSet the entry in focus belongs to
     * @param viewPortHandler provides information about the current chart state (scale, translation, ...)
     * @return the formatted label ready for being drawn
     */
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return getPlusSignStr(value) + mFormat.format(value) + getUnitStr();
    }
}
