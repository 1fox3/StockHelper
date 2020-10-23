package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fox.stockhelper.R;
import com.fox.stockhelper.api.stock.realtime.DealPriceLineApi;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealPriceLineApiDto;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.StockDealMinuteInfoView;
import com.github.mikephil.charting.stockChart.BaseChart;
import com.github.mikephil.charting.stockChart.OneDayChart;
import com.github.mikephil.charting.stockChart.dataManage.KLineDataManage;
import com.github.mikephil.charting.stockChart.dataManage.TimeDataManage;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * @author lusongsong
 * @date 2020/10/22 20:39
 */
public class StockLandLineRealtimeFragment extends BaseFragment
        implements CommonHandleListener, BaseChart.OnHighlightValueSelectedListener {
    boolean land = true;
    /**
     * 股票id
     */
    Integer stockId;
    @BindView(R.id.stockMinInfoSDMIV)
    StockDealMinuteInfoView stockMinInfoSDMIV;
    @BindView(R.id.stockRealtimeODC)
    OneDayChart stockRealtimeODC;
    /**
     * 成交分时数据
     */
    private TimeDataManage kTimeData = new TimeDataManage();
    private org.json.JSONObject object;
    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);
    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public StockLandLineRealtimeFragment(Context context) {
        super(context);
    }
    public StockLandLineRealtimeFragment(Context context, int stockId) {
        super(context);
        this.stockId = stockId;
    }

    /**
     * 创建视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_land_line_realtime, null);
        ButterKnife.bind(this, view);
        //初始化
        stockRealtimeODC.initChart(land);
        stockRealtimeODC.setHighlightValueSelectedListener(this);
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
        Bundle bundle = message.getData();
        switch (message.what) {
            case MsgWhatConfig.STOCK_DEAL_PRICE_LINE:
                String dealPriceLineApiDtoStr = bundle.getString("stockDealPriceLine");
                try {
                    DealPriceLineApiDto dealPriceLineApiDto =
                            new ObjectMapper()
                                    .readValue(dealPriceLineApiDtoStr, DealPriceLineApiDto.class);
                    object = new org.json.JSONObject(com.alibaba.fastjson.JSONObject.toJSONString(dealPriceLineApiDto.convertToRealTimeChartData()));
                    Log.e("StockDealLineRealtimeFragment", com.alibaba.fastjson.JSONObject.toJSONString(dealPriceLineApiDto.convertToRealTimeChartData()));
                    //上证指数代码000001.IDX.SH
                    kTimeData.parseTimeData(object,"000001.IDX.SH",0);
                    stockRealtimeODC.setDataToChart(kTimeData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 刷新交易价格线图信息
     */
    private void handleDealPriceLine() {
        Runnable stockDealPriceLineRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                DealPriceLineApi dealPriceLineApi = new DealPriceLineApi();
                Map<String, Object> params = new HashMap<>();
                params.put("stockId", stockId);
                dealPriceLineApi.setParams(params);
                DealPriceLineApiDto dealPriceLineApiDto = (DealPriceLineApiDto)dealPriceLineApi.request();
                Message msg = new Message();
                msg.what = MsgWhatConfig.STOCK_DEAL_PRICE_LINE;
                Bundle bundle = new Bundle();
                bundle.putString("stockDealPriceLine",
                        com.alibaba.fastjson.JSONObject.toJSONString(dealPriceLineApiDto)
                );
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(stockDealPriceLineRunnable);
        thread.start();
    }

    /**
     * 选中
     * @param index
     */
    public void choose(Integer index) {
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
