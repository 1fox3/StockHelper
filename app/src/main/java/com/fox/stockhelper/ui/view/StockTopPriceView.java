package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.spider.stock.constant.StockConst;
import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.po.stock.StockRealtimeDealInfoPo;
import com.fox.stockhelper.ui.adapter.recyclerview.StockTopDealPriceAdapter;
import com.fox.stockhelper.util.StockUtil;
import com.fox.stockhelper.util.StockValueUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股票最新交易日交易价格信息
 *
 * @author lusongsong
 * @date 2021/2/4 16:51
 */
public class StockTopPriceView extends LinearLayout {
    /**
     * 股票最新交易日交易信息
     */
    StockRealtimeDealInfoPo stockRealtimeDealInfoPo;
    /**
     * 视图组件
     */
    @BindView(R.id.stockTopPriceTV)
    TextView stockTopPriceTV;
    @BindView(R.id.stockTopPriceSellRV)
    RecyclerView stockSellRV;
    @BindView(R.id.stockTopPriceBuyRV)
    RecyclerView stockBuyRV;
    /**
     * 价格列表适配器
     */
    StockTopDealPriceAdapter sellStockTopDealPriceAdapter;
    StockTopDealPriceAdapter buyStockTopDealPriceAdapter;
    List<List<Object>> buyPriceList;
    List<List<Object>> sellPriceList;
    List<Integer> priceColumnWeight;


    /**
     * 构造函数
     *
     * @param context
     */
    public StockTopPriceView(Context context) {
        super(context);
        initView(null);
    }

    public StockTopPriceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public StockTopPriceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public StockTopPriceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void initView(AttributeSet attrs) {
        bindLayout();
    }

    /**
     * 绑定布局文件
     */
    private void bindLayout() {
        View view = LayoutInflater.from(this.getContext()).inflate(
                R.layout.view_stock_top_price, this, true
        );
        ButterKnife.bind(this, view);
        LinearLayoutManager sellLinearLayoutManager = new LinearLayoutManager(this.getContext());
        sellLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager buyLinearLayoutManager = new LinearLayoutManager(this.getContext());
        buyLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        //初始化TOP售价
        sellStockTopDealPriceAdapter = new StockTopDealPriceAdapter();
        stockSellRV.setLayoutManager(sellLinearLayoutManager);
        //初始化TOP买价
        buyStockTopDealPriceAdapter = new StockTopDealPriceAdapter();
        stockBuyRV.setLayoutManager(buyLinearLayoutManager);
    }

    /**
     * 设置交易信息
     *
     * @param stockRealtimeDealInfoPo
     * @return
     */
    public StockTopPriceView setPriceInfo(StockRealtimeDealInfoPo stockRealtimeDealInfoPo) {
        this.stockRealtimeDealInfoPo = stockRealtimeDealInfoPo;
        handlePriceList();
        return this;
    }

    /**
     * 处理几个列表
     */
    private void handlePriceList() {
        if (null == stockRealtimeDealInfoPo
                || null == stockRealtimeDealInfoPo.getCurrentPrice()
                || null == stockRealtimeDealInfoPo.getPreClosePrice()) {
            return;
        }
        buyPriceList = getTopPriceList(StockConst.DEAL_BUY);
        sellPriceList = getTopPriceList(StockConst.DEAL_SELL);
        priceColumnWeight = getPriceColumnWeight();
        buyStockTopDealPriceAdapter.setColumnWeight(priceColumnWeight);
        sellStockTopDealPriceAdapter.setColumnWeight(priceColumnWeight);
        buyStockTopDealPriceAdapter.setData(buyPriceList);
        sellStockTopDealPriceAdapter.setData(sellPriceList);
        stockSellRV.setAdapter(sellStockTopDealPriceAdapter);
        stockBuyRV.setAdapter(buyStockTopDealPriceAdapter);
    }

