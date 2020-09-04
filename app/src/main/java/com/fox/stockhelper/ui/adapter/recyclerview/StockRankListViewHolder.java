package com.fox.stockhelper.ui.adapter.recyclerview;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author lusongsong
 * @date 2020/9/4 14:27
 */
public class StockRankListViewHolder extends BaseViewHolder {
    private static int posX = 0;
    TextView stockNameCode;
    RecyclerView stockRankItemRV;
    public StockRankListViewHolder(@NonNull View itemView) {
        super(itemView);
        stockNameCode = (TextView) itemView.findViewById(R.id.stockNameCode);
        stockRankItemRV = (RecyclerView) itemView.findViewById(R.id.stockRankItemRV);
    }

    public void setPosX(int dx) {
        posX = dx;
    }

    @Override
    public void setData(Object data){
        stockNameCode.setText(((RankApiDto)data).getStockName());
        StockRankItemAdapter itemAdapter = new StockRankItemAdapter();
        List<String> valueList  = new ArrayList<>(5);
        valueList.add(((RankApiDto)data).getPrice().toString());
        valueList.add(((RankApiDto)data).getUptickRate().toString());
        valueList.add(((RankApiDto)data).getSurgeRate().toString());
        valueList.add(((RankApiDto)data).getDealNum().toString());
        valueList.add(((RankApiDto)data).getDealMoney().toString());
        itemAdapter.addData(valueList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(null);
        stockRankItemRV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        stockRankItemRV.setAdapter(itemAdapter);
        Log.e("setData", String.valueOf(posX));
        stockRankItemRV.scrollBy(posX, 0);
    }
}
