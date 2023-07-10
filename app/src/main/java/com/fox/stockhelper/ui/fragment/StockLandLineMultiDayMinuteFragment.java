package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.spider.stock.entity.po.sina.SinaMinuteKLineDataPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.base.StockBaseFragment;
import com.fox.stockhelper.ui.view.StockDealMinuteInfoView;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelperchart.StockMultiDayMinuteChart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 近5个交易日的分时数据
 *
 * @author lusongsong
 * @date 2020/10/22 20:38
 */
public class StockLandLineMultiDayMinuteFragment extends StockBaseFragment{
    /**
     * 时间粒度(5分钟)
     */
    private static final int SCALE = 5;
    /**
     * 数据点长度
     */
    private static final int DATA_LEN = 400;
    /**
     * 是否需要刷新数据
     */
    protected boolean dataRefresh = true;
    /**
     * 股票
     */
    private StockVo stockVo;
    /**
     * 是否横屏显示
     */
    private boolean land = true;
    /**
     * 分钟线图交易数据
     */
    List<SinaMinuteKLineDataPo> sinaMinuteKLineDataPoList;
    /**
     * 数据起始位置
     */
    private int startPos = 0;

    @BindView(R.id.stockMinInfoSDMIV)
    StockDealMinuteInfoView stockMinInfoSDMIV;
    /**
     * 多天分钟线图
     */
    @BindView(R.id.stockMultiDayMinuteChart)
    StockMultiDayMinuteChart stockMultiDayMinuteChart;

    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public StockLandLineMultiDayMinuteFragment(Context context, StockVo stockVo) {
        super(context);
        this.stockVo = stockVo;
        stockMarket = stockVo.getStockMarket();
    }

    /**
     * 创建视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_land_line_multi_day_minute, null);
        ButterKnife.bind(this, view);
        needStockMarketDealStatusService = true;
        //初始化
        stockMultiDayMinuteChart.initChart(stockVo);
        return view;
    }

    /**
     * 处理状态变化
     *
     * @param oldStatus
     * @param newStatus
     */
    @Override
    protected void handleStockMarketDealStatusBroadcast(Integer oldStatus, Integer newStatus) {
        super.handleStockMarketDealStatusBroadcast(oldStatus, newStatus);
        if (null != newStatus) {
            dataRefresh = StockMarketStatusConst.CAN_DEAL_STATUS_LIST.contains(newStatus);
            if (!newStatus.equals(oldStatus)) {
                //状态发生变化时保证刷新一次
                startRefreshData();
            }
        }
    }

    /**
     * 开启数据刷新
     */
    private void startRefreshData() {
        //初始化交易价格线图信息
        handleDealPriceLine();
    }

    /**
     * 刷新交易价格线图信息
     */
    private void handleDealPriceLine() {
        Runnable stockDealPriceLineRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    if (dataRefresh) {
                        stockMultiDayMinuteChart.freshData();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        break;
                    }
                }
            }
        };
        Thread thread = new Thread(stockDealPriceLineRunnable);
        thread.start();
    }

    private Map<String, Object> convertToFiveDayChartData() {
        BigDecimal lastClosePrice = null;
        BigDecimal preClosePrice = null;
        Map<String, Object> realTimeChartData = new HashMap<>(2);
        Map<String, List<List<Object>>> dateMinuteData = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<List<Object>> singleDayMinuteDataList = new ArrayList<>();
        String currentDate = null;
        for (SinaMinuteKLineDataPo sinaMinuteKLineDataPo : sinaMinuteKLineDataPoList) {
            if (null == currentDate) {
                currentDate = sinaMinuteKLineDataPo.getDt();
            }
            if (null == preClosePrice) {
                preClosePrice = sinaMinuteKLineDataPo.getClosePrice();
            }
            if (null == lastClosePrice) {
                lastClosePrice = sinaMinuteKLineDataPo.getClosePrice();
            }
            if (!currentDate.equals(sinaMinuteKLineDataPo.getDt())) {
                dateList.add(currentDate);
                preClosePrice = lastClosePrice;
                dateMinuteData.put(currentDate, singleDayMinuteDataList);
                currentDate = sinaMinuteKLineDataPo.getDt();
                singleDayMinuteDataList = new ArrayList<>();
            }
            List<Object> minuteData = new ArrayList<>(5);
            minuteData.add(
                    DateUtil.getDateFromStr(
                            sinaMinuteKLineDataPo.getDt() + " " + sinaMinuteKLineDataPo.getTime(),
                            DateUtil.TIME_FORMAT_1
                    ).getTime()
            );
            minuteData.add(sinaMinuteKLineDataPo.getClosePrice().toString());
            minuteData.add(sinaMinuteKLineDataPo.getOpenPrice().toString());
            minuteData.add(sinaMinuteKLineDataPo.getDealNum().toString());
            minuteData.add(preClosePrice.toString());
            lastClosePrice = sinaMinuteKLineDataPo.getClosePrice();
            singleDayMinuteDataList.add(minuteData);
        }
        dateMinuteData.put(currentDate, singleDayMinuteDataList);
        dateList.add(currentDate);
        List<List<Object>> fiveDayMinuteDataList = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            String date = dateList.get(dateList.size() - i);
            fiveDayMinuteDataList.addAll(dateMinuteData.get(date));
        }
        startPos = sinaMinuteKLineDataPoList.size() - fiveDayMinuteDataList.size();
        realTimeChartData.put("data", fiveDayMinuteDataList);
        realTimeChartData.put("preClose", preClosePrice.toString());
        return realTimeChartData;
    }

    /**
     * 选中
     *
     * @param index
     */
    public void choose(Integer index) {
        int pos = startPos + index;
        SinaMinuteKLineDataPo sinaMinuteKLineDataPo = sinaMinuteKLineDataPoList.get(pos);
        stockMinInfoSDMIV.setDt(sinaMinuteKLineDataPo.getDt());
        stockMinInfoSDMIV.setTime(sinaMinuteKLineDataPo.getTime());
        stockMinInfoSDMIV.setPrice(sinaMinuteKLineDataPo.getClosePrice());
        stockMinInfoSDMIV.setDealNum(sinaMinuteKLineDataPo.getDealNum());
        stockMinInfoSDMIV.setTime(sinaMinuteKLineDataPo.getTime());
        stockMinInfoSDMIV.reDraw();
    }
}
