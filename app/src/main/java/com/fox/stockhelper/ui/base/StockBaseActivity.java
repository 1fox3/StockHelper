package com.fox.stockhelper.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.fox.stockhelper.constant.StockReceiverConst;

/**
 * 股票Activity
 *
 * @author lusongsong
 * @date 2020/12/4 17:07
 */
public class StockBaseActivity extends BaseActivity {
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
    StockBaseActivity.StockMarketDealStatusInsideReceiver stockMarketDealStatusInsideReceiver;
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
     * 启动
     */
    @Override
    public void onStart() {
        super.onStart();
        //股市交易状态服务绑定
        if (needStockMarketDealStatusService) {
            stockMarketDealStatusInsideReceiver = new StockBaseActivity.StockMarketDealStatusInsideReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(StockReceiverConst.ACTION_STOCK_MARKET_DEAL_STATUS);
            this.registerReceiver(stockMarketDealStatusInsideReceiver, filter);
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
            this.unregisterReceiver(stockMarketDealStatusInsideReceiver);
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
