package com.fox.stockhelper.ui.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.TopDealPriceSingleDto;
import com.fox.stockhelper.ui.view.StockValueTextView;

import androidx.annotation.NonNull;

/**
 * 股票排行数值列表
 * @author lusongsong
 * @date 2020/9/8 15:26
 */
public class StockTopDealPriceAdapter extends BaseRecyclerViewAdapter {
    /**
     * 价格类型 卖价
     */
    public static final int TOP_PRICE_TYPE_SELL = 0;
    /**
     * 价格类型 买价
     */
    public static final int TOP_PRICE_TYPE_BUY = 1;
    /**
     * 价格类型
     */
    private int priceType = TOP_PRICE_TYPE_SELL;
    /**
     * 昨日收盘价
     */
    private float yesterdayClosePrice = 0;

    class StockTopDealPriceViewHolder extends BaseViewHolder {
        TextView stockTopOrderNumTV;
        StockValueTextView stockTopDealPriceSVTV;
        StockValueTextView stockTopDealNumSVTV;

        public StockTopDealPriceViewHolder(@NonNull View itemView) {
            super(itemView);
            stockTopOrderNumTV = itemView.findViewById(R.id.stockTopOrderNumTV);
            stockTopDealPriceSVTV = itemView.findViewById(R.id.stockTopDealPriceSVTV);
            stockTopDealNumSVTV = itemView.findViewById(R.id.stockTopDealNumSVTV);
        }

        @Override
        public void setData(int position, Object data){
            String priceTypeStr = priceType == TOP_PRICE_TYPE_SELL ? "卖" : "买";
            int priceNum = priceType == TOP_PRICE_TYPE_SELL ?
                    getItemCount() - position : position + 1;
            stockTopOrderNumTV.setText(priceTypeStr + priceNum);
            float price = ((TopDealPriceSingleDto)data).getPrice();
            int uptickType = getUptickType(price);
            stockTopDealPriceSVTV.setValue(price).setUptickType(uptickType).reDraw();
            stockTopDealNumSVTV.setValue(((TopDealPriceSingleDto)data).getNum())
                    .setUptickType(uptickType).reDraw();
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
     * 设置售价类型
     * @param priceType
     */
    public StockTopDealPriceAdapter setPriceType(int priceType) {
        if (priceType == TOP_PRICE_TYPE_BUY) {
            this.priceType = TOP_PRICE_TYPE_BUY;
        } else {
            this.priceType = TOP_PRICE_TYPE_SELL;
        }
        return this;
    }

    /**
     * 设置昨日收盘价
     * @param yesterdayClosePrice
     * @return
     */
    public StockTopDealPriceAdapter setYesterdayClosePrice(float yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice > 0 ?
                yesterdayClosePrice : this.yesterdayClosePrice;
        return this;
    }

    /**
     * 获取增长类型
     * @param price
     * @return
     */
    private int getUptickType(float price) {
        int type = StockValueTextView.UPTICK_TYPE_FLAT;
        if (0 < yesterdayClosePrice) {
            if (price > yesterdayClosePrice) {
                type = StockValueTextView.UPTICK_TYPE_UP;
            } else if (price < yesterdayClosePrice) {
                type = StockValueTextView.UPTICK_TYPE_DOWN;
            } else {
                type = StockValueTextView.UPTICK_TYPE_FLAT;
            }
        }
        return  type;
    }
}