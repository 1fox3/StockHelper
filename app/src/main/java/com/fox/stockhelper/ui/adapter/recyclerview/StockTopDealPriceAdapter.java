package com.fox.stockhelper.ui.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.TopDealPriceSingleDto;
import com.fox.stockhelper.ui.view.StockValueTextView;

import java.math.BigDecimal;

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
    private BigDecimal preClosePrice = BigDecimal.ZERO;

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
            BigDecimal price = ((TopDealPriceSingleDto)data).getPrice();
            int uptickType = getUptickType(price);
            stockTopDealPriceSVTV.setValue(price).setUptickType(uptickType).reDraw();
            stockTopDealNumSVTV.setValue(new BigDecimal(((TopDealPriceSingleDto)data).getNum()))
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
     * @param preClosePrice
     * @return
     */
    public StockTopDealPriceAdapter setPreClosePrice(BigDecimal preClosePrice) {
        this.preClosePrice = null != preClosePrice && 1 == preClosePrice.compareTo(BigDecimal.ZERO)
                ? preClosePrice : this.preClosePrice;
        return this;
    }

    /**
     * 获取增长类型
     * @param price
     * @return
     */
    private int getUptickType(BigDecimal price) {
        int type = StockValueTextView.UPTICK_TYPE_FLAT;
        if (1 == preClosePrice.compareTo(BigDecimal.ZERO) && 0 != price.compareTo(BigDecimal.ZERO)) {
            switch (price.compareTo(preClosePrice)) {
                case 1:
                    type = StockValueTextView.UPTICK_TYPE_UP;
                    break;
                case 0:
                    type = StockValueTextView.UPTICK_TYPE_FLAT;
                    break;
                case -1:
                    type = StockValueTextView.UPTICK_TYPE_DOWN;
                    break;
            }
        }
        return  type;
    }
}