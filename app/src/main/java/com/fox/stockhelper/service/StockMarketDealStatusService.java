package com.fox.stockhelper.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.stockhelper.constant.StockReceiverConst;
import com.fox.stockhelper.database.dao.StockMarketAroundDealDateDao;
import com.fox.stockhelper.database.dao.StockMarketDealStatusDao;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelper.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;

/**
 * 股市交易状态同步任务
 *
 * @author lusongsong
 * @date 2020/12/2 15:03
 */
public class StockMarketDealStatusService extends Service {
    /**
     * 更新间隔
     */
    private static final int SYNC_SCOPE = 1000;
    /**
     * 交易状态
     */
    private static final Map<Integer, Integer> stockMarketDealStatusMap = new HashMap<>();
    /**
     * 股市交易日数据库操作类
     */
    StockMarketAroundDealDateDao stockMarketAroundDealDateDao;
    /**
     * 股市交易状态数据库操作类
     */
    StockMarketDealStatusDao stockMarketDealStatusDao;
    /**
     * 服务绑定
     */
    IBinder iBinder = new StockMarketDealStatusServiceBinder();

    /**
     * 服务绑定类
     */
    public class StockMarketDealStatusServiceBinder extends Binder {
        /**
         * 获取当前交易状态
         *
         * @return
         */
        Integer getDealStatus(Integer stockMarket) {
            return stockMarketDealStatusMap.containsKey(stockMarket) ?
                    stockMarketDealStatusMap.get(stockMarket) : null;
        }
    }

    /**
     * 禁止绑定
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    /**
     * 执行任务
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stockMarketAroundDealDateDao = new StockMarketAroundDealDateDao(this);
        stockMarketDealStatusDao = new StockMarketDealStatusDao(this);
        getTaskThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取任务线程
     *
     * @return
     */
    private Thread getTaskThread() {
        Runnable stockMarketDealStatusRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    try {
                        String currentDate = DateUtil.getCurrentDate();
                        for (Integer stockMarket : StockConst.SM_ALL) {
                            String lastDealDate =
                                    stockMarketAroundDealDateDao.last(stockMarket);
                            if (null == lastDealDate) {
                                continue;
                            }
                            Integer dealStatus = StockMarketStatusConst.REST;
                            if (lastDealDate.equals(currentDate)) {
                                dealStatus = StockMarketStatusConst.timeSMStatus(stockMarket);
                            }
                            if (null != dealStatus) {
                                //广播
                                Intent intent = new Intent();
                                intent.putExtra("stockMarket", stockMarket);
                                intent.putExtra("stockMarketDealStatus", dealStatus);
                                intent.setAction(StockReceiverConst.ACTION_STOCK_MARKET_DEAL_STATUS);
                                sendBroadcast(intent);
                                //本地存储
                                stockMarketDealStatusMap.put(stockMarket, dealStatus);
                                //数据库存储
                                stockMarketDealStatusDao.updateStatus(stockMarket, dealStatus);
                            }
                        }
                        Thread.sleep(SYNC_SCOPE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.error(e.getMessage());
                    }
                }
            }
        };
        return new Thread(stockMarketDealStatusRunnable);
    }
}
