package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.fox.stockhelper.R;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class StockValueTextView extends androidx.appcompat.widget.AppCompatTextView {
    /**
     * 数值计量单位范围
     */
    private static final Map<Double, String> NUMBER_SCOPE =
            new HashMap<Double, String>(2)
            {{ put(10000.0, "万");put(100000000.0, "亿"); }};
    /**
     * 数值
     */
    private static final int TYPE_NUMBER = 0;
    /**
     * 百分比
     */
    private static final int TYPE_RATE = 1;
    /**
     * 计量单位数字
     */
    private static final int UNIT_NUMBER = 0;
    /**
     * 计量单位手
     */
    private static final int UNIT_HAND = 1;
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
    }

    /**
     * 初始化显示
     */
    private void initView() {
        String valueStr = this.getValueStr();
        if (null != valueStr || 0 < valueStr.length()) {
            this.setText(valueStr);
        }
        if (null != value) {
            if (0.0 < value) {
                this.setTextColor(RED);
            }else if(0.0 > value) {
                this.setTextColor(GREEN);
            } else {
                this.setTextColor(GRAY);
            }
        }
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
        //处理计量单位对数值的影响
        if (valueStr.length() > 0) {
            switch (unit) {
                case UNIT_HAND:
                    valueStr += "手";
                    break;
            }
        }

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
        Double scopeNum = 1.0;
        for (Double numberScope : NUMBER_SCOPE.keySet()) {
            if (numberValue >= numberScope) {
                scopeNum = numberScope;
                scopeStr = NUMBER_SCOPE.get(numberScope);
            } else {
                break;
            }
        }
        numberValue /= scopeNum;
        stringBuffer.append(String.format("%.2f", numberValue));
        stringBuffer.append(scopeStr);
        return stringBuffer.toString();
    }
}
