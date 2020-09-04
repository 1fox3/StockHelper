package com.fox.stockhelper.ui.adapter.recyclerview;

import android.view.View;
import android.widget.TextView;

import com.fox.stockhelper.R;

import androidx.annotation.NonNull;

/**
 * @author lusongsong
 * @date 2020/9/4 14:27
 */
public class StockRankHeaderViewHolder extends BaseViewHolder {
    TextView sortType;
    public StockRankHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        sortType = (TextView) itemView.findViewById(R.id.sortType);
    }

    @Override
    public void setData(Object data){
        sortType.setText((String)data);
    }
}
