package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.fox.stockhelper.R;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class StockValueTextView extends androidx.appcompat.widget.AppCompatTextView {
    /**
     * 数值计量单位范围
     */
    private static final Map<Long, String> NUMBER_SCOPE = new HashMap<Long, String>(3) {
        {
            put(10000L, "万");put(100000000L, "亿");put(1000000000000L, "万亿");
        }
    };
    /**
     * 数值
     */
    public static final int TYPE_NUMBER = 0;
    /**
     * 百分比
     */
    public static final int TYPE_RATE = 1;
    /**
     * 计量单位数字
     */
    public static final int UNIT_NUMBER = 0;
    /**
     * 计量单位手
     */
    public static final int UNIT_HAND = 1;
    /**
     * 增幅类型平
     */
    public static final int UPTICK_TYPE_FLAT = 0;
    /**
     * 增幅类型涨
     */
    public static final int UPTICK_TYPE_UP = 1;
    /**
     * 增幅类型跌
     */
    public static final int UPTICK_TYPE_DOWN = 2;
    /**
     * 小数点精度
     */
    private int precisionLen = 2;
    /**
     * 数值
     */
    private Double value;
    /**
     * 数值类型
     */
    private int type;
    /**
     * 计量单位
     */
    private int unit;
    /**
     * 增幅类型
     */
    private int uptickType;

    /**
     * 构造函数
     * @param context
     */
    public StockValueTextView(Context context) {
        super(context);
        this.initView();
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public StockValueTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initAttrs(attrs);
        this.initView();
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public StockValueTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initAttrs(attrs);
        this.initView();
    }

    /**
     * 初始化属性设置
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(
                attrs, R.styleable.StockValueTextView
        );
        String valueStr = typedArray.getString(R.styleable.StockValueTextView_value);
        if (null != valueStr) {
            value = Double.valueOf(valueStr);
        }
        type = typedArray.getInt(
                R.styleable.StockValueTextView_type,
                StockValueTextView.TYPE_NUMBER
        );
        unit = typedArray.getInt(
                R.styleable.StockValueTextView_unit,
                StockValueTextView.UNIT_NUMBER
        );
        uptickType = typedArray.getInt(
                R.styleable.StockValueTextView_uptickType,
                StockValueTextView.UPTICK_TYPE_FLAT
        );
    }

    /**
     * 设置数值
     * @param value
     * @param type
     * @param unit
     */
    public StockValueTextView setValue(double value, int type, int unit) {
        this.value = value;
        this.type = type;
        this.unit = unit;
        return this;
    }

    /**
     * 设置数值
     * @param value
     */
    public StockValueTextView setValue(double value) {
        this.value = value;
        return this;
    }

    /**
     * 设置增幅类型
     * @param uptickType
     * @return
     */
    public StockValueTextView setUptickType(int uptickType) {
        this.uptickType = uptickType;
        return this;
    }

    /**
     * 重新绘画
     */
    public void reDraw() {
        initView();
    }

    /**
     * 初始化显示
     */
    private void initView() {
        if (unit == UNIT_HAND) {
            precisionLen = 0;
        }
        String valueStr = this.getValueStr();
        if (null != valueStr || 0 < valueStr.length()) {
            this.setText(valueStr);
        }
        if (type == TYPE_RATE) {
            if (value > 0) {
                uptickType = UPTICK_TYPE_UP;
            } else if (value < 0) {
                uptickType = UPTICK_TYPE_DOWN;
            } else {
                uptickType = UPTICK_TYPE_FLAT;
            }
        }
        int color;
        switch (uptickType) {
            case UPTICK_TYPE_UP:
                color = R.color.up;
                break;
            case UPTICK_TYPE_DOWN:
                color = R.color.down;
                break;
            default:
                color = R.color.flat;
                break;
        }
        this.setTextColor(this.getContext().getColor(color));
    }

    /**
     * 获取显示文案
     * @return
     */
    private String getValueStr() {
        String valueStr = "";
        Double currentValue = value;
        if (null == value) {
            return valueStr;
        }
        //处理计量单位对数值的影响
        switch (unit) {
            case UNIT_HAND:
                currentValue = currentValue / 100;
                break;
        }
        //处理数值类型
        switch (type) {
            case TYPE_RATE:
                valueStr = String.format("%.2f", currentValue * 100) + "%";
                break;
            case TYPE_NUMBER:
                valueStr = this.getNumberStr(currentValue);
                break;
        }
//        //处理计量单位对数值的影响
//        if (valueStr.length() > 0) {
//            switch (unit) {
//                case UNIT_HAND:
//                    valueStr += "手";
//                    break;
//            }
//        }
        return valueStr;
    }

    /**
     * 获取数值字符串
     * @param numberValue
     * @return
     */
    private String getNumberStr(Double numberValue) {
        StringBuffer stringBuffer = new StringBuffer();
        if (numberValue < 0.0) {
            stringBuffer.append("-");
        }
        numberValue = Math.abs(numberValue);
        String scopeStr = "";
        Long scopeNum = 1L;
        for (Long numberScope : NUMBER_SCOPE.keySet()) {
            if (numberValue >= numberScope) {
                scopeNum = numberScope;
                scopeStr = NUMBER_SCOPE.get(numberScope);
            } else {
                break;
            }
        }
        numberValue /= scopeNum;
        stringBuffer.append(String.format("%." + precisionLen + "f", numberValue));
        stringBuffer.append(scopeStr);
        return stringBuffer.toString();
    }
}
