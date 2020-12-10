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

import com.fox.spider.stock.api.nets.NetsRealtimeMinuteDealInfoApi;
import com.fox.spider.stock.api.sina.SinaRealtimeDealInfoApi;
import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.spider.stock.entity.po.nets.NetsRealtimeMinuteDealInfoPo;
import com.fox.spider.stock.entity.po.nets.NetsRealtimeMinuteNodeDataPo;
import com.fox.spider.stock.entity.po.sina.SinaRealtimeDealInfoPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;
import com.fox.stockhelper.entity.dto.api.stock.realtime.TopDealPriceSingleDto;
import com.fox.stockhelper.ui.activity.StockAllKlineLandActivity;
import com.fox.stockhelper.ui.adapter.recyclerview.StockTopDealPriceAdapter;
import com.fox.stockhelper.ui.base.StockBaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.fox.stockhelper.util.DateUtil;
import com.github.mikephil.charting.stockChart.OneDayChart;
import com.github.mikephil.charting.stockChart.dataManage.TimeDataManage;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 股市实时成交线图
 *
 * @author lusongsong
 * @date 2020/9/14 15:57
 */
public class StockDealLineRealtimeFragment extends StockBaseFragment implements CommonHandleListener {
    /**
     * 是否需要刷新数据
     */
    protected boolean dataRefresh = true;
    /**
     * 股票
     */
    private StockVo stockVo;
    /**
     * 实时交易数据
     */
    SinaRealtimeDealInfoPo sinaRealtimeDealInfoPo;
    /**
     * 分钟线图信息
     */
    NetsRealtimeMinuteDealInfoPo netsRealtimeMinuteDealInfoPo;
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
     * 股票编码
     */
    String stockCode;
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
     * 消息处理
     */
    Handler handler = new CommonHandler(this);

