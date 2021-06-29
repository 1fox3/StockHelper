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

import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.realtime.DealInfoApiDto;
import com.fox.stockhelper.entity.po.stock.StockRealtimeDealInfoPo;
import com.fox.stockhelper.entity.po.stock.StockRealtimeMinuteKLinePo;
import com.fox.stockhelper.spider.out.StockSpiderRealtimeDealInfoApi;
import com.fox.stockhelper.spider.out.StockSpiderRealtimeMinuteKLineApi;
import com.fox.stockhelper.ui.activity.StockAllKlineLandActivity;
import com.fox.stockhelper.ui.base.StockBaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.fox.stockhelper.ui.view.StockTopPriceView;
import com.fox.stockhelper.util.SelfBeanUtil;
import com.fox.stockhelperchart.StockSingleDayMinuteChart;

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
    protected boolean dataRefresh = false;
    /**
     * 股票
     */
    private StockVo stockVo;
    /**
     * 股票最新交易日交易数据接口
     */
    private StockSpiderRealtimeDealInfoApi stockSpiderRealtimeDealInfoApi =
            new StockSpiderRealtimeDealInfoApi();
    /**
     * 股票最新交易日分钟线图数据接口
     */
    private StockSpiderRealtimeMinuteKLineApi stockSpiderRealtimeMinuteKLineApi =
            new StockSpiderRealtimeMinuteKLineApi();
    /**
     * 最新交易日交易数据
     */
    StockRealtimeDealInfoPo stockRealtimeDealInfoPo;
    /**
     * 最新交易日分钟线图信息
     */
    StockRealtimeMinuteKLinePo stockRealtimeMinuteKLinePo;
    /**
     * 交易信息
     */
    @BindView(R.id.stockDealInfoSDIV)
    StockDealInfoView stockDealInfoSDIV;
    /**
     * 单天分钟线图
     */
    @BindView(R.id.stockOneDayChart)
    StockSingleDayMinuteChart stockOneDayChart;
    /**
     * Top售价
     */
    @BindView(R.id.stockTopPriceSTPV)
    StockTopPriceView stockTopPriceSTPV;
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
     * 是否为横屏
     */
    private boolean land = false;//是否横屏
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
        stockOneDayChart.initChart(stockVo);
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
                    SelfBeanUtil.copyProperties(stockRealtimeDealInfoPo, dealInfoApiDto);
                    //交易信息
                    stockDealInfoSDIV.setData(dealInfoApiDto).reDraw();
                    stockTopPriceSTPV.setPriceInfo(stockRealtimeDealInfoPo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
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
                    stockRealtimeDealInfoPo =
                            stockSpiderRealtimeDealInfoApi.realtimeDealInfo(stockVo);
                    if (null != stockRealtimeDealInfoPo) {
                        Message msg = new Message();
                        msg.what = MsgWhatConfig.STOCK_DEAL_INFO;
                        handler.sendMessage(msg);
                        if (dataRefresh) {
                            Thread.sleep(2000);
                        } else {
                            break;
                        }
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
                    if (dataRefresh) {
                        stockOneDayChart.freshData();
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