    /**
     * 获取售价信息列表
     *
     * @param dealType
     * @return
     */
    private List<List<Object>> getTopPriceList(int dealType) {
        Map<BigDecimal, Long> priceMap;
        String priceDealStr;
        if (dealType == StockConst.DEAL_BUY) {
            priceMap = stockRealtimeDealInfoPo.getBuyPriceMap();
            priceMap = ((TreeMap) priceMap).descendingMap();
            priceDealStr = "买";
        } else {
            priceMap = stockRealtimeDealInfoPo.getSellPriceMap();
            priceDealStr = "卖";
        }
        List<List<Object>> priceList = new ArrayList<>();
        int i = 1;
        for (BigDecimal price : priceMap.keySet()) {
            List<Object> singlePriceList = new ArrayList<>();
            singlePriceList.add(priceDealStr + i);
            if (null != price) {
                singlePriceList.add(StockValueUtil.bdToStr(price));
            } else {
                singlePriceList.add("--");
            }
            if (null != priceMap.get(price)) {
                singlePriceList.add(StockValueUtil.longToStr(
                        priceMap.get(price)
                                /
                                (
                                        StockConst.SM_A_LIST.contains(
                                                stockRealtimeDealInfoPo.getStockMarket()
                                        )
                                                ? 100 : 1
                                )
                ));
            } else {
                singlePriceList.add("--");
            }
            int uptickType = StockUtil.getUptickType(
                    price,
                    stockRealtimeDealInfoPo.getPreClosePrice()
            );
            int colorId = StockUtil.getColor(uptickType);
            int colorSource = this.getContext().getColor(colorId);
            singlePriceList.add(colorSource);
            i++;
            priceList.add(singlePriceList);
        }
        if (dealType == StockConst.DEAL_SELL) {
            Collections.reverse(priceList);
        }
        return priceList;
    }

    /**
     * 获取显示权重
     *
     * @return
     */
    private List<Integer> getPriceColumnWeight() {
        int maxPriceNumStrLen = 0;
        int maxPriceStrLen = 0;
        int maxNumStrLen = 0;
        String maxPriceNumStr = "";
        String maxPriceStr = "";
        String maxNumStr = "";
        TextPaint textPaint = stockTopPriceTV.getPaint();
        for (List<List<Object>> priceList : Arrays.asList(buyPriceList, sellPriceList)) {
            for (int i = 0; i < priceList.size(); i++) {
                String priceNumStr = (String) priceList.get(i).get(0);
                String priceStr = (String) priceList.get(i).get(1);
                String numStr = (String) priceList.get(i).get(2);
                int priceNumLen = (int) textPaint.measureText(priceNumStr);
                int priceLen = (int) textPaint.measureText(priceStr);
                int numLen = (int) textPaint.measureText(numStr);
                if (priceNumLen > maxPriceNumStrLen) {
                    maxPriceNumStr = priceNumStr;
                    maxPriceNumStrLen = priceNumLen;
                }
                if (priceLen > maxPriceStrLen) {
                    maxPriceStr = priceStr;
                    maxPriceStrLen = priceLen;
                }
                if (numLen > maxNumStrLen) {
                    maxNumStr = numStr;
                    maxNumStrLen = numLen;
                }
            }
        }
        int viewWidth = (int)(getMeasuredWidth() * 0.32);
        int totalWeight = maxPriceNumStrLen + maxPriceStrLen + maxNumStrLen;
        int priceNumTextSize = getTextSize(textPaint, (viewWidth * maxPriceNumStrLen / totalWeight), maxPriceNumStr);
        int priceTextSize = getTextSize(textPaint, (viewWidth * maxPriceStrLen / totalWeight), maxPriceStr);
        int numTextSize = getTextSize(textPaint, (viewWidth * maxNumStrLen / totalWeight), maxNumStr);
        return Arrays.asList(maxPriceNumStrLen, priceNumTextSize, maxPriceStrLen, priceTextSize, maxNumStrLen, numTextSize);
    }

    /**
     * 获取合适的字体大小
     *
     * @param textPaint
     * @param width
     * @param text
     * @return
     */
    private int getTextSize(TextPaint textPaint, int width, String text) {
        int ori = (int) textPaint.getTextSize();
        if (width < 0 || null == text || text.isEmpty()) {
            return ori;
        }
        for (int i = 0; i < 10000; i++) {
            textPaint.setTextSize(i + 1);
            if (textPaint.measureText(text) > width) {
                return i;
            }
        }
        return ori;
    }
}
