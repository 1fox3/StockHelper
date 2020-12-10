package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.spider.stock.api.sina.SinaRealtimeDealInfoApi;
import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.constant.StockMarketStatusConst;
import com.fox.spider.stock.entity.po.sina.SinaRealtimeDealInfoPo;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.api.stock.realtime.RankApi;
import com.fox.stockhelper.api.stock.realtime.UptickRateStatisticsApi;
import com.fox.stockhelper.config.ActivityRequestCodeConfig;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.constant.stock.SHStockConst;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.entity.dto.api.stock.realtime.UptickRateStatisticsApiDto;
import com.fox.stockhelper.ui.activity.StockDealLineActivity;
import com.fox.stockhelper.ui.activity.StockRankActivity;
import com.fox.stockhelper.ui.adapter.StockRankAdapter;
import com.fox.stockhelper.ui.base.StockBaseFragment;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.SortTextView;
import com.fox.stockhelper.ui.view.StockIndexBlockView;
import com.fox.stockhelper.util.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.SneakyThrows;

/**
 * 股市首页
 *
 * @author lusongsong
 */
public class StockMarketFragment extends StockBaseFragment implements CommonHandleListener {
    /**
     * 是否需要刷新数据
     */
    protected boolean dataRefresh = true;
    /**
     * top指标实时交易数据
     */
    private Map<String, SinaRealtimeDealInfoPo> topIndexDealInfoMap = new HashMap<>();
    /**
     * 涨跌统计
     */
    private UptickRateStatisticsApiDto uptickRateStatisticsApiDto;
    /**
     * 股票排行
     */
    private List<RankApiDto> rankApiDtoList;
    /**
     * 股市状态logo
     */
    @BindView(R.id.smStatusIV)
    ImageView smStatusIV;
    /**
     * 股市状态文案
     */
    @BindView(R.id.smStatusTV)
    TextView smStatusTV;
    /**
     * 当前日期
     */
    @BindView(R.id.smDate)
    TextView smDate;
    /**
     * 星期
     */
    @BindView(R.id.smWeek)
    TextView smWeek;
    /**
     * 股票排行
     */
    @BindView(R.id.stockRankHeader)
    LinearLayout stockRankHeader;
    /**
     * top指数列表
     */
    @BindView(R.id.topIndexLL)
    LinearLayout topIndexLL;
    /**
     * 统计跌文本
     */
    @BindView(R.id.smUSDownTV)
    TextView smUSDownTV;
    /**
     * 统计跌图片
     */
    @BindView(R.id.smUSDownIV)
    ImageView smUSDownIV;
    /**
     * 统计平文本
     */
    @BindView(R.id.smUSFlatTV)
    TextView smUSFlatTV;
    /**
     * 统计平图片
     */
    @BindView(R.id.smUSFlatIV)
    ImageView smUSFlatIV;
    /**
     * 统计涨文本
     */
    @BindView(R.id.smUSUpTV)
    TextView smUSUpTV;
    /**
     * 统计涨图片
     */
    @BindView(R.id.smUSUpIV)
    ImageView smUSUpIV;
    /**
     * 统计涨停数文本
     */
    @BindView(R.id.smUSUpLimitTV)
    TextView smUSUpLimitTV;
    /**
     * 涨停文案
     */
    @BindView(R.id.smUSUpLimitTextTV)
    TextView smUSUpLimitTextTV;
    /**
     * 统计跌停文本
     */
    @BindView(R.id.smUSDownLimitTV)
    TextView smUSDownLimitTV;
    /**
     * 跌停文案
     */
    @BindView(R.id.smUSDownLimitTextTV)
    TextView smUSDownLimitTextTV;

    /**
     * 价格排序
     */
    @BindView(R.id.priceSTV)
    SortTextView priceSTV;
    /**
     * 增幅排序
     */
    @BindView(R.id.uptickRateSTV)
    SortTextView uptickRateSTV;
    /**
     * 波动排序
     */
    @BindView(R.id.surgeRateSTV)
    SortTextView surgeRateSTV;
    /**
     * 成交量排序
     */
    @BindView(R.id.dealNumSTV)
    SortTextView dealNumSTV;
    /**
     * 成交金额排序
     */
    @BindView(R.id.dealMoneySTV)
    SortTextView dealMoneySTV;
    /**
     * 股票排行
     */
    @BindView(R.id.stockRankList)
    ListView stockRankListLV;
    /**
     * 股票排行适配器
     */
    StockRankAdapter stockRankAdapter;