    /**
     * 构造函数
     *
     * @param context
     */
    public StockDealLineRealtimeFragment(Context context, StockVo stockVo) {
        super(context);
        this.stockVo = stockVo;
        if (null != stockVo) {
            stockMarket = stockVo.getStockMarket();
            stockCode = stockVo.getStockCode();
        }
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
        View view = inflater.inflate(R.layout.fragment_stock_deal_line_realtime, null);
        ButterKnife.bind(this, view);
        needStockMarketDealStatusService = true;
        //初始化
        stockOneDayChart.initChart(land);
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
                bundle.putInt("stockMarket", stockMarket);
                bundle.putString("stockCode", stockCode);
                intent.putExtra("stock", bundle);
                getContext().startActivity(intent);
            }
        });
        //首次填充数据
        startRefreshData();
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
        if (null != newStatus && !newStatus.equals(oldStatus)) {
            if (StockMarketStatusConst.CAN_DEAL_STATUS_LIST.contains(newStatus)) {
                if (!dataRefresh) {
                    dataRefresh = true;
                }
            } else {
                dataRefresh = false;
            }
            //状态发生变化时保证刷新一次
            startRefreshData();
        }
    }

    /**
     * 开启数据刷新
     */
    private void startRefreshData() {
        //初始化交易信息
        handleDealInfo();
        //初始化交易价格线图信息
        handleDealPriceLine();
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
            case MsgWhatConfig.STOCK_DEAL_INFO:
                try {
                    DealInfoApiDto dealInfoApiDto = new DealInfoApiDto();
                    dealInfoApiDto.setCurrentPrice(sinaRealtimeDealInfoPo.getCurrentPrice());
                    dealInfoApiDto.setOpenPrice(sinaRealtimeDealInfoPo.getOpenPrice());
                    dealInfoApiDto.setPreClosePrice(sinaRealtimeDealInfoPo.getPreClosePrice());
                    dealInfoApiDto.setHighestPrice(sinaRealtimeDealInfoPo.getHighestPrice());
                    dealInfoApiDto.setLowestPrice(sinaRealtimeDealInfoPo.getLowestPrice());
                    dealInfoApiDto.setDealNum(sinaRealtimeDealInfoPo.getDealNum());
                    dealInfoApiDto.setDealMoney(sinaRealtimeDealInfoPo.getDealMoney());
                    //交易信息
                    stockDealInfoSDIV.setData(dealInfoApiDto).reDraw();
                    //TOP售价
                    sellStockTopDealPriceAdapter.clearData();
                    sellStockTopDealPriceAdapter.setPreClosePrice(sinaRealtimeDealInfoPo.getPreClosePrice());
                    List<Map<String, BigDecimal>> sellList = sinaRealtimeDealInfoPo.getSellPriceList();
                    List<TopDealPriceSingleDto> sellPriceList = new ArrayList<>(sellList.size());
                    for (Map<String, BigDecimal> sellPriceMap : sellList) {
                        TopDealPriceSingleDto topDealPriceSingleDto = new TopDealPriceSingleDto();
                        topDealPriceSingleDto.setNum(((BigDecimal) sellPriceMap.get("num")).longValue());
                        topDealPriceSingleDto.setPrice(((BigDecimal) sellPriceMap.get("price")));
                        sellPriceList.add(topDealPriceSingleDto);
                    }
                    sellStockTopDealPriceAdapter.addData(sellPriceList);
                    sellStockTopDealPriceRV.setAdapter(sellStockTopDealPriceAdapter);
                    //TOP买价
                    buyStockTopDealPriceAdapter.clearData();
                    buyStockTopDealPriceAdapter.setPreClosePrice(sinaRealtimeDealInfoPo.getPreClosePrice());
                    List<Map<String, BigDecimal>> buyList = sinaRealtimeDealInfoPo.getSellPriceList();
                    List<TopDealPriceSingleDto> buyPriceList = new ArrayList<>(sellList.size());
                    for (Map<String, BigDecimal> buyPriceMap : buyList) {
                        TopDealPriceSingleDto topDealPriceSingleDto = new TopDealPriceSingleDto();
                        topDealPriceSingleDto.setNum(buyPriceMap.get("num").longValue());
                        topDealPriceSingleDto.setPrice(buyPriceMap.get("price"));
                        buyPriceList.add(topDealPriceSingleDto);
                    }
                    buyStockTopDealPriceAdapter.addData(buyPriceList);
                    buyStockTopDealPriceRV.setAdapter(buyStockTopDealPriceAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MsgWhatConfig.STOCK_DEAL_PRICE_LINE:
                try {
                    object = new JSONObject(com.alibaba.fastjson.JSONObject.toJSONString(convertToRealTimeChartData()));
                    //上证指数代码000001.IDX.SH
                    kTimeData.parseTimeData(object, "000001.IDX.SH", 0);
                    stockOneDayChart.setDataToChart(kTimeData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 将数据转换成表格需要的格式
     *
     * @return
     */
    public Map<String, Object> convertToRealTimeChartData() {
        List<NetsRealtimeMinuteNodeDataPo> netsRealtimeMinuteNodeDataPoList = netsRealtimeMinuteDealInfoPo.getKlineData();
        Map<String, Object> realTimeChartData = new HashMap<>(2);
        List<List<Object>> minuteDataList = new ArrayList<>(netsRealtimeMinuteNodeDataPoList.size());
        for (NetsRealtimeMinuteNodeDataPo netsRealtimeMinuteNodeDataPo : netsRealtimeMinuteNodeDataPoList) {
            List<Object> minuteData = new ArrayList<>(5);
            minuteData.add(
                    DateUtil.getDateFromStr(
                            netsRealtimeMinuteDealInfoPo.getDealNum() + " " + netsRealtimeMinuteNodeDataPo.getTime(),
                            DateUtil.TIME_FORMAT_2
                    ).getTime()
            );
            minuteData.add(netsRealtimeMinuteNodeDataPo.getPrice());
            minuteData.add(netsRealtimeMinuteNodeDataPo.getAvgPrice());
            minuteData.add(netsRealtimeMinuteNodeDataPo.getDealNum());
            minuteData.add(netsRealtimeMinuteNodeDataPo.getAvgPrice());
            minuteDataList.add(minuteData);
        }
        realTimeChartData.put("data", minuteDataList);
        realTimeChartData.put("preClose", netsRealtimeMinuteDealInfoPo.getPreClosePrice());
        return realTimeChartData;
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
                    SinaRealtimeDealInfoApi sinaRealtimeDealInfoApi = new SinaRealtimeDealInfoApi();
                    sinaRealtimeDealInfoPo = sinaRealtimeDealInfoApi.realtimeDealInfo(stockVo);
                    Message msg = new Message();
                    msg.what = MsgWhatConfig.STOCK_DEAL_INFO;
                    handler.sendMessage(msg);
                    if (dataRefresh) {
                        Thread.sleep(2000);
                    } else {
                        break;
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
                    NetsRealtimeMinuteDealInfoApi netsRealtimeMinuteDealInfoApi =
                            new NetsRealtimeMinuteDealInfoApi();
                    netsRealtimeMinuteDealInfoPo =
                            netsRealtimeMinuteDealInfoApi.realtimeMinuteKLine(stockVo);
                    Message msg = new Message();
                    msg.what = MsgWhatConfig.STOCK_DEAL_PRICE_LINE;
                    handler.sendMessage(msg);
                    if (dataRefresh) {
                        Thread.sleep(2000);
                    } else {
                        break;
                    }
                }
            }
        };
        Thread thread = new Thread(stockDealPriceLineRunnable);
        thread.start();
    }
}
