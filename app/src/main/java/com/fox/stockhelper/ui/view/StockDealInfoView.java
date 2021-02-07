package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;

import java.math.BigDecimal;

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
    private BigDecimal currentPrice = BigDecimal.ZERO;
    /**
     * 上个交易日收盘价
     */
    private BigDecimal preClosePrice = BigDecimal.ZERO;
    /**
     * 交易日开盘价
     */
    private BigDecimal openPrice = BigDecimal.ZERO;
    /**
     * 交易日最高价
     */
    private BigDecimal highestPrice = BigDecimal.ZERO;
    /**
     * 交易日最低价
     */
    private BigDecimal lowestPrice = BigDecimal.ZERO;
    /**
     * 交易日成交量
     */
    private Long dealNum = 0L;
    /**
     * 交易日成交金额
     */
    private BigDecimal dealMoney = BigDecimal.ZERO;

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
        initView(null);
    }

    public StockDealInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public StockDealInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public StockDealInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void initView(AttributeSet attrs) {
        bindLayout();
        initAttrs(attrs);
        draw();
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
        currentPrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealInfoView_currentPrice,
                0f
        ));
        preClosePrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealInfoView_preClosePrice,
                0f
        ));
        openPrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealInfoView_openPrice,
                0f
        ));
        highestPrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealInfoView_highestPrice,
                0f
        ));
        lowestPrice = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealInfoView_lowestPrice,
                0f
        ));
        dealNum = (long) typedArray.getFloat(
                R.styleable.StockDealInfoView_dealNum,
                0f
        );
        dealMoney = new BigDecimal(typedArray.getFloat(
                R.styleable.StockDealInfoView_dealMoney,
                0f
        ));
    }

    /**
     * 初始化界面
     */
    private void draw() {
        BigDecimal uptickPrice = BigDecimal.ZERO;
        BigDecimal uptickRate = BigDecimal.ZERO;
        if (0 < currentPrice.compareTo(BigDecimal.ZERO) && 0 < preClosePrice.compareTo(BigDecimal.ZERO)) {
            uptickPrice = currentPrice.subtract(preClosePrice);
            uptickRate = uptickPrice.multiply(new BigDecimal(100)).divide(preClosePrice, 2, BigDecimal.ROUND_HALF_UP);
        }
        int uptickType = getUptickType(uptickPrice);
        currentPriceSVTV.setValue(currentPrice).setUptickType(uptickType).reDraw();
        uptickPriceSVTV.setValue(uptickPrice).setUptickType(uptickType).reDraw();
        uptickRateSVTV.setValue(uptickRate).setUptickType(uptickType).reDraw();
        todayHighestPriceSVTV.setValue(highestPrice)
                .setUptickType(getUptickType(highestPrice.subtract(preClosePrice)))
                .reDraw();
        todayLowestPriceSVTV.setValue(lowestPrice)
                .setUptickType(getUptickType(lowestPrice.subtract(preClosePrice)))
                .reDraw();
        todayOpenPriceSVTV.setValue(openPrice)
                .setUptickType(getUptickType(openPrice.subtract(preClosePrice)))
                .reDraw();
        dealNumSVTV.setValue(new BigDecimal(dealNum)).reDraw();
        dealMoneySVTV.setValue(dealMoney).reDraw();
        BigDecimal dealAvgPrice = BigDecimal.ZERO;
        if (dealNum > 0 && 0 < dealMoney.compareTo(BigDecimal.ZERO)) {
            dealAvgPrice = dealMoney.divide(new BigDecimal(dealNum), 2, BigDecimal.ROUND_HALF_UP);
        }
        dealAvgPriceSVTV.setValue(dealAvgPrice).reDraw();
    }

    /**
     * 获取增幅类型
     * @param value
     * @return
     */
    private int getUptickType(BigDecimal value) {
        if (null != value) {
            switch (value.compareTo(BigDecimal.ZERO)) {
                case 1:
                    return StockValueTextView.UPTICK_TYPE_UP;
                case 0:
                    return StockValueTextView.UPTICK_TYPE_FLAT;
                case -1:
                    return StockValueTextView.UPTICK_TYPE_DOWN;
            }
        }
        return StockValueTextView.UPTICK_TYPE_FLAT;
    }

    /**
     * 设置数据
     * @param dealInfoApiDto
     * @return
     */
    public StockDealInfoView setData(DealInfoApiDto dealInfoApiDto) {
        if (null != dealInfoApiDto.getCurrentPrice()
                && 0 < dealInfoApiDto.getCurrentPrice().compareTo(BigDecimal.ZERO)) {
            currentPrice = dealInfoApiDto.getCurrentPrice();
        }
        if (null != dealInfoApiDto.getPreClosePrice()
                && 0 < dealInfoApiDto.getPreClosePrice().compareTo(BigDecimal.ZERO)) {
            preClosePrice = dealInfoApiDto.getPreClosePrice();
        }
        if (null != dealInfoApiDto.getOpenPrice()
                && 0 < dealInfoApiDto.getOpenPrice().compareTo(BigDecimal.ZERO)) {
            openPrice = dealInfoApiDto.getOpenPrice();
        }
        if (null != dealInfoApiDto.getHighestPrice()
                && 0 < dealInfoApiDto.getHighestPrice().compareTo(BigDecimal.ZERO)) {
            highestPrice = dealInfoApiDto.getHighestPrice();
        }
        if (null != dealInfoApiDto.getLowestPrice()
                && 0 < dealInfoApiDto.getLowestPrice().compareTo(BigDecimal.ZERO)) {
            lowestPrice = dealInfoApiDto.getLowestPrice();
        }
        if (null != dealInfoApiDto.getDealNum() && 0 < dealInfoApiDto.getDealNum()) {
            dealNum = dealInfoApiDto.getDealNum();
        }
        if (null != dealInfoApiDto.getDealMoney()
                && 0 < dealInfoApiDto.getDealMoney().compareTo(BigDecimal.ZERO)) {
            dealMoney = dealInfoApiDto.getDealMoney();
        }
        return this;
    }

    /**
     * 刷新数据
     */
    public void reDraw() {
        draw();
    }
}
