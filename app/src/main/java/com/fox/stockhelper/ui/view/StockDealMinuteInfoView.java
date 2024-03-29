package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;

import java.math.BigDecimal;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;

/**
 * @author lusongsong
 * @date 2020/10/22 20:56
 */
public class StockDealMinuteInfoView extends LinearLayout {
    /**
     * 日期
     */
    private String dt;
    /**
     * 时间
     */
    private String time;
    /**
     * 价格
     */
    private BigDecimal price = BigDecimal.ZERO;
    /**
     * 均价
     */
    private BigDecimal avgPrice = BigDecimal.ZERO;
    /**
     * 成交量
     */
    private Long dealNum = 0L;
    /**
     * 成交金额
     */
    private BigDecimal dealMoney = BigDecimal.ZERO;

    @BindView(R.id.dtTV)
    TextView dtTV;
    @BindView(R.id.timeTV)
    TextView timeTV;
    @BindView(R.id.priceSVTV)
    StockValueTextView priceSVTV;
    @BindView(R.id.avgPriceSVTV)
    StockValueTextView avgPriceSVTV;
    @BindView(R.id.dealNumSVTV)
    StockValueTextView dealNumSVTV;
    @BindView(R.id.dealMoneySVTV)
    StockValueTextView dealMoneySVTV;

    /**
     * 构造函数
     * @param context
     */
    public StockDealMinuteInfoView(Context context) {
        super(context);
        this.bindLayout();
        this.initView();
    }

    public StockDealMinuteInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockDealMinuteInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockDealMinuteInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                R.layout.view_stock_deal_minute_info, this, true
        );
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化属性设置
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(
                attrs, R.styleable.StockDealMinuteInfoView
        );
        dt = typedArray.getString(R.styleable.StockDealMinuteInfoView_dt);
        time = typedArray.getString(R.styleable.StockDealMinuteInfoView_time);
        price = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealMinuteInfoView_price,
                0f
        ));
        avgPrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealMinuteInfoView_avgPrice,
                0f
        ));
        dealNum = (long) typedArray.getFloat(
                R.styleable.StockDealMinuteInfoView_dealNum,
                0f
        );
        dealMoney = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealMinuteInfoView_dealMoney,
                0f
        ));
    }

    /**
     * 初始化界面
     */
    private void initView() {
        dtTV.setText(dt);
        timeTV.setText(time);
        priceSVTV.setValue(price).reDraw();
        avgPriceSVTV.setValue(avgPrice).reDraw();
        dealNumSVTV.setValue(new BigDecimal(dealNum)).reDraw();
        dealMoneySVTV.setValue(dealMoney).reDraw();
    }

    /**
     * 刷新数据
     */
    public void reDraw() {
        initView();
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Long getDealNum() {
        return dealNum;
    }

    public void setDealNum(Long dealNum) {
        this.dealNum = dealNum;
    }

    public BigDecimal getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(BigDecimal dealMoney) {
        this.dealMoney = dealMoney;
    }

    public TextView getDtTV() {
        return dtTV;
    }

    public void setDtTV(TextView dtTV) {
        this.dtTV = dtTV;
    }

    public TextView getTimeTV() {
        return timeTV;
    }

    public void setTimeTV(TextView timeTV) {
        this.timeTV = timeTV;
    }

    public StockValueTextView getPriceSVTV() {
        return priceSVTV;
    }

    public void setPriceSVTV(StockValueTextView priceSVTV) {
        this.priceSVTV = priceSVTV;
    }

    public StockValueTextView getAvgPriceSVTV() {
        return avgPriceSVTV;
    }

    public void setAvgPriceSVTV(StockValueTextView avgPriceSVTV) {
        this.avgPriceSVTV = avgPriceSVTV;
    }

    public StockValueTextView getDealNumSVTV() {
        return dealNumSVTV;
    }

    public void setDealNumSVTV(StockValueTextView dealNumSVTV) {
        this.dealNumSVTV = dealNumSVTV;
    }

    public StockValueTextView getDealMoneySVTV() {
        return dealMoneySVTV;
    }

    public void setDealMoneySVTV(StockValueTextView dealMoneySVTV) {
        this.dealMoneySVTV = dealMoneySVTV;
    }
}
