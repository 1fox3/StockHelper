package com.fox.stockhelper.ui.adapter.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.ui.activity.StockDealLineActivity;
import com.fox.stockhelper.ui.view.StockValueTextView;

import androidx.annotation.NonNull;

/**
 * 股票排行数值列表
 * @author lusongsong
 * @date 2020/9/8 15:26
 */
public class StockRankValueAdapter extends BaseRecyclerViewAdapter {
    class StockRankValueViewHolder extends BaseViewHolder {
        LinearLayout stockRankValueLL;
        StockValueTextView stockRankValuePrice;
        StockValueTextView stockRankValueUptickRate;
        StockValueTextView stockRankValueSurgeRate;
        StockValueTextView stockRankValueDealNum;
        StockValueTextView stockRankValueDealMoney;

        public StockRankValueViewHolder(@NonNull View itemView) {
            super(itemView);
            stockRankValueLL = itemView.findViewById(R.id.stockRankValueLL);
            stockRankValuePrice = itemView.findViewById(R.id.stockRankValuePrice);
            stockRankValueUptickRate = itemView.findViewById(R.id.stockRankValueUptickRate);
            stockRankValueSurgeRate = itemView.findViewById(R.id.stockRankValueSurgeRate);
            stockRankValueDealNum = itemView.findViewById(R.id.stockRankValueDealNum);
            stockRankValueDealMoney = itemView.findViewById(R.id.stockRankValueDealMoney);
        }

        @Override
        public void setData(int position, Object data){
            stockRankValuePrice.setValue(((RankApiDto)data).getPrice()).reDraw();
            stockRankValueUptickRate.setValue(((RankApiDto)data).getUptickRate()).reDraw();
            stockRankValueSurgeRate.setValue(((RankApiDto)data).getSurgeRate()).reDraw();
            stockRankValueDealNum.setValue(((RankApiDto)data).getDealNum()).reDraw();
            stockRankValueDealMoney.setValue(((RankApiDto)data).getDealMoney()).reDraw();
            stockRankValueLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), StockDealLineActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("stockId", ((RankApiDto)data).getStockId());
                    bundle.putString("stockName", ((RankApiDto)data).getStockName());
                    bundle.putString("stockCode", ((RankApiDto)data).getStockCode());
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
                .inflate(R.layout.layout_stock_rank_value, parent, false);
        return new StockRankValueViewHolder(view);
    }
}