package com.fox.stockhelper.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.base.BaseFragment;

/**
 * A股首页
 * @author lusongsong
 */
public class StockMarketFragment extends BaseFragment {
    private String stockMarket;

    public StockMarketFragment(String stockMarket) {
        this.stockMarket = stockMarket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_market, null);
        return view;
    }
}
