package com.fox.stockhelper.ui.activity;

import android.os.Bundle;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.adapter.SimpleFragmentStatePagerAdapter;
import com.fox.stockhelper.ui.base.BaseActivity;
import com.fox.stockhelper.ui.fragment.StockLandLineMultiDayMinuteFragment;
import com.fox.stockhelper.ui.fragment.StockLandLineKlineFragment;
import com.fox.stockhelper.ui.fragment.StockLandLineSingleDayMinuteFragment;
import com.fox.stockhelper.ui.view.NoTouchScrollViewpager;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股票所有类型横屏k线图
 *
 * @author lusongsong
 * @date 2020/10/23 13:49
 */
public class StockAllKlineLandActivity extends BaseActivity {
    /**
     * 股市
     */
    Integer stockMarket;
    /**
     * 股票代码
     */
    String stockCode;
    /**
     * 线图分类
     */
    public static final List<String> titles = Arrays.asList(
            "分时",
            "5日",
            "日K",
            "周K",
            "月K"
    );

    @BindView(R.id.klineTL)
    TabLayout klineTL;
    @BindView(R.id.klineNTSV)
    NoTouchScrollViewpager klineNTSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_all_kline_land);
        ButterKnife.bind(StockAllKlineLandActivity.this);
        Bundle bundle = getIntent().getBundleExtra("stock");
        stockMarket = bundle.getInt("stockId", StockConst.SM_A);
        stockCode = bundle.getString("stockCode");
        StockVo stockVo = new StockVo(stockCode, stockMarket);

        List<Fragment> fragmentList = new LinkedList<>();
        fragmentList.add(new StockLandLineSingleDayMinuteFragment(this, stockVo));
        fragmentList.add(new StockLandLineMultiDayMinuteFragment(this, stockVo));
        fragmentList.add(new StockLandLineKlineFragment(this, stockVo, StockConst.DT_DAY));
        fragmentList.add(new StockLandLineKlineFragment(this, stockVo, StockConst.DT_WEEK));
        fragmentList.add(new StockLandLineKlineFragment(this, stockVo, StockConst.DT_MONTH));
        klineNTSV.setOffscreenPageLimit(titles.size());
        klineNTSV.setAdapter(new SimpleFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList, titles));
        klineTL.setupWithViewPager(klineNTSV);
    }
}
