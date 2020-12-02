package com.fox.stockhelper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.stockhelper.database.dao.StockMarketAroundDealDateDao;
import com.fox.stockhelper.database.dao.StockMarketDealStatusDao;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelper.util.LogUtil;

import androidx.annotation.Nullable;
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
     * 股市交易日数据库操作类
     */
    StockMarketAroundDealDateDao stockMarketAroundDealDateDao;
    /**
     * 股市交易状态数据库操作类
     */
    StockMarketDealStatusDao stockMarketDealStatusDao;

    /**
     * 禁止绑定
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
                        if (null == currentDate) {
                            continue;
                        }
                        for (Integer stockMarket : StockConst.SM_ALL) {
                            String lastDealDate =
                                    stockMarketAroundDealDateDao.last(stockMarket);
                            if (null == lastDealDate) {
                                continue;
                            }
                            int dealStatus = StockMarketStatusConst.REST;
                            if (lastDealDate.equals(currentDate)) {
                                dealStatus = StockMarketStatusConst.timeSMStatus(stockMarket);
                            }
                            stockMarketDealStatusDao.updateStatus(stockMarket, dealStatus);
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
