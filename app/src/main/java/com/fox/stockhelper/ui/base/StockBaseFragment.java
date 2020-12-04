package com.fox.stockhelper.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.fox.stockhelper.constant.StockReceiverConst;

/**
 * 股票Fragment
 *
 * @author lusongsong
 * @date 2020/12/4 17:09
 */
public class StockBaseFragment extends BaseFragment {
    /**
     * 股市
     */
    protected Integer stockMarket;
    /**
     * 股市交易状态
     */
    protected Integer stockMarketDealStatus;
    /**
     * 股市交易状态广播内部接收内
     */
    StockMarketDealStatusInsideReceiver stockMarketDealStatusInsideReceiver;
    /**
     * 是否需要交易状态服务
     */
    protected Boolean needStockMarketDealStatusService = false;

    /**
     * 内部广播接收类
     */
    public class StockMarketDealStatusInsideReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int rStockMarket = bundle.getInt("stockMarket");
            int newStockMarketDealStatus = bundle.getInt("stockMarketDealStatus");
            if (rStockMarket == stockMarket) {
                handleStockMarketDealStatusBroadcast(
                        stockMarketDealStatus,
                        newStockMarketDealStatus
                );
            }
        }
    }

    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public StockBaseFragment(Context context) {
        super(context);
    }


    /**
     * 启动
     */
    @Override
    public void onStart() {
        super.onStart();
        //股市交易状态服务绑定
        if (needStockMarketDealStatusService) {
            stockMarketDealStatusInsideReceiver = new StockMarketDealStatusInsideReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(StockReceiverConst.ACTION_STOCK_MARKET_DEAL_STATUS);
            context.registerReceiver(stockMarketDealStatusInsideReceiver, filter);
        }
    }

    /**
     * 停止
     */
    @Override
    public void onStop() {
        super.onStop();
        //股市交易状态服务解绑
        if (needStockMarketDealStatusService) {
            context.unregisterReceiver(stockMarketDealStatusInsideReceiver);
        }
    }

    /**
     * 处理股市交易状态变化
     *
     * @param oldStatus
     * @param newStatus
     */
    protected void handleStockMarketDealStatusBroadcast(Integer oldStatus, Integer newStatus) {
        stockMarketDealStatus = newStatus;
    }
}
