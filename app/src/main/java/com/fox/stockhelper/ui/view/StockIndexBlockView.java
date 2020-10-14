package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * top指标组件
 * @author lusongsong
 * @date 2020-08-24 14:14
 */
public class StockIndexBlockView extends RelativeLayout {
    /**
     * 显示名称
     */
    private String name;
    /**
     * 当前价
     */
    private BigDecimal currentPrice;
    /**
     * 上个交易日收盘价
     */
    private BigDecimal preClosePrice;
    /**
     * 名称文本组件
     */
    @BindView(R.id.stockIndexName)
    TextView nameTV;
    /**
     * 当前价格文本组件
     */
    @BindView(R.id.stockIndexCurrentPrice)
    TextView currentPriceTV;
    /**
     * 价格增幅文本组件
     */
    @BindView(R.id.stockIndexUptickPrice)
    TextView uptickPriceTV;
    /**
     * 价格增幅率文本组件
     */
    @BindView(R.id.stockIndexUptickRate)
    TextView uptickRateTV;

    /**
     * 构造函数
     * @param context
     */
    public StockIndexBlockView(Context context) {
        super(context);
        this.bindLayout();
        this.initView();
    }

    public StockIndexBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockIndexBlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockIndexBlockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    /**
     * 绑定布局文件
     */
    private void bindLayout() {
        View view = LayoutInflater.from(this.getContext()).inflate(
                R.layout.view_stock_index_block, this, true
        );
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化属性
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(
                attrs, R.styleable.StockIndexBlockView
        );
        name = typedArray.getString(R.styleable.StockIndexBlockView_name);
        currentPrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockIndexBlockView_currentPrice, 0f
        ));
        preClosePrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockIndexBlockView_preClosePrice, 0f
        ));
    }

    /**
     * 初始化view
     */
    public void initView() {
        this.showName();
        this.showValue();
    }

    /**
     * 格式化数字为字符串
     * @param numberValue
     * @return
     */
    private String getNumberStr(BigDecimal numberValue) {
        numberValue = numberValue.setScale(2, BigDecimal.ROUND_HALF_UP);
        return numberValue.toString();
    }

    /**
     * 修改颜色
     * @param color
     */
    private void setTextColor(int color) {
        currentPriceTV.setTextColor(color);
        uptickPriceTV.setTextColor(color);
        uptickRateTV.setTextColor(color);
    }

    /**
     * 设置名称
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        nameTV.setText(name);
    }

    /**
     * 设置价格
     * @param currentPrice
     * @param preClosePrice
     */
    public void setPrice(BigDecimal currentPrice, BigDecimal preClosePrice) {
        this.currentPrice = currentPrice;
        this.preClosePrice = preClosePrice;
        this.showValue();
    }

    /**
     * 显示名称
     */
    public void showName() {
        nameTV.setText(null == name ? "" : name);
    }


    /**
     * 显示数值
     */
    public void showValue() {
        if (null != currentPrice && null != preClosePrice
                && 0 < currentPrice.compareTo(BigDecimal.ZERO)
                && 0 < preClosePrice.compareTo(BigDecimal.ZERO)) {
            BigDecimal uptickPrice = currentPrice.subtract(preClosePrice);
            BigDecimal uptickRate = uptickPrice.divide(preClosePrice, 4,  BigDecimal.ROUND_HALF_UP);
            int uptickType = uptickPrice.compareTo(BigDecimal.ZERO);
            currentPriceTV.setText(getNumberStr(currentPrice));
            uptickPriceTV.setText((1 == uptickType ? "+" : "") + getNumberStr(uptickPrice));
            uptickRateTV.setText((1 == uptickType ? "+" : "") + getNumberStr(uptickRate.multiply(new BigDecimal(100))) + "%");
            if (0 < uptickType) {
                this.setTextColor(this.getContext().getColor(R.color.up));
            } else if (0 > uptickType) {
                this.setTextColor(this.getContext().getColor(R.color.down));
            } else {
                this.setTextColor(this.getContext().getColor(R.color.flat));
            }
        }
    }
}
