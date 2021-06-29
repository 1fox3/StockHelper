package com.fox.stockhelper.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.fox.spider.stock.constant.StockConst;
import com.fox.stockhelper.api.stock.stockmarket.AroundDealDateApi;
import com.fox.stockhelper.database.dao.StockMarketAroundDealDateDao;
import com.fox.stockhelper.entity.dto.api.stock.stockmarket.AroundDealDateApiDto;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelper.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;

/**
 * 股市交易日同步任务
 *
 * @author lusongsong
 * @date 2020/12/2 15:03
 */
public class StockMarketAroundDealDateService extends Service {
    /**
     * 更新间隔
     */
    private static final int SYNC_SCOPE = 600000;
    /**
     * 股市交易日数据库操作类
     */
    StockMarketAroundDealDateDao stockMarketAroundDealDateDao;
    /**
     * 股市交易日接口类
     */
    AroundDealDateApi aroundDealDateApi = new AroundDealDateApi();

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
        aroundDealDateApi = new AroundDealDateApi();
        getTaskThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取任务线程
     *
     * @return
     */
    private Thread getTaskThread() {
        Runnable stockMarketAroundDealDateRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    try {
                        String currentDate = DateUtil.getCurrentDate();
                        for (Integer stockMarket : StockConst.SM_ALL) {
                            String lastDealDate =
                                    stockMarketAroundDealDateDao.last(stockMarket);
                            if (currentDate.equals(lastDealDate)) {
                                continue;
                            }
                            //如果今天未到下一个交易日则继续
                            String nextDealDate =
                                    stockMarketAroundDealDateDao.next(stockMarket);
                            if (null != nextDealDate &&
                                    DateUtil.compare(
                                            currentDate,
                                            nextDealDate,
                                            DateUtil.DATE_FORMAT_1
                                    )
                            ) {
                                continue;
                            }
                            Map<String, Object> params = new HashMap<>(1);
                            params.put("stockMarket", stockMarket);
                            aroundDealDateApi.setParams(params);
                            AroundDealDateApiDto aroundDealDateApiDto =
                                    (AroundDealDateApiDto) aroundDealDateApi.request();
                            if (null == aroundDealDateApiDto) {
                                continue;
                            }
                            stockMarketAroundDealDateDao.savePre(
                                    stockMarket, aroundDealDateApiDto.getPre()
                            );
                            stockMarketAroundDealDateDao.saveLast(
                                    stockMarket, aroundDealDateApiDto.getLast()
                            );
                            stockMarketAroundDealDateDao.saveNext(
                                    stockMarket, aroundDealDateApiDto.getNext()
                            );
                        }
                        Thread.sleep(SYNC_SCOPE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.error(e.getMessage());
                    }
                }
            }
        };
        return new Thread(stockMarketAroundDealDateRunnable);
    }
}
