package com.fox.stockhelper.ui.activity;

import android.os.Bundle;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * 股票排行
 * @author lusongsong
 * @date 2020/9/3 14:51
 */
public class StockRankActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_rank);
        ButterKnife.bind(StockRankActivity.this);
    }
}
