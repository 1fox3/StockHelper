package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fox.spider.stock.api.nets.NetsDayDealInfoApi;
import com.fox.spider.stock.entity.po.nets.NetsDayDealInfoPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;
import com.fox.stockhelper.ui.activity.StockAllKlineLandActivity;
import com.fox.stockhelper.ui.base.StockBaseFragment;
import com.fox.stockhelper.ui.chart.custom.StockKLineChart;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.listener.ListChooseListener;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.fox.stockhelper.util.DateUtil;
import com.github.mikephil.charting.stockChart.dataManage.KLineDataManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 股市离线成交线图
 *
 * @author lusongsong
 * @date 2020/9/14 15:58
 */
public class StockDealLineOfflineFragment extends StockBaseFragment implements CommonHandleListener, ListChooseListener {
    /**
     * 股票编码
     */
    String stockCode;

    @BindView(R.id.stockDealInfoSDIV)
    StockDealInfoView stockDealInfoSDIV;
    /**
     * 单谈表格
     */
    @BindView(R.id.stockKLineChart)
    StockKLineChart stockKLineChart;
    /**
     * 查看所以线图图片
     */
    @BindView(R.id.allKlineIV)
    ImageView allKlineIV;
    private int mType = 1;//日K：1；周K：7；月K：30
    private boolean land = false;//是否横屏
    private KLineDataManage kLineData;
    private JSONObject object;
    /**
     * 按天价格数据
     */
    List<NetsDayDealInfoPo> dealDayInfoList;

    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);

    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public StockDealLineOfflineFragment(Context context, StockVo stockVo) {
        super(context);
        if (null != stockVo) {
            stockMarket = stockVo.getStockMarket();
            stockCode = stockVo.getStockCode();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_deal_line_offline, null);
        ButterKnife.bind(this, view);
        kLineData = new KLineDataManage(getActivity());
        //初始化
        stockKLineChart.initChart(land);
        stockKLineChart.setListChooseListener(this);
        //初始化交易价格线图信息
        this.handleDealPriceLine();
        allKlineIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StockAllKlineLandActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("stockMarket", stockMarket);
                bundle.putString("stockCode", stockCode);
                intent.putExtra("stock", bundle);
                getContext().startActivity(intent);
            }
        });
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
                dealDayInfoList = netsDayDealInfoApi.dayDealInfo(
                        new StockVo(stockCode, stockMarket), "1990-01-01", currentDate
                );
                Message msg = new Message();
                msg.what = MsgWhatConfig.STOCK_DEAL_PRICE_LINE;
                handler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(stockPriceDayRunnable);
        thread.start();
    }

    /**
     * 选中
     *
     * @param index
     */
    @Override
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
}
