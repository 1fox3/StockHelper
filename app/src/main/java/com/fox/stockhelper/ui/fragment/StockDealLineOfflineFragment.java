package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.api.stock.offline.DealDayApi;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.offline.DealDayApiDto;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;
import com.fox.stockhelper.ui.activity.StockAllKlineLandActivity;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.ui.chart.custom.StockKLineChart;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.listener.ListChooseListener;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.github.mikephil.charting.stockChart.dataManage.KLineDataManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 股市离线成交线图
 * @author lusongsong
 * @date 2020/9/14 15:58
 */
public class StockDealLineOfflineFragment extends BaseFragment implements CommonHandleListener, ListChooseListener {
    /**
     * 股票id
     */
    private Integer stockId;

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
    public StockDealLineOfflineFragment(Context context) {
        super(context);
    }
    public StockDealLineOfflineFragment(Context context, int stockId) {
        super(context);
        this.stockId = stockId;
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
                bundle.putInt("stockId", stockId);
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
                DealDayApi dealDayApi = new DealDayApi();
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
     *
     * @param index
     */
    @Override
    public void choose(Integer index) {
        DealInfoApiDto dealInfoApiDto = new DealInfoApiDto();
        DealDayApiDto dealDayApiDto = dealDayApiDtoList.get(index);
        dealInfoApiDto.setCurrentPrice(dealDayApiDto.getClosePrice());
        dealInfoApiDto.setPreClosePrice(dealDayApiDto.getPreClosePrice());
        Log.e("preClosePrice", dealDayApiDto.getPreClosePrice().toString());
        dealInfoApiDto.setOpenPrice(dealDayApiDto.getOpenPrice());
        dealInfoApiDto.setHighestPrice(dealDayApiDto.getHighestPrice());
        dealInfoApiDto.setLowestPrice(dealDayApiDto.getLowestPrice());
        dealInfoApiDto.setDealNum(dealDayApiDto.getDealNum());
        dealInfoApiDto.setDealMoney(dealDayApiDto.getDealMoney());
        stockDealInfoSDIV.setData(dealInfoApiDto).reDraw();
    }
}
