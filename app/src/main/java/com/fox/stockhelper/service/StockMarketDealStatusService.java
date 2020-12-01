package com.fox.stockhelper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fox.spider.stock.constant.StockConst;
import com.fox.stockhelper.api.stock.stockmarket.AroundDealDateApi;
import com.fox.stockhelper.database.bean.StockMarketAroundDealDateBean;
import com.fox.stockhelper.database.dao.StockMarketAroundDealDateDao;
import com.fox.stockhelper.entity.dto.api.stock.stockmarket.AroundDealDateApiDto;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelper.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import lombok.SneakyThrows;

/**
 * 股市交易状态同步任务
 *
 * @author lusongsong
 * @date 2020/11/30 14:40
 */
public class StockMarketDealStatusService extends Service {
    private Context context;
    /**
     * 禁止绑定
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //记录生命周期日志
        LogUtil.error("lifeCycleLog");
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
        //记录生命周期日志
        LogUtil.error("lifeCycleLog");
        context = this;
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
                        StockMarketAroundDealDateDao stockMarketAroundDealDateDao =
                                new StockMarketAroundDealDateDao(context);
                        String currentDate = DateUtil.getCurrentDate();
                        AroundDealDateApi aroundDealDateApi = new AroundDealDateApi();
                        for (Integer stockMarket : StockConst.SM_ALL) {
                            StockMarketAroundDealDateBean stockMarketAroundDealDateBean =
                                    stockMarketAroundDealDateDao.last(stockMarket);
                            if (null != stockMarketAroundDealDateBean &&
                                    currentDate.equals(stockMarketAroundDealDateBean.getDealDate())) {
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
                        Thread.sleep(3600000);
                    } catch (Exception e) {
                        StackTraceElement[] stackTraceElements = e.getStackTrace();
                        for (StackTraceElement stackTraceElement : stackTraceElements) {
                            LogUtil.error(stackTraceElement.getClassName() + "/" + stackTraceElement.getMethodName() + "/" + stackTraceElement.getLineNumber());
                        }
                        e.getStackTrace();
                        LogUtil.error(e.getMessage());
                    }
                }
            }
        };
        return new Thread(stockMarketDealStatusRunnable);
    }
}
