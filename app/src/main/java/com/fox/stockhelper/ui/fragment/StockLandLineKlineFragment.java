package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.stockhelper.R;
import com.fox.stockhelper.api.BaseApi;
import com.fox.stockhelper.api.stock.offline.DealDayApi;
import com.fox.stockhelper.api.stock.offline.DealMonthApi;
import com.fox.stockhelper.api.stock.offline.DealWeekApi;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.offline.DealDayApiDto;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.github.mikephil.charting.stockChart.BaseChart;
import com.github.mikephil.charting.stockChart.KLineChart;
import com.github.mikephil.charting.stockChart.dataManage.KLineDataManage;
import com.github.mikephil.charting.stockChart.dataManage.TimeDataManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * @author lusongsong
 * @date 2020/10/22 20:38
 */
public class StockLandLineKlineFragment extends BaseFragment
        implements CommonHandleListener, BaseChart.OnHighlightValueSelectedListener {
    public static
    /**
     * 股票id
     */
    Integer stockId;
    /**
     * 线图类型
     */
    Integer klineType;

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
    public StockLandLineKlineFragment(Context context) {
        super(context);
    }
    public StockLandLineKlineFragment(Context context, Integer stockId, Integer klineType) {
        super(context);
        this.stockId = stockId;
        this.klineType = klineType;
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
        Bundle bundle = message.getData();
        switch (message.what) {
            case MsgWhatConfig.STOCK_DEAL_PRICE_LINE:
                String dealPriceDayStr = bundle.getString("stockDealPriceDayLine");
                Log.e("stockDealPriceDayLine", dealPriceDayStr);
                try {
                    try {
                        object = new JSONObject(dealPriceDayStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //上证指数代码000001.IDX.SH
                    kLineData.parseKlineData(object,"000001.IDX.SH", land);
                    stockKLineChart.setDataToChart(kLineData);
                    choose(dealDayApiDtoList.size() - 1);
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
        Runnable stockPriceDayRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                BaseApi dealDayApi;
                switch (klineType) {
                    case 4:
                        dealDayApi = new DealWeekApi();
                        break;
                    case 5:
                        dealDayApi = new DealMonthApi();
                        break;
                    default:
                        dealDayApi = new DealDayApi();
                }
                Map<String, Object> params = new HashMap<>();
                params.put("stockId", stockId);
                dealDayApi.setParams(params);
                dealDayApiDtoList = (List<DealDayApiDto>)dealDayApi.request();
                Log.e("stockDealPriceDayLine", String.valueOf(dealDayApiDtoList.size()));
                Map<String, List> priceDayMap = new HashMap<>(1);
                priceDayMap.put("data", DealDayApiDto.listToChartData(dealDayApiDtoList));
                Message msg = new Message();
                msg.what = MsgWhatConfig.STOCK_DEAL_PRICE_LINE;
                Bundle bundle = new Bundle();
                bundle.putString("stockDealPriceDayLine",
                        com.alibaba.fastjson.JSONObject.toJSONString(priceDayMap)
                );
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(stockPriceDayRunnable);
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
