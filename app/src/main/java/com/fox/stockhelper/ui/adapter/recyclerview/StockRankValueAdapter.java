package com.fox.stockhelper.ui.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.ui.view.StockValueTextView;

import androidx.annotation.NonNull;

/**
 * 股票排行数值列表
 * @author lusongsong
 * @date 2020/9/8 15:26
 */
public class StockRankValueAdapter extends BaseRecyclerViewAdapter {
    class StockRankValueViewHolder extends BaseViewHolder {

        StockValueTextView stockRankValuePrice;
        StockValueTextView stockRankValueUptickRate;
        StockValueTextView stockRankValueSurgeRate;
        StockValueTextView stockRankValueDealNum;
        StockValueTextView stockRankValueDealMoney;

        public StockRankValueViewHolder(@NonNull View itemView) {
            super(itemView);
            stockRankValuePrice = itemView.findViewById(R.id.stockRankValuePrice);
            stockRankValueUptickRate = itemView.findViewById(R.id.stockRankValueUptickRate);
            stockRankValueSurgeRate = itemView.findViewById(R.id.stockRankValueSurgeRate);
            stockRankValueDealNum = itemView.findViewById(R.id.stockRankValueDealNum);
            stockRankValueDealMoney = itemView.findViewById(R.id.stockRankValueDealMoney);
        }

        @Override
        public void setData(int position, Object data){
            stockRankValuePrice.setValue(((RankApiDto)data).getPrice());
            stockRankValueUptickRate.setValue(((RankApiDto)data).getUptickRate());
            stockRankValueSurgeRate.setValue(((RankApiDto)data).getSurgeRate());
            stockRankValueDealNum.setValue(((RankApiDto)data).getDealNum());
            stockRankValueDealMoney.setValue(((RankApiDto)data).getDealMoney());
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