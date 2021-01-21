package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fox.spider.stock.api.nets.NetsRealtimeMinuteKLineApi;
import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.spider.stock.entity.po.nets.NetsRealtimeMinuteKLinePo;
import com.fox.spider.stock.entity.po.nets.NetsRealtimeMinuteNodeDataPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.ui.base.StockBaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.StockDealMinuteInfoView;
import com.fox.stockhelper.util.DateUtil;
import com.github.mikephil.charting.stockChart.BaseChart;
import com.github.mikephil.charting.stockChart.OneDayChart;
import com.github.mikephil.charting.stockChart.dataManage.KLineDataManage;
import com.github.mikephil.charting.stockChart.dataManage.TimeDataManage;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 最新交易线图
 *
 * @author lusongsong
 * @date 2020/10/22 20:39
 */
public class StockLandLineRealtimeFragment extends StockBaseFragment
        implements CommonHandleListener, BaseChart.OnHighlightValueSelectedListener {
    /**
     * 是否需要刷新数据
     */
    protected boolean dataRefresh = true;
    /**
     * 股票
     */
    private StockVo stockVo;
    /**
     * 分钟线图信息
     */
    NetsRealtimeMinuteKLinePo netsRealtimeMinuteKLinePo;
    /**
     * 是否横屏显示
     */
    boolean land = true;
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
    public StockLandLineRealtimeFragment(Context context, StockVo stockVo) {
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
        View view = inflater.inflate(R.layout.fragment_stock_land_line_realtime, null);
        ButterKnife.bind(this, view);
        needStockMarketDealStatusService = true;
        //初始化
        stockRealtimeODC.initChart(land);
        stockRealtimeODC.setHighlightValueSelectedListener(this);
        //首次填充数据
        startRefreshData();
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
                try {
                    object = new JSONObject(com.alibaba.fastjson.JSONObject.toJSONString(convertToRealTimeChartData()));
                    //上证指数代码000001.IDX.SH
                    kTimeData.parseTimeData(object, "000001.IDX.SH", 0);
                    stockRealtimeODC.setDataToChart(kTimeData);
                    choose(netsRealtimeMinuteKLinePo.getKlineData().size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
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
                    NetsRealtimeMinuteKLineApi netsRealtimeMinuteKLineApi =
                            new NetsRealtimeMinuteKLineApi();
                    netsRealtimeMinuteKLinePo =
                            netsRealtimeMinuteKLineApi.realtimeMinuteKLine(stockVo);
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

    /**
     * 将数据转换成表格需要的格式
     *
     * @return
     */
    public Map<String, Object> convertToRealTimeChartData() {
        List<NetsRealtimeMinuteNodeDataPo> netsRealtimeMinuteNodeDataPoList = netsRealtimeMinuteKLinePo.getKlineData();
        Map<String, Object> realTimeChartData = new HashMap<>(2);
        List<List<Object>> minuteDataList = new ArrayList<>(netsRealtimeMinuteNodeDataPoList.size());
        for (NetsRealtimeMinuteNodeDataPo netsRealtimeMinuteNodeDataPo : netsRealtimeMinuteNodeDataPoList) {
            List<Object> minuteData = new ArrayList<>(5);
            minuteData.add(
                    DateUtil.getDateFromStr(
                            netsRealtimeMinuteKLinePo.getDealNum() + " " + netsRealtimeMinuteNodeDataPo.getTime(),
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
        realTimeChartData.put("preClose", netsRealtimeMinuteKLinePo.getPreClosePrice());
        return realTimeChartData;
    }

    /**
     * 选中
     *
     * @param index
     */
    public void choose(Integer index) {
        List<NetsRealtimeMinuteNodeDataPo> netsRealtimeMinuteNodeDataPoList = netsRealtimeMinuteKLinePo.getKlineData();
        NetsRealtimeMinuteNodeDataPo netsRealtimeMinuteNodeDataPo = netsRealtimeMinuteNodeDataPoList.get(index);
        stockMinInfoSDMIV.setDt(netsRealtimeMinuteKLinePo.getDt());
        stockMinInfoSDMIV.setTime(netsRealtimeMinuteNodeDataPo.getTime());
        stockMinInfoSDMIV.setPrice(netsRealtimeMinuteNodeDataPo.getPrice());
        stockMinInfoSDMIV.setAvgPrice(netsRealtimeMinuteNodeDataPo.getAvgPrice());
        stockMinInfoSDMIV.setDealNum(netsRealtimeMinuteNodeDataPo.getDealNum());
        stockMinInfoSDMIV.setDealMoney(netsRealtimeMinuteNodeDataPo.getAvgPrice().multiply(new BigDecimal(netsRealtimeMinuteNodeDataPo.getDealNum())));
        stockMinInfoSDMIV.setTime(netsRealtimeMinuteNodeDataPo.getTime());
        stockMinInfoSDMIV.reDraw();
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
