package com.fox.stockhelper.ui.adapter.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.ui.activity.StockDealLineActivity;

import androidx.annotation.NonNull;

/**
 * 股票排行名称列表
 * @author lusongsong
 * @date 2020/9/8 15:17
 */
public class StockRankNameAdapter extends BaseRecyclerViewAdapter {
    class StockRankNameViewHolder extends BaseViewHolder {
        LinearLayout stockRankNameLL;
        TextView stockRankName;
        TextView stockRankCode;
        public StockRankNameViewHolder(@NonNull View itemView) {
            super(itemView);
            stockRankNameLL = itemView.findViewById(R.id.stockRankNameLL);
            stockRankName = itemView.findViewById(R.id.stockRankName);
            stockRankCode = itemView.findViewById(R.id.stockRankCode);
        }

        @Override
        public void setData(int position, Object data){
            stockRankName.setText(((RankApiDto)data).getStockName());
            stockRankCode.setText(((RankApiDto)data).getStockCode());
            stockRankNameLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), StockDealLineActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("stockId", ((RankApiDto)data).getStockId());
                    intent.putExtra("stock", bundle);
                    getContext().startActivity(intent);
                }
            });
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