    /**
     * 排序可选项
     */
    SortTextView[] sortTextViews;
    /**
     * top指标ui
     */
    Map<String, StockIndexBlockView> topIndexMap = new HashMap<>();
    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);
    /**
     * 排序条件
     */
    String sortColumn = "price";
    /**
     * 排序类型
     */
    String sortType = "DESC";
    /**
     * 页码
     */
    int pageNum = 1;
    /**
     * 每页记录条数
     */
    int pageSize = 15;

    /**
     * 指定上下文和股市
     *
     * @param context
     * @param stockMarket
     */
    public StockMarketFragment(Context context, Integer stockMarket) {
        super(context);
        this.stockMarket = stockMarket;
        needStockMarketDealStatusService = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_market, null);
        ButterKnife.bind(this, view);
        sortTextViews = new SortTextView[]{
                priceSTV,
                uptickRateSTV,
                surgeRateSTV,
                dealNumSTV,
                dealMoneySTV
        };
        //设置排序监听方法
        setSortClickListener();
        //排行适配器
        stockRankAdapter = new StockRankAdapter(this.getContext(), R.layout.view_stock_rank_info);
        //开启数据刷新
        startRefreshData();
        return view;
    }

    /**
     * 设置显示时间
     */
    private void initDate() {
        smDate.setText(DateUtil.getCurrentDate());
        smWeek.setText(DateUtil.getCurrentWeekNum());
    }

    /**
     * 开启数据刷新
     */
    private void startRefreshData() {
        //显示时间
        initDate();
        //top指标
        handleStockMarketTopIndex();
        //涨跌统计
        handleStockMarketStatistics();
        //初始化排行
        handleStockMarketRank();
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
            if (SHStockConst.STATUS_IMAGE_MAP.containsKey(newStatus)) {
                smStatusIV.setImageResource(SHStockConst.STATUS_IMAGE_MAP.get(newStatus));
            }
            if (StockMarketStatusConst.STATUS_DESC_MAP.containsKey(newStatus)) {
                smStatusTV.setText(StockMarketStatusConst.STATUS_DESC_MAP.get(newStatus));
            }
            if (StockMarketStatusConst.OPEN == newStatus
                    || StockMarketStatusConst.COMPETE == newStatus) {
                smStatusTV.setTextColor(Color.RED);
            } else {
                smStatusTV.setTextColor(Color.BLACK);
            }
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
     * 消息处理
     *
     * @param message
     */
    @Override
    public void handleMessage(Message message) {
        Bundle bundle = message.getData();
        switch (message.what) {
            case MsgWhatConfig.TOP_INDEX:
                try {
                    List<StockVo> stockVoList = StockConst.stockMarketTopIndex(stockMarket);
                    SinaRealtimeDealInfoPo sinaRealtimeDealInfoPo;
                    for (StockVo stockVo : stockVoList) {
                        if (null == stockVo.getStockCode() || null == topIndexDealInfoMap
                                || !topIndexDealInfoMap.containsKey(stockVo.getStockCode())) {
                            continue;
                        }
                        sinaRealtimeDealInfoPo = topIndexDealInfoMap.get(stockVo.getStockCode());
                        String stockCode = sinaRealtimeDealInfoPo.getStockCode();
                        StockIndexBlockView stockIndexBlockView;
                        if (!topIndexMap.containsKey(stockCode)) {
                            stockIndexBlockView = new StockIndexBlockView(this.context);
                            stockIndexBlockView.setLayoutParams(
                                    new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            1)
                            );
                            //xml中的center数值是0x11，与RelativeLayout.END_OF(17)等价
                            stockIndexBlockView.setGravity(RelativeLayout.END_OF);
                            stockIndexBlockView.setName(sinaRealtimeDealInfoPo.getStockName());
                            stockIndexBlockView.setPrice(
                                    sinaRealtimeDealInfoPo.getCurrentPrice(),
                                    sinaRealtimeDealInfoPo.getPreClosePrice()
                            );
                            topIndexMap.put(stockCode, stockIndexBlockView);
                            topIndexLL.addView(stockIndexBlockView);
                        } else {
                            stockIndexBlockView = topIndexMap.get(stockCode);
                            stockIndexBlockView.setPrice(
                                    sinaRealtimeDealInfoPo.getCurrentPrice(),
                                    sinaRealtimeDealInfoPo.getPreClosePrice()
                            );
                        }
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Log.e("topIndexRuntimeException", e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("topIndexException", e.getMessage());
                }
                break;
            case MsgWhatConfig.UPTICK_RATE_STATISTICS:
                try {
                    if (StockConst.SM_NO_LIMIT_LIST.contains(stockMarket)) {
                        smUSDownLimitTV.setText(String.valueOf(uptickRateStatisticsApiDto.getDown()));
                        smUSUpLimitTV.setText(String.valueOf(uptickRateStatisticsApiDto.getUp()));
                        smUSDownLimitTextTV.setText("跌");
                        smUSUpLimitTextTV.setText("涨");
                    } else {
                        smUSDownLimitTV.setText(String.valueOf(uptickRateStatisticsApiDto.getDownLimit()));
                        smUSUpLimitTV.setText(String.valueOf(uptickRateStatisticsApiDto.getUpLimit()));
                    }
                    smUSDownTV.setText(String.valueOf(uptickRateStatisticsApiDto.getDown()));
                    smUSFlatTV.setText(String.valueOf(uptickRateStatisticsApiDto.getFlat()));
                    smUSUpTV.setText(String.valueOf(uptickRateStatisticsApiDto.getUp()));
                    smUSDownIV.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            uptickRateStatisticsApiDto.getDown()
                    ));
                    smUSFlatIV.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            uptickRateStatisticsApiDto.getFlat()
                    ));
                    smUSUpIV.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            uptickRateStatisticsApiDto.getUp()
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MsgWhatConfig.STOCK_RANK:
                try {
                    stockRankAdapter.clear();
                    stockRankAdapter.addAll(rankApiDtoList);
                    stockRankListLV.setAdapter(stockRankAdapter);
                    stockRankListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getContext(), StockDealLineActivity.class);
                            Bundle bundle = new Bundle();
                            RankApiDto rankApiDto = rankApiDtoList.get(i);
                            bundle.putInt("stockMarket", stockMarket);
                            bundle.putString("stockName", rankApiDto.getStockName());
                            bundle.putString("stockCode", rankApiDto.getStockCode());
                            intent.putExtra("stock", bundle);
                            startActivity(intent);
                        }
                    });
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MsgWhatConfig.STOCK_RANK_FRESH:
                this.handleStockMarketRank();
                break;
        }
    }

    /**
     * 处理顶部指指数
     */
    private void handleStockMarketTopIndex() {
        Runnable stockMarketStatusRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    List<StockVo> stockVoList = StockConst.stockMarketTopIndex(stockMarket);
                    SinaRealtimeDealInfoApi sinaRealtimeDealInfo = new SinaRealtimeDealInfoApi();
                    topIndexDealInfoMap =
                            sinaRealtimeDealInfo.batchRealtimeDealInfo(stockVoList);
                    Message msg = new Message();
                    msg.what = MsgWhatConfig.TOP_INDEX;
                    handler.sendMessage(msg);
                    if (dataRefresh) {
                        Thread.sleep(2000);
                    } else {
                        break;
                    }
                }
            }
        };
        Thread thread = new Thread(stockMarketStatusRunnable);
        thread.start();
    }

    /**
     * 处理涨跌统计
     */
    private void handleStockMarketStatistics() {
        Runnable stockMarketStatisticsRunnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    Map<String, Object> params = new HashMap<>(1);
                    params.put("stockMarket", stockMarket);
                    UptickRateStatisticsApi uptickRateStatisticsApi =
                            new UptickRateStatisticsApi();
                    uptickRateStatisticsApi.setParams(params);
                    uptickRateStatisticsApiDto =
                            (UptickRateStatisticsApiDto) uptickRateStatisticsApi.request();
                    Message msg = new Message();
                    msg.what = MsgWhatConfig.UPTICK_RATE_STATISTICS;
                    handler.sendMessage(msg);
                    if (dataRefresh) {
                        Thread.sleep(5000);
                    } else {
                        break;
                    }
                }
            }
        };
        Thread thread = new Thread(stockMarketStatisticsRunnable);
        thread.start();
    }

    /**
     * 排序设置OnClickListener
     */
    public void setSortClickListener() {
        for (SortTextView sortTextView : sortTextViews) {
            sortTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    for (SortTextView sortTextView : sortTextViews) {
                        if (id != sortTextView.getId()) {
                            sortTextView.reset();
                        }
                    }
                    sortColumn = ((SortTextView) view).getSortColumn();
                    sortType = ((SortTextView) view).getSortType();
                    Message msg = new Message();
                    msg.what = MsgWhatConfig.STOCK_RANK_FRESH;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    /**
     * 获取股票排行线程
     *
     * @return
     */
    private Runnable getStockMarketRankRunnable() {
        return new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                RankApi rankApi = new RankApi();
                Map<String, Object> params = new HashMap<>();
                params.put("stockMarket", stockMarket);
                params.put("type", sortColumn);
                params.put("sortType", sortType);
                params.put("pageNum", pageNum);
                params.put("pageSize", pageSize);
                rankApi.setParams(params);
                rankApiDtoList = (List<RankApiDto>) rankApi.request();
                Message msg = new Message();
                msg.what = MsgWhatConfig.STOCK_RANK;
                handler.sendMessage(msg);
            }
        };
    }

    /**
     * 初始化排行
     */
    private void handleStockMarketRank() {
        Runnable stockMarketStatisticsRunnable = getStockMarketRankRunnable();
        Thread thread = new Thread(stockMarketStatisticsRunnable);
        thread.start();
    }

    @OnClick({R.id.stockRankMore})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.stockRankMore:
                Intent intent = new Intent(this.getContext(), StockRankActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("stockMarket", stockMarket);
                bundle.putString("sortColumn", sortColumn);
                bundle.putString("sortType", sortType);
                intent.putExtras(bundle);
                startActivityForResult(intent, ActivityRequestCodeConfig.STOCK_RANK_MORE);
                break;
        }
    }
}
