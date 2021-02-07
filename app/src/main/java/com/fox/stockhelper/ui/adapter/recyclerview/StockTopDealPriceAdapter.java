package com.fox.stockhelper.ui.adapter.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * 股票排行数值列表
 *
 * @author lusongsong
 * @date 2020/9/8 15:26
 */
public class StockTopDealPriceAdapter extends BaseRecyclerViewAdapter {
    /**
     * 买卖编号显示权重
     */
    private int priceNumWeight = 2;
    private int priceNumTextSize = 2;
    /**
     * 价格显示权重
     */
    private int priceWeight = 4;
    private int priceTextSize = 4;
    /**
     * 数量显示权重
     */
    private int numWeight = 4;
    private int numTextSize = 4;

    class StockTopDealPriceViewHolder extends BaseViewHolder {
        TextView stockTopOrderNumTV;
        TextView stockTopDealPriceTV;
        TextView stockTopDealNumTV;

        public StockTopDealPriceViewHolder(@NonNull View itemView) {
            super(itemView);
            stockTopOrderNumTV = itemView.findViewById(R.id.stockTopOrderNumTV);
            stockTopDealPriceTV = itemView.findViewById(R.id.stockTopDealPriceTV);
            stockTopDealNumTV = itemView.findViewById(R.id.stockTopDealNumTV);
        }

        @Override
        public void setData(int position, Object data) {
            List<Object> topPriceInfoList = (List<Object>) data;
            int color = (int) topPriceInfoList.get(3);
            stockTopOrderNumTV.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT, priceNumWeight));
            stockTopOrderNumTV.setTextSize(priceNumTextSize);
            stockTopOrderNumTV.setText((String) topPriceInfoList.get(0));
            stockTopDealPriceTV.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT, priceWeight));
            stockTopDealPriceTV.setTextSize(priceTextSize);
            stockTopDealPriceTV.setTextColor(color);
            stockTopDealPriceTV.setText((String) topPriceInfoList.get(1));
            stockTopDealNumTV.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT, numWeight));
            stockTopDealNumTV.setTextSize(numTextSize);
            stockTopDealNumTV.setTextColor(color);
            stockTopDealNumTV.setText((String) topPriceInfoList.get(2));
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_stock_top_deal_price, parent, false);
        return new StockTopDealPriceViewHolder(view);
    }

    /**
     * 设置显示权重
     *
     * @param weightList
     */
    public void setColumnWeight(List<Integer> weightList) {
        if (null == weightList || weightList.isEmpty() || weightList.size() != 6) {
            return;
        }
        priceNumWeight = weightList.get(0);
        priceNumTextSize = weightList.get(1);
        priceWeight = weightList.get(2);
        priceTextSize = weightList.get(3);
        numWeight = weightList.get(4);
        numTextSize = weightList.get(5);
    }
}