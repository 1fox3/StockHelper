package com.fox.stockhelper.ui.adapter.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.stockhelper.R;

import androidx.annotation.NonNull;

/**
 * 股票排行列表
 * @author lusongsong
 * @date 2020/9/3 16:51
 */
public class StockRankListAdapter extends BaseRecyclerViewAdapter {
    /**
     * 列表当前x偏移量
     */
    private int posX = 0;
    StockRankListViewHolder stockRankListViewHolder;
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("StockRankListAdapter:onCreateViewHolder", String.valueOf(posX));
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_stock_rank_list, parent, false);
        stockRankListViewHolder = new StockRankListViewHolder(view);
        stockRankListViewHolder.setPosX(posX);
        return stockRankListViewHolder;
    }

    public void setPosX(int dx) {
        posX = dx;
    }
}
