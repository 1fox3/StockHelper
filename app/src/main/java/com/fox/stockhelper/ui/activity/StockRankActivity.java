package com.fox.stockhelper.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fox.stockhelper.R;
import com.fox.stockhelper.api.stock.realtime.RankApi;
import com.fox.stockhelper.config.MsgWhatConfig;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.ui.adapter.recyclerview.StockRankNameAdapter;
import com.fox.stockhelper.ui.adapter.recyclerview.StockRankValueAdapter;
import com.fox.stockhelper.ui.base.BaseActivity;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.SortTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;

/**
 * 股票排行
 *
 * @author lusongsong
 * @date 2020/9/3 14:51
 */
public class StockRankActivity extends BaseActivity implements CommonHandleListener {
    /**
     * 名称列表
     */
    @BindView(R.id.stockRankNameRV)
    RecyclerView stockRankNameRV;
    /**
     * 数值列表
     */
    @BindView(R.id.stockRankValueRV)
    RecyclerView stockRankValueRV;
    /**
     * 当前纵向主动滑动的id
     */
    private int mainVerticalScrollId = 0;
    /**
     * 价格排序组件
     */
    @BindView(R.id.stockRankPriceSTV)
    SortTextView stockRankPriceSTV;
    /**
     * 增幅排序组件
     */
    @BindView(R.id.stockRankUptickRateSTV)
    SortTextView stockRankUptickRateSTV;
    /**
     * 波动排序组件
     */
    @BindView(R.id.stockRankSurgeRateSTV)
    SortTextView stockRankSurgeRateSTV;
    /**
     * 成交量排序组件
     */
    @BindView(R.id.stockRankDealNumSTV)
    SortTextView stockRankDealNumSTV;
    /**
     * 成交金额排序组件
     */
    @BindView(R.id.stockRankDealMoneySTV)
    SortTextView stockRankDealMoneySTV;
    /**
     * 底部提示栏
     */
    @BindView(R.id.bottomTipLL)
    LinearLayout bottomTipLL;
    /**
     * 加载动图
     */
    ImageView loadImgIV;
    /**
     * 底部提示文案
     */
    TextView bottomTipTV;
    /**
     * 排序可选项
     */
    SortTextView[] sortTextViews;
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
    int pageSize = 50;
    /**
     * 消息处理
     */
    Handler handler = new CommonHandler(this);
    /**
     * 名称列表
     */
    StockRankNameAdapter stockRankNameAdapter;
    /**
     * 数值列表
     */
    StockRankValueAdapter stockRankValueAdapter;
    /**
     * 加载动图效果
     */
    Animation loadingImgAnimation;
    /**
     * 是否在加载数据
     */
    boolean isLoadingData = false;
    /**
     * 构建显示内容
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_rank);
        ButterKnife.bind(StockRankActivity.this);
        sortTextViews = new SortTextView[]{
                stockRankPriceSTV,
                stockRankUptickRateSTV,
                stockRankSurgeRateSTV,
                stockRankDealNumSTV,
                stockRankDealMoneySTV
        };
        bottomTipTV = new TextView(this);
        bottomTipTV.setGravity(Gravity.CENTER);
        //现价排序按钮选择监听器
        this.setSortClickListener();
        //初始化排序
        this.initSortType();
        //初始化排行名称列表
        this.initStockRankNameRV();
        //初始化排行数值列表
        this.initStockRankValueRV();
        //初始化排行数据
        this.initRankData();
    }

    /**
     * 初始化动图
     */
    private void initLoadingImg() {
        //设置图片动画属性，各参数说明可参照api
        loadingImgAnimation= new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        //设置旋转重复次数，即转几圈
        loadingImgAnimation.setRepeatCount(30);
        //设置持续时间，注意这里是每一圈的持续时间，如果上面设置的圈数为3，持续时间设置1000，则图片一共旋转3秒钟
        loadingImgAnimation.setDuration(1000);
        //设置动画匀速改变。相应的还有AccelerateInterpolator、DecelerateInterpolator、CycleInterpolator等
        loadingImgAnimation.setInterpolator(new LinearInterpolator());
        //设置ImageView的动画，也可以myImageView.startAnimation(myAlphaAnimation)
        loadImgIV.setAnimation(loadingImgAnimation);
        loadingImgAnimation.setAnimationListener(new Animation.AnimationListener() {	//设置动画监听事件
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("animation", "start");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (!isLoadingData) {
                    animation.cancel();
                }
                Log.e("animation", "repeat");
            }
            //图片旋转结束后触发事件，这里启动新的activity
            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("animation", "end");
            }
        });
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
            case MsgWhatConfig.STOCK_RANK_FRESH:
                this.clearData();
                pageNum = 1;
                Runnable stockMarketStatisticsRunnable = getStockMarketRankRunnable();
                Thread thread = new Thread(stockMarketStatisticsRunnable);
                thread.start();
                break;
            case MsgWhatConfig.STOCK_RANK:
                String[] stockRankArr = bundle.getStringArray("stockRank");
                //关闭动图
                endLoadingData(stockRankArr.length);
                try {
                    List<RankApiDto> rankApiDtoList = new ArrayList<>();
                    for (String stockRankStr : stockRankArr) {
                        RankApiDto rankApiDto =
                                new ObjectMapper().readValue(stockRankStr, RankApiDto.class);
                        rankApiDtoList.add(rankApiDto);
                    }
                    this.addData(rankApiDtoList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
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
                    initRankData();
                }
            });
        }
    }

    /**
     * 初始化排序类型
     */
    private void initSortType() {
        Bundle bundle = getIntent().getExtras();
        String sortColumnParam = bundle.getString("sortColumn");
        if (null != sortColumnParam && !"".equals(sortColumnParam)) {
            sortColumn = sortColumnParam;
        }
        String sortTypeParam = bundle.getString("sortType");
        if (null != sortTypeParam && !"".equals(sortTypeParam)) {
            sortType = sortTypeParam;
        }
        for (SortTextView sortTextView : sortTextViews) {
            sortTextView.reset();
            if (sortColumn.equals(sortTextView.getSortColumn())) {
                sortTextView.setSortType(sortType);
            }
        }
    }

    /**
     * 初始化排行名称头部
     */
    private void initStockRankNameRV() {
        stockRankNameAdapter = new StockRankNameAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        stockRankNameRV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        stockRankNameRV.setAdapter(stockRankNameAdapter);
        stockRankNameRV.addOnScrollListener(this.getRecyclerViewOnScrollListener());
        stockRankNameRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    /**
     * 初始化排行数值头部
     */
    private void initStockRankValueRV() {
        stockRankValueAdapter = new StockRankValueAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        stockRankValueRV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        stockRankValueRV.setAdapter(stockRankValueAdapter);
        stockRankValueRV.addOnScrollListener(this.getRecyclerViewOnScrollListener());
        stockRankValueRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    /**
     * RecyclerView滑动监听
     *
     * @return
     */
    private RecyclerView.OnScrollListener getRecyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (0 != newState && 0 == mainVerticalScrollId) {
                    mainVerticalScrollId = recyclerView.getId();
                }
                if (0 == newState && 0 != mainVerticalScrollId && mainVerticalScrollId == recyclerView.getId()) {
                    mainVerticalScrollId = 0;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                switch (recyclerView.getId()) {
                    case R.id.stockRankNameRV:
                        if (dx == 0 && dy != 0 && mainVerticalScrollId == R.id.stockRankNameRV) {
                            stockRankValueRV.scrollBy(dx, dy);
                        }
                        break;
                    case R.id.stockRankValueRV:
                        if (dx == 0 && dy != 0 && mainVerticalScrollId == R.id.stockRankValueRV) {
                            stockRankNameRV.scrollBy(dx, dy);
                        }
                        break;
                }
                int lastLastVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                if (lastLastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 10) {
                    if (!isLoadingData) {
                        pageNum += 1;
                        //开始加载
                        startLoadingData();
                        Runnable stockMarketStatisticsRunnable = getStockMarketRankRunnable();
                        Thread thread = new Thread(stockMarketStatisticsRunnable);
                        thread.start();
                    }
                }
            }
        };
    }

    /**
     * 初始化排行数据
     */
    private void initRankData() {
        startLoadingData();
        Message msg = new Message();
        msg.what = MsgWhatConfig.STOCK_RANK_FRESH;
        handler.sendMessage(msg);
    }

    /**
     * 清空数据
     */
    private void clearData() {
        stockRankNameAdapter.clearData();
        stockRankValueAdapter.clearData();
    }

    /**
     * 添加数据
     *
     * @param rankApiDtoList
     */
    private void addData(List<RankApiDto> rankApiDtoList) {
        stockRankNameAdapter.addData(rankApiDtoList);
        stockRankValueAdapter.addData(rankApiDtoList);
        if (1 == pageNum) {
            stockRankNameRV.setAdapter(stockRankNameAdapter);
            stockRankValueRV.setAdapter(stockRankValueAdapter);
        } else {
            stockRankNameAdapter.notifyDataSetChanged();
            stockRankValueAdapter.notifyDataSetChanged();
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
                isLoadingData = true;
                Map<String, Object> params = new HashMap<>();
                params.put("type", sortColumn);
                params.put("sortType", sortType);
                params.put("pageNum", pageNum);
                params.put("pageSize", pageSize);
                String[] stockRankArr = new String[pageSize];
                List<RankApiDto> rankApiDtoList = new ArrayList<>();
                try {
                    RankApi rankApi = new RankApi();
                    rankApi.setParams(params);
                    rankApiDtoList = (List<RankApiDto>) rankApi.request();
                    for (int i = 0; i < rankApiDtoList.size(); i++) {
                        stockRankArr[i] = JSONObject.toJSONString(rankApiDtoList.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = MsgWhatConfig.STOCK_RANK;
                Bundle bundle = new Bundle();
                bundle.putStringArray("stockRank", stockRankArr);
                msg.setData(bundle);
                handler.sendMessage(msg);
                isLoadingData = false;
            }
        };
    }

    /**
     * 数据开始加载
     */
    private void startLoadingData() {
        loadImgIV = new ImageView(this);
        loadImgIV.setImageResource(R.drawable.loading);
        this.initLoadingImg();
        bottomTipLL.removeAllViews();
        bottomTipLL.addView(loadImgIV);
    }

    /**
     * 数据加载完成
     * @param resultDataSize
     */
    private void endLoadingData(int resultDataSize) {
        if (resultDataSize < pageSize) {
            bottomTipTV.setText("已加载完毕，无更多数据");
        } else {
            bottomTipTV.setText("滑动加载更多");
        }
        loadImgIV.setVisibility(View.GONE);
        bottomTipLL.removeAllViews();
        bottomTipLL.addView(bottomTipTV);
    }
}
