package com.fox.stockhelper.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * 股票排行信息
 *
 * @author lusongsong
 */
public class StockRankInfoFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_rank_info, null);
        ButterKnife.bind(this, view);

        return view;
    }
}
