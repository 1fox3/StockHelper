package com.fox.stockhelper.ui.adapter.recyclerview;

import android.view.View;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.view.StockValueTextView;

import androidx.annotation.NonNull;

/**
 * @author lusongsong
 * @date 2020/9/4 14:27
 */
public class StockRankItemViewHolder extends BaseViewHolder {
    StockValueTextView stockValueTextView;
    public StockRankItemViewHolder(@NonNull View itemView) {
        super(itemView);
        stockValueTextView = (StockValueTextView) itemView.findViewById(R.id.stockValueTextView);
    }

    public void setData(Object data){
        stockValueTextView.setValue(Double.valueOf(data.toString()), StockValueTextView.TYPE_NUMBER, StockValueTextView.UNIT_NUMBER);
    }
}
