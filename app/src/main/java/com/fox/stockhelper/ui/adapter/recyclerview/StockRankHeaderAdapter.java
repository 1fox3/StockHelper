package com.fox.stockhelper.ui.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.stockhelper.R;

import androidx.annotation.NonNull;

/**
 * 股票排行头部
 * @author lusongsong
 * @date 2020/9/3 16:51
 */
public class StockRankHeaderAdapter extends BaseRecyclerViewAdapter {
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_stock_rank_header, parent, false);
        return new StockRankHeaderViewHolder(view);
    }
}
