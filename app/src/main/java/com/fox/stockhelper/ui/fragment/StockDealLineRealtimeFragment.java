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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.spider.stock.util.StockMarketStatusUtil;
import com.fox.stockhelper.R;
import com.fox.stockhelper.api.stock.realtime.DealInfoApi;
import com.fox.stockhelper.api.stock.realtime.DealPriceLineApi;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealPriceLineApiDto;
import com.fox.stockhelper.ui.activity.StockAllKlineLandActivity;
import com.fox.stockhelper.ui.adapter.recyclerview.StockTopDealPriceAdapter;
import com.fox.stockhelper.ui.base.BaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.fox.stockhelper.util.DateUtil;
import com.github.mikephil.charting.stockChart.OneDayChart;
import com.github.mikephil.charting.stockChart.dataManage.TimeDataManage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 股市实时成交线图
 * @author lusongsong
 * @date 2020/9/14 15:57
 */
public class StockDealLineRealtimeFragment extends BaseFragment implements CommonHandleListener {
    /**
     * 交易信息
     */
    @BindView(R.id.stockDealInfoSDIV)
    StockDealInfoView stockDealInfoSDIV;
    /**
     * 单天分钟线图
     */
    @BindView(R.id.stockOneDayChart)
    OneDayChart stockOneDayChart;
    /**
     * Top售价
     */
    @BindView(R.id.sellStockTopDealPriceRV)
    RecyclerView sellStockTopDealPriceRV;
    /**
     * Top买价
     */
    @BindView(R.id.buyStockTopDealPriceRV)
    RecyclerView buyStockTopDealPriceRV;
    /**
     * 查看所以线图图片
     */
    @BindView(R.id.allKlineIV)
    ImageView allKlineIV;
    /**
     * 股票id
     */
    private Integer stockId;
    /**
     * Top售价适配器
     */
    StockTopDealPriceAdapter sellStockTopDealPriceAdapter;
    /**
     * Top买价适配器
     */
    StockTopDealPriceAdapter buyStockTopDealPriceAdapter;
    /**
     * 是否为横屏
     */
    private boolean land = false;//是否横屏
    /**
     * 成交分时数据
     */
    private TimeDataManage kTimeData = new TimeDataManage();
    private JSONObject object;
    /**
     *  股市状态
     */
    int smStatus = StockMarketStatusConst.OPEN;
    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);

    /**
     * 构造函数
     * @param context
     */
    public StockDealLineRealtimeFragment(Context context) {
        super(context);
    }

    public StockDealLineRealtimeFragment(Context context, int stockId) {
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
        View view = inflater.inflate(R.layout.fragment_stock_deal_line_realtime, null);
        ButterKnife.bind(this, view);
        //初始化
        stockOneDayChart.initChart(land);
        //更新股市状态
        this.handleStockMarketStatus();
        //初始化交易信息
        this.handleDealInfo();
        //初始化交易价格线图信息
        this.handleDealPriceLine();
        //初始化TOP售价
        sellStockTopDealPriceAdapter = new StockTopDealPriceAdapter();
        LinearLayoutManager sellLinearLayoutManager = new LinearLayoutManager(this.getContext());
        sellLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        sellStockTopDealPriceRV.setLayoutManager(sellLinearLayoutManager);
        //初始化TOP买价
        buyStockTopDealPriceAdapter = new StockTopDealPriceAdapter();
        buyStockTopDealPriceAdapter.setPriceType(StockTopDealPriceAdapter.TOP_PRICE_TYPE_BUY);
        LinearLayoutManager buyLinearLayoutManager = new LinearLayoutManager(this.getContext());
        buyLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        buyStockTopDealPriceRV.setLayoutManager(buyLinearLayoutManager);
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
            case MsgWhatConfig.SM_STATUS:
                smStatus = bundle.getInt("smStatus");
                break;
            case MsgWhatConfig.STOCK_DEAL_INFO:
                String stockDealInfoStr = bundle.getString("stockDealInfo");
                try {
                    DealInfoApiDto dealInfoApiDto =
                            new ObjectMapper().readValue(stockDealInfoStr, DealInfoApiDto.class);
                    //交易信息
                    stockDealInfoSDIV.setData(dealInfoApiDto).reDraw();
                    //TOP售价
                    sellStockTopDealPriceAdapter.clearData();
                    sellStockTopDealPriceAdapter.setPreClosePrice(dealInfoApiDto.getPreClosePrice());
                    sellStockTopDealPriceAdapter.addData(dealInfoApiDto.getSellPriceList());
                    sellStockTopDealPriceRV.setAdapter(sellStockTopDealPriceAdapter);
                    //TOP买价
                    buyStockTopDealPriceAdapter.clearData();
                    buyStockTopDealPriceAdapter.setPreClosePrice(dealInfoApiDto.getPreClosePrice());
                    buyStockTopDealPriceAdapter.addData(dealInfoApiDto.getBuyPriceList());
                    buyStockTopDealPriceRV.setAdapter(buyStockTopDealPriceAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MsgWhatConfig.STOCK_DEAL_PRICE_LINE:
                String dealPriceLineApiDtoStr = bundle.getString("stockDealPriceLine");
                try {
                    DealPriceLineApiDto dealPriceLineApiDto =
                            new ObjectMapper()
                                    .readValue(dealPriceLineApiDtoStr, DealPriceLineApiDto.class);
                    object = new JSONObject(com.alibaba.fastjson.JSONObject.toJSONString(dealPriceLineApiDto.convertToRealTimeChartData()));
                    Log.e("StockDealLineRealtimeFragment", com.alibaba.fastjson.JSONObject.toJSONString(dealPriceLineApiDto.convertToRealTimeChartData()));
                    //上证指数代码000001.IDX.SH
                    kTimeData.parseTimeData(object,"000001.IDX.SH",0);
                    stockOneDayChart.setDataToChart(kTimeData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 开启定时检查交易状态
     */
    private void handleStockMarketStatus() {
        Runnable stockMarketStatusRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    int smStatus = StockMarketStatusUtil.currentSMStatus(1);
                    Message msg = new Message();
                    msg.what = MsgWhatConfig.SM_STATUS;
                    Bundle bundle = new Bundle();
                    bundle.putInt("smStatus", smStatus);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    int minute = Integer.valueOf(DateUtil.getCurrentDate(DateUtil.MINUTE_FORMAT_1));
                    int second = Integer.valueOf(DateUtil.getCurrentDate(DateUtil.SECOND_FORMAT_1));
                    int s = 300 - second - (minute % 5) * 60;
                    Thread.sleep(s * 1000);
                }
            }
        };
        new Thread(stockMarketStatusRunnable).start();
    }

    /**
     * 刷新交易信息
     */
    private void handleDealInfo() {
        Runnable stockDealInfoRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    if (smStatus == StockMarketStatusConst.OPEN
                            || smStatus == StockMarketStatusConst.COMPETE
                    ) {
                        DealInfoApi dealInfoApi = new DealInfoApi();
                        Map<String, Object> params = new HashMap<>();
                        params.put("stockId", stockId);
                        dealInfoApi.setParams(params);
                        DealInfoApiDto dealInfoApiDto = (DealInfoApiDto)dealInfoApi.request();
                        Message msg = new Message();
                        msg.what = MsgWhatConfig.STOCK_DEAL_INFO;
                        Bundle bundle = new Bundle();
                        bundle.putString("stockDealInfo",
                                com.alibaba.fastjson.JSONObject.toJSONString(dealInfoApiDto)
                        );
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        Thread.sleep(3000);
                    }
                }
            }
        };
        Thread thread = new Thread(stockDealInfoRunnable);
        thread.start();
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
                    if (smStatus == StockMarketStatusConst.OPEN
                            || smStatus == StockMarketStatusConst.COMPETE
                    ) {
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
                        Thread.sleep(3000);
                    }
                }
            }
        };
        Thread thread = new Thread(stockDealPriceLineRunnable);
        thread.start();
    }
}
