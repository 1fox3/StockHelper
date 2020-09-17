package com.fox.stockhelper.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.adapter.SimpleFragmentStatePagerAdapter;
import com.fox.stockhelper.ui.base.BaseActivity;
import com.fox.stockhelper.ui.fragment.StockDealLineOfflineFragment;
import com.fox.stockhelper.ui.fragment.StockDealLineRealtimeFragment;
import com.fox.stockhelper.ui.view.NoTouchScrollViewpager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股市成交线图
 * @author lusongsong
 * @date 2020/9/14 15:15
 */
public class StockDealLineActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    /**
     * 股票id
     */
    Integer stockId;
    @BindView(R.id.stockNameCodeTV)
    TextView stockNameCodeTV;
    @BindView(R.id.stockDealLineTabTL)
    TabLayout stockDealLineTabTL;
    @BindView(R.id.stockDealLineTypeNTSVP)
    NoTouchScrollViewpager stockDealLineTypeNTSVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_deal_line);
        ButterKnife.bind(StockDealLineActivity.this);
        Bundle bundle = getIntent().getBundleExtra("stock");
        stockId = bundle.getInt("stockId", 1);
        String stockName = bundle.getString("stockName");
        String stockCode = bundle.getString("stockCode");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stockName);
        stringBuilder.append("(");
        stringBuilder.append(stockCode);
        stringBuilder.append(")");
        stockNameCodeTV.setText(stringBuilder.toString());
        List<Fragment> stockDealLineFragmentList = new ArrayList<>(2);
        stockDealLineFragmentList.add(new StockDealLineRealtimeFragment(this, this.stockId));
        stockDealLineFragmentList.add(new StockDealLineOfflineFragment(this, this.stockId));
        List<String> titleList = Arrays.asList("实时", "离线");
        //股市页面切换器
        SimpleFragmentStatePagerAdapter simpleFragmentStatePagerAdapter =
                new SimpleFragmentStatePagerAdapter(
                        getSupportFragmentManager(),
                        stockDealLineFragmentList,
                        titleList
                );
        stockDealLineTypeNTSVP.setAdapter(simpleFragmentStatePagerAdapter);
        stockDealLineTypeNTSVP.addOnPageChangeListener(this);
        stockDealLineTabTL.setupWithViewPager(stockDealLineTypeNTSVP);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}
}
