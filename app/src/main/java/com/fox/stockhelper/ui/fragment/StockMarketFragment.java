package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.util.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股市首页
 * @author lusongsong
 */
public class StockMarketFragment extends BaseFragment {
    /**
     * 股市
     */
    private String stockMarket;

    /**
     * 当前日期
     */
    @BindView(R.id.smDate)
    TextView smDate;
    /**
     * 星期
     */
    @BindView(R.id.smWeek)
    TextView smWeek;

    /**
     * 股票排行
     */
    @BindView(R.id.stockRankHeader)
    LinearLayout stockRankHeader;

    /**
     * 指定上下文和股市
     * @param context
     * @param stockMarket
     */
    public StockMarketFragment(Context context, String stockMarket) {
        super(context);
        this.stockMarket = stockMarket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_market, null);
        ButterKnife.bind(this, view);
        //显示时间
        this.initDate();
        return view;
    }

    /**
     * 设置显示时间
     */
    private void initDate() {
        smDate.setText(DateUtil.getCurrentDate());
        smWeek.setText(DateUtil.getCurrentWeekNum());
    }
}
