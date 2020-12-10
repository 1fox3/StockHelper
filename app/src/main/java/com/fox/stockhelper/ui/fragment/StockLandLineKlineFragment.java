package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.spider.stock.api.nets.NetsDayDealInfoApi;
import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.po.nets.NetsDayDealInfoPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.offline.DealDayApiDto;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.fox.stockhelper.util.DateUtil;
import com.fox.stockhelper.util.LogUtil;
import com.github.mikephil.charting.stockChart.BaseChart;
import com.github.mikephil.charting.stockChart.KLineChart;
import com.github.mikephil.charting.stockChart.dataManage.KLineDataManage;
import com.github.mikephil.charting.stockChart.dataManage.TimeDataManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 天周月线图
 *
 * @author lusongsong
 * @date 2020/10/22 20:38
 */
public class StockLandLineKlineFragment extends BaseFragment
        implements CommonHandleListener, BaseChart.OnHighlightValueSelectedListener {
    /**
     * 股票id
     */
    StockVo stockVo;
    /**
     * 线图类型
     */
    Integer klineType;
    /**
     * 按天价格数据
     */
    List<NetsDayDealInfoPo> dealDayInfoList;
    boolean land = true;

    @BindView(R.id.stockDealInfoSDIV)
    StockDealInfoView stockDealInfoSDIV;
    @BindView(R.id.stockKLineChart)
    KLineChart stockKLineChart;
    /**
     * 成交分时数据
     */
    private KLineDataManage kLineData;
    private JSONObject object;
    /**
     * 按天价格数据
     */
    List<DealDayApiDto> dealDayApiDtoList;
    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);

    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public StockLandLineKlineFragment(Context context, StockVo stockVo, Integer klineType) {
        super(context);
        this.stockVo = stockVo;
        this.klineType = klineType;
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
        View view = inflater.inflate(R.layout.fragment_stock_land_line_kline, null);
        ButterKnife.bind(this, view);
        kLineData = new KLineDataManage(getActivity());
        //初始化
        stockKLineChart.initChart(land);
        stockKLineChart.setHighlightValueSelectedListener(this);
        //初始化交易价格线图信息
        this.handleDealPriceLine();
        return view;
    }

    /**
     * 消息处理
     *
     * @param message
     */
    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case MsgWhatConfig.STOCK_DEAL_PRICE_LINE:
                String dealPriceDayStr = listToChartData();
                try {
                    try {
                        object = new JSONObject(dealPriceDayStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //上证指数代码000001.IDX.SH
                    kLineData.parseKlineData(object, "000001.IDX.SH", land);
                    stockKLineChart.setDataToChart(kLineData);
                    choose(dealDayInfoList.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 数据转图标
     *
     * @return
     */
    private String listToChartData() {
        if (null == dealDayInfoList) {
            return null;
        }
        List chartDataList = new ArrayList();
        List<BigDecimal> closePriceList = new ArrayList<>(60);
        List<Integer> maList = Arrays.asList(5, 10, 20, 30, 60);
        Map<Integer, BigDecimal> sumClosePriceMap = new HashMap<>(maList.size());
        for (Integer ma : maList) {
            sumClosePriceMap.put(ma, new BigDecimal(0));
        }
        for (int i = 0; i < dealDayInfoList.size(); i++) {
            NetsDayDealInfoPo netsDayDealInfoPo = dealDayInfoList.get(i);
            if (null != netsDayDealInfoPo) {
                List<Object> dataList = new ArrayList<>();
                dataList.add(DateUtil.getDateFromStr(netsDayDealInfoPo.getDt()).getTime());
                dataList.add(netsDayDealInfoPo.getOpenPrice().toString());
                dataList.add(netsDayDealInfoPo.getHighestPrice().toString());
                dataList.add(netsDayDealInfoPo.getLowestPrice().toString());
                dataList.add(netsDayDealInfoPo.getClosePrice().toString());
                dataList.add(netsDayDealInfoPo.getDealNum().toString());
                dataList.add(netsDayDealInfoPo.getDealMoney().toString());
                closePriceList.add(netsDayDealInfoPo.getClosePrice());
                for (Integer ma : maList) {
                    BigDecimal sumPrice = sumClosePriceMap.get(ma);
                    sumPrice = sumPrice.add(netsDayDealInfoPo.getClosePrice());
                    if (ma <= closePriceList.size()) {
                        sumPrice = sumPrice.subtract(
                                closePriceList.get(closePriceList.size() - ma)
                        );
                        dataList.add(sumPrice.divide(
                                new BigDecimal(ma), 2, RoundingMode.HALF_UP)
                        );
                    } else {
                        dataList.add(sumPrice.divide(
                                new BigDecimal(closePriceList.size()), 2, RoundingMode.HALF_UP)
                        );
                    }
                    sumClosePriceMap.put(ma, sumPrice);
                }
                dataList.add(netsDayDealInfoPo.getPreClosePrice().toString());
                chartDataList.add(dataList);
            }
        }
        Map<String, List> priceDayMap = new HashMap<>(1);
        priceDayMap.put("data", chartDataList);
        return com.alibaba.fastjson.JSONObject.toJSONString(priceDayMap);
    }

    /**
     * 刷新交易价格线图信息
     */
    private void handleDealPriceLine() {
        Runnable stockPriceDayRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                String currentDate = DateUtil.getCurrentDate();
                NetsDayDealInfoApi netsDayDealInfoApi = new NetsDayDealInfoApi();
                List<NetsDayDealInfoPo> netsDayDealInfoPoList = netsDayDealInfoApi.dayDealInfo(
                        stockVo, "1990-12-01", currentDate
                );
                handleData(netsDayDealInfoPoList);
                Message msg = new Message();
                msg.what = MsgWhatConfig.STOCK_DEAL_PRICE_LINE;
                handler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(stockPriceDayRunnable);
        thread.start();
    }

    private void handleData(List<NetsDayDealInfoPo> netsDayDealInfoPoList) {
        if (klineType == StockConst.DT_DAY) {
            dealDayInfoList = netsDayDealInfoPoList;
            return;
        }
        try {
            List<String> dateScopeList = doDateByStatisticsType();
            if (null == dateScopeList || dateScopeList.isEmpty()) {
                return;
            }
            Integer dateScopePos = 0;
            Boolean changeScope = false;
            String dt = null, scopeStartDate = null, scopeEndDate = null;
            BigDecimal preClosePrice =null, openPrice = null, lowestPrice = null, highestPrice = null, closePrice = null;
            BigDecimal dealMoney = null, circValue = null, totalValue = null;
            String stockCode = "", stockName = "";
            Long dealNum = null;
            NetsDayDealInfoPo scopeNetsDayDealInfoPo = new NetsDayDealInfoPo();
            List<NetsDayDealInfoPo> scopeNetsDayDealInfoPoList = new ArrayList<>();
            for (NetsDayDealInfoPo netsDayDealInfoPo : netsDayDealInfoPoList) {
                if (null == netsDayDealInfoPo) {
                    continue;
                }
                stockCode = netsDayDealInfoPo.getStockCode();
                stockName = netsDayDealInfoPo.getStockName();
                //忽略当日无交易的脏数据
                if (null != netsDayDealInfoPo.getClosePrice()
                        && 0 == BigDecimal.ZERO.compareTo(netsDayDealInfoPo.getClosePrice())) {
                    continue;
                }
                try {
                    while (null == scopeStartDate || null == scopeEndDate
                            || DateUtil.compare(scopeEndDate, netsDayDealInfoPo.getDt(), DateUtil.DATE_FORMAT_1)) {
                        changeScope = true;
                        if (dateScopePos > dateScopeList.size() - 1) {
                            break;
                        }
                        scopeStartDate = dateScopeList.get(dateScopePos);
                        scopeEndDate = dateScopeList.get(dateScopePos + 1);
                        dateScopePos += 2;
                    }
                    if (changeScope) {
                        changeScope = false;
                        scopeNetsDayDealInfoPo = new NetsDayDealInfoPo();
                        scopeNetsDayDealInfoPo.setStockCode(stockCode);
                        scopeNetsDayDealInfoPo.setStockName(stockName);
                        if (null != closePrice) {
                            scopeNetsDayDealInfoPo.setDt(dt);
                            scopeNetsDayDealInfoPo.setPreClosePrice(preClosePrice);
                            scopeNetsDayDealInfoPo.setOpenPrice(openPrice);
                            scopeNetsDayDealInfoPo.setClosePrice(closePrice);
                            scopeNetsDayDealInfoPo.setHighestPrice(highestPrice);
                            scopeNetsDayDealInfoPo.setLowestPrice(lowestPrice);
                            scopeNetsDayDealInfoPo.setDealNum(dealNum);
                            scopeNetsDayDealInfoPo.setDealMoney(dealMoney);
                            scopeNetsDayDealInfoPo.setCircValue(circValue);
                            scopeNetsDayDealInfoPo.setTotalValue(totalValue);
                            scopeNetsDayDealInfoPoList.add(scopeNetsDayDealInfoPo);
                        }
                        preClosePrice = closePrice;
                        openPrice = null;
                        lowestPrice = null;
                        highestPrice = null;
                        closePrice = null;
                        dealMoney = null;
                        dealNum = null;
                        circValue = null;
                        totalValue = null;
                    }
                    dt = netsDayDealInfoPo.getDt();
                    preClosePrice = null == preClosePrice ? netsDayDealInfoPo.getOpenPrice() : preClosePrice;
                    openPrice = null == openPrice ? netsDayDealInfoPo.getOpenPrice() : openPrice;
                    lowestPrice = null == lowestPrice || (
                            null != netsDayDealInfoPo.getLowestPrice()
                                    && 0 > netsDayDealInfoPo.getLowestPrice().compareTo(lowestPrice)
                    ) ? netsDayDealInfoPo.getLowestPrice() : highestPrice;
                    highestPrice = null == highestPrice || (
                            null != netsDayDealInfoPo.getHighestPrice()
                                    && 0 > highestPrice.compareTo(netsDayDealInfoPo.getHighestPrice())
                    ) ? netsDayDealInfoPo.getHighestPrice() : highestPrice;
                    closePrice = null == closePrice || (
                            null != netsDayDealInfoPo.getClosePrice()
                                    && 0 > BigDecimal.ZERO.compareTo(netsDayDealInfoPo.getClosePrice())
                    ) ? netsDayDealInfoPo.getClosePrice() : closePrice;
                    dealMoney = null == dealMoney ?
                            netsDayDealInfoPo.getDealMoney() : dealMoney.add(netsDayDealInfoPo.getDealMoney());
                    dealNum = null == dealNum ?
                            netsDayDealInfoPo.getDealNum() : dealNum + netsDayDealInfoPo.getDealNum();
                    circValue = netsDayDealInfoPo.getCircValue();
                    totalValue = netsDayDealInfoPo.getTotalValue();
                } catch (ParseException e) {
                    LogUtil.error(e.getMessage());
                }
            }
            //处理最后一周的数据
            if (null != closePrice) {
                scopeNetsDayDealInfoPo = new NetsDayDealInfoPo();
                scopeNetsDayDealInfoPo.setStockCode(stockCode);
                scopeNetsDayDealInfoPo.setStockName(stockName);
                scopeNetsDayDealInfoPo.setDt(dt);
                scopeNetsDayDealInfoPo.setPreClosePrice(preClosePrice);
                scopeNetsDayDealInfoPo.setOpenPrice(openPrice);
                scopeNetsDayDealInfoPo.setClosePrice(closePrice);
                scopeNetsDayDealInfoPo.setHighestPrice(highestPrice);
                scopeNetsDayDealInfoPo.setLowestPrice(lowestPrice);
                scopeNetsDayDealInfoPo.setDealNum(dealNum);
                scopeNetsDayDealInfoPo.setDealMoney(dealMoney);
                scopeNetsDayDealInfoPo.setCircValue(circValue);
                scopeNetsDayDealInfoPo.setTotalValue(totalValue);
                scopeNetsDayDealInfoPoList.add(scopeNetsDayDealInfoPo);
            }
            dealDayInfoList = scopeNetsDayDealInfoPoList;
        } catch (ParseException e) {
            LogUtil.error(e.getMessage());
        }
    }

    /**
     * 获取周月范围列表
     *
     * @return
     * @throws ParseException
     */
    public List<String> doDateByStatisticsType() throws ParseException {
        List<String> listWeekOrMonth = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = "1990-12-01";
        String endDate = DateUtil.getCurrentDate();
        Date sDate = dateFormat.parse(startDate);
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        sCalendar.setTime(sDate);
        Date eDate = dateFormat.parse(endDate);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        eCalendar.setTime(eDate);
        boolean bool = true;
        switch (klineType) {
            case StockConst.DT_WEEK:
                while (sCalendar.getTime().getTime() < eCalendar.getTime().getTime()) {
                    if (bool || sCalendar.get(Calendar.DAY_OF_WEEK) == 2 || sCalendar.get(Calendar.DAY_OF_WEEK) == 1) {
                        listWeekOrMonth.add(dateFormat.format(sCalendar.getTime()));
                        bool = false;
                    }
                    sCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                if (listWeekOrMonth.size() % 2 != 0) {
                    listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                }
                break;
            case StockConst.DT_MONTH:
                while (sCalendar.getTime().getTime() < eCalendar.getTime().getTime()) {
                    if (bool || sCalendar.get(Calendar.DAY_OF_MONTH) == 1 || sCalendar.get(Calendar.DAY_OF_MONTH) == sCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        listWeekOrMonth.add(dateFormat.format(sCalendar.getTime()));
                        bool = false;
                    }
                    sCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                if (listWeekOrMonth.size() % 2 != 0) {
                    listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
                }
                break;

        }
        return listWeekOrMonth;
    }

    /**
     * 选中
     *
     * @param index
     */
    public void choose(Integer index) {
        DealInfoApiDto dealInfoApiDto = new DealInfoApiDto();
        NetsDayDealInfoPo netsDayDealInfoPo = dealDayInfoList.get(index);
        dealInfoApiDto.setCurrentPrice(netsDayDealInfoPo.getClosePrice());
        dealInfoApiDto.setPreClosePrice(netsDayDealInfoPo.getPreClosePrice());
        dealInfoApiDto.setOpenPrice(netsDayDealInfoPo.getOpenPrice());
        dealInfoApiDto.setHighestPrice(netsDayDealInfoPo.getHighestPrice());
        dealInfoApiDto.setLowestPrice(netsDayDealInfoPo.getLowestPrice());
        dealInfoApiDto.setDealNum(netsDayDealInfoPo.getDealNum());
        dealInfoApiDto.setDealMoney(netsDayDealInfoPo.getDealMoney());
        stockDealInfoSDIV.setData(dealInfoApiDto).reDraw();
    }

    @Override
    public void onDayHighlightValueListener(TimeDataManage mData, int index, boolean isSelect) {
        choose(index);
    }

    @Override
    public void onKHighlightValueListener(KLineDataManage data, int index, boolean isSelect) {
        choose(index);
    }
}
