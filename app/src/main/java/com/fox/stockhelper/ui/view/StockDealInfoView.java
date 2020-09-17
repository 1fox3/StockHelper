package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fox.stockhelper.R;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股票交易信息
 * @author lusongsong
 * @date 2020/9/16 14:41
 */
public class StockDealInfoView extends LinearLayout {
    /**
     * 当前价格
     */
    private float currentPrice = 0;
    /**
     * 上个交易日收盘价
     */
    private float yesterdayClosePrice = 0;
    /**
     * 交易日开盘价
     */
    private float todayOpenPrice = 0;
    /**
     * 交易日最高价
     */
    private float todayHighestPrice = 0;
    /**
     * 交易日最低价
     */
    private float todayLowestPrice = 0;
    /**
     * 交易日成交量
     */
    private double dealNum = 0;
    /**
     * 交易日成交金额
     */
    private double dealMoney = 0;

    @BindView(R.id.currentPriceSVTV)
    StockValueTextView currentPriceSVTV;
    @BindView(R.id.uptickPriceSVTV)
    StockValueTextView uptickPriceSVTV;
    @BindView(R.id.uptickRateSVTV)
    StockValueTextView uptickRateSVTV;
    @BindView(R.id.todayHighestPriceSVTV)
    StockValueTextView todayHighestPriceSVTV;
    @BindView(R.id.todayLowestPriceSVTV)
    StockValueTextView todayLowestPriceSVTV;
    @BindView(R.id.todayOpenPriceSVTV)
    StockValueTextView todayOpenPriceSVTV;
    @BindView(R.id.dealAvgPriceSVTV)
    StockValueTextView dealAvgPriceSVTV;
    @BindView(R.id.dealNumSVTV)
    StockValueTextView dealNumSVTV;
    @BindView(R.id.dealMoneySVTV)
    StockValueTextView dealMoneySVTV;

    /**
     * 构造函数
     * @param context
     */
    public StockDealInfoView(Context context) {
        super(context);
        this.bindLayout();
        this.initView();
    }

    public StockDealInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockDealInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockDealInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                R.layout.view_stock_deal_info, this, true
        );
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化属性设置
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(
                attrs, R.styleable.StockDealInfoView
        );
        currentPrice = typedArray.getFloat(
                R.styleable.StockDealInfoView_currentPrice,
                0f
        );
        yesterdayClosePrice = typedArray.getFloat(
                R.styleable.StockDealInfoView_yesterdayClosePrice,
                0f
        );
        todayOpenPrice = typedArray.getFloat(
                R.styleable.StockDealInfoView_todayOpenPrice,
                0f
        );
        todayHighestPrice = typedArray.getFloat(
                R.styleable.StockDealInfoView_todayHighestPrice,
                0f
        );
        todayLowestPrice = typedArray.getFloat(
                R.styleable.StockDealInfoView_todayLowestPrice,
                0f
        );
        dealNum = typedArray.getFloat(
                R.styleable.StockDealInfoView_dealNum,
                0f
        );
        dealMoney = typedArray.getFloat(
                R.styleable.StockDealInfoView_dealMoney,
                0f
        );
    }

    /**
     * 初始化界面
     */
    private void initView() {
        float uptickPrice = 0;
        float uptickRate = 0;
        if (currentPrice > 0 && yesterdayClosePrice > 0) {
            uptickPrice = currentPrice - yesterdayClosePrice;
            uptickRate = uptickPrice / yesterdayClosePrice;
        }
        int uptickType = getUptickType(uptickPrice);
        currentPriceSVTV.setValue(currentPrice).setUptickType(uptickType).reDraw();
        uptickPriceSVTV.setValue(uptickPrice).setUptickType(uptickType).reDraw();
        uptickRateSVTV.setValue(uptickRate).setUptickType(uptickType).reDraw();
        todayHighestPriceSVTV.setValue(todayHighestPrice)
                .setUptickType(getUptickType(todayHighestPrice - yesterdayClosePrice))
                .reDraw();
        todayLowestPriceSVTV.setValue(todayLowestPrice)
                .setUptickType(getUptickType(todayLowestPrice - yesterdayClosePrice))
                .reDraw();
        todayOpenPriceSVTV.setValue(todayOpenPrice)
                .setUptickType(getUptickType(todayOpenPrice - yesterdayClosePrice))
                .reDraw();
        dealNumSVTV.setValue(dealNum).reDraw();
        dealMoneySVTV.setValue(dealMoney).reDraw();
        double dealAvgPrice = 0;
        if (dealNum > 0 && dealMoney > 0) {
            dealAvgPrice = dealMoney / dealNum;
        }
        dealAvgPriceSVTV.setValue(dealAvgPrice).reDraw();
    }

    /**
     * 获取增幅类型
     * @param value
     * @return
     */
    private int getUptickType(float value) {
        if (value > 0) {
            return StockValueTextView.UPTICK_TYPE_UP;
        } else if (value < 0) {
            return StockValueTextView.UPTICK_TYPE_DOWN;
        } else {
            return StockValueTextView.UPTICK_TYPE_FLAT;
        }
    }
}
