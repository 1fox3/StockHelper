package com.fox.stockhelper.ui.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;

import androidx.annotation.NonNull;

/**
 * 股票排行名称列表
 * @author lusongsong
 * @date 2020/9/8 15:17
 */
public class StockRankNameAdapter extends BaseRecyclerViewAdapter {
    class StockRankNameViewHolder extends BaseViewHolder {
        TextView stockRankName;
        TextView stockRankCode;
        public StockRankNameViewHolder(@NonNull View itemView) {
            super(itemView);
            stockRankName = (TextView) itemView.findViewById(R.id.stockRankName);
            stockRankCode = (TextView) itemView.findViewById(R.id.stockRankCode);
        }

        @Override
        public void setData(int position, Object data){
            stockRankName.setText(((RankApiDto)data).getStockName());
            stockRankCode.setText(((RankApiDto)data).getStockCode());
        }
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_stock_rank_name, parent, false);
        return new StockRankNameViewHolder(view);
    }
}