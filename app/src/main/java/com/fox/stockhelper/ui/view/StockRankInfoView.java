package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股票排行信息
 * @author lusongsong
 * @date 2020-08-28 14:34
 */
public class StockRankInfoView extends LinearLayout {
    /**
     * 排行信息对象
     */
    private RankApiDto rankApiDto;
    /**
     * 股票名称
     */
    @BindView(R.id.stockNameCode)
    TextView stockNameCode;
    /**
     * 股票价格
     */
    @BindView(R.id.stockPrice)
    StockValueTextView stockPrice;
    /**
     * 股票增幅
     */
    @BindView(R.id.stockUptickRate)
    StockValueTextView stockUptickRate;
    /**
     * 股票波动
     */
    @BindView(R.id.stockSurgeRate)
    StockValueTextView stockSurgeRate;
    /**
     * 股票成交量
     */
    @BindView(R.id.stockDealNum)
    StockValueTextView stockDealNum;
    /**
     * 股票成交金额
     */
    @BindView(R.id.stockDealMoney)
    StockValueTextView stockDealMoney;

    public StockRankInfoView(Context context) {
        super(context);
        this.bindLayout();
        this.initView();
    }

    public StockRankInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockRankInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.bindLayout();
        this.initAttrs(attrs);
        this.initView();
    }

    public StockRankInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                R.layout.view_stock_rank_info, this, true
        );
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化属性设置
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        rankApiDto = new RankApiDto();

        TypedArray typedArray = this.getContext().obtainStyledAttributes(
                attrs, R.styleable.StockRankInfoView
        );
        rankApiDto.setStockName(typedArray.getString(R.styleable.StockRankInfoView_stockName));
        rankApiDto.setStockCode(typedArray.getString(R.styleable.StockRankInfoView_stockCode));
        rankApiDto.setPrice(typedArray.getFloat(
                R.styleable.StockRankInfoView_stockPrice,
                0f
        ));
        rankApiDto.setUptickRate(typedArray.getFloat(
                R.styleable.StockRankInfoView_uptickRate,
                0f
        ));
        rankApiDto.setSurgeRate(typedArray.getFloat(
                R.styleable.StockRankInfoView_surgeRate,
                0f
        ));
        rankApiDto.setDealNum(typedArray.getFloat(
                R.styleable.StockRankInfoView_dealNum,
                0f
        ));
        rankApiDto.setDealMoney(typedArray.getFloat(
                R.styleable.StockRankInfoView_dealMoney,
                0f
        ));
    }

    /**
     * 显示内容
     */
    public void initView() {
        if (null != rankApiDto) {
            stockNameCode.setText(rankApiDto.getStockName() + "\n" + rankApiDto.getStockCode());
            stockPrice.setValue(
                    (double)rankApiDto.getPrice(),
                    StockValueTextView.TYPE_NUMBER,
                    StockValueTextView.UNIT_NUMBER
            ).reDraw();
            stockUptickRate.setValue(
                    (double)rankApiDto.getUptickRate(),
                    StockValueTextView.TYPE_RATE,
                    StockValueTextView.UNIT_NUMBER
            ).reDraw();
            stockSurgeRate.setValue(
                    (double)rankApiDto.getSurgeRate(),
                    StockValueTextView.TYPE_RATE,
                    StockValueTextView.UNIT_NUMBER
            ).reDraw();
            stockDealNum.setValue(
                    (double)rankApiDto.getDealNum(),
                    StockValueTextView.TYPE_NUMBER,
                    StockValueTextView.UNIT_NUMBER
            ).reDraw();
            stockDealMoney.setValue(
                    (double)rankApiDto.getDealMoney(),
                    StockValueTextView.TYPE_NUMBER,
                    StockValueTextView.UNIT_NUMBER
            ).reDraw();
        }
    }

    /**
     * 设置排行信息
     * @param rankApiDto
     */
    public void setRankInfo(RankApiDto rankApiDto) {
        this.rankApiDto = rankApiDto;
        initView();
    }
}
