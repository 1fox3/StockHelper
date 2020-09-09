package com.fox.stockhelper.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.ui.adapter.recyclerview.StockRankHeaderAdapter;
import com.fox.stockhelper.ui.adapter.recyclerview.StockRankNameAdapter;
import com.fox.stockhelper.ui.adapter.recyclerview.StockRankValueAdapter;
import com.fox.stockhelper.ui.base.BaseActivity;
import com.fox.stockhelper.ui.handler.CommonHandler;
import com.fox.stockhelper.ui.listener.CommonHandleListener;
import com.fox.stockhelper.ui.view.ScrollListenerHorizontalScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股票排行
 * @author lusongsong
 * @date 2020/9/3 14:51
 */
public class StockRankActivity extends BaseActivity implements CommonHandleListener {
    /**
     * 排序选项
     */
    @BindView(R.id.stockRankHeaderRV)
    RecyclerView stockRankHeaderRV;
    /**
     * 名称列表
     */
    @BindView(R.id.stockRankNameRV)
    RecyclerView stockRankNameRV;
    /**
     * 数值列表横向滑动组件
     */
    @BindView(R.id.stockRankValueSLHSV)
    ScrollListenerHorizontalScrollView stockRankValueSLHSV;
    /**
     * 数值列表
     */
    @BindView(R.id.stockRankValueRV)
    RecyclerView stockRankValueRV;

    /**
     * 当前横向主动滑动的id
     */
    private int mainHorizontalScrollId = 0;
    /**
     * 当前纵向主动滑动的id
     */
    private int mainVerticalScrollId = 0;

    /**
     * 构建显示内容
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_rank);
        ButterKnife.bind(StockRankActivity.this);
        stockRankValueSLHSV.setHandler(new CommonHandler(this));
        stockRankValueSLHSV.setOnScrollStateChangedListener(this.getOnScrollStateChangedListener());
        //初始化排行头部
        this.initStockRankHeaderRV();
        //初始化排行名称列表
        this.initStockRankNameRV();
        //初始化排行数值列表
        this.initStockRankValueRV();
    }

    /**
     * 初始化排行头部
     */
    private void initStockRankHeaderRV() {
        List<String> rankHeaderList = Arrays.asList("价格", "增幅", "波动", "成交量", "成交金额");
        StockRankHeaderAdapter stockRankHeaderAdapter = new StockRankHeaderAdapter();
        stockRankHeaderAdapter.addData(rankHeaderList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        stockRankHeaderRV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        stockRankHeaderRV.setAdapter(stockRankHeaderAdapter);
        stockRankHeaderRV.addOnScrollListener(this.getRecyclerViewOnScrollListener());
    }

    /**
     * 获取测试数据
     * @return
     */
    private List<RankApiDto> getRankTestValue() {
        int len = 80;
        List<RankApiDto> apiDtoList = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            RankApiDto rankApiDto = new RankApiDto();
            rankApiDto.setStockId(i + 1);
            rankApiDto.setStockName("同花顺" + String.valueOf(i));
            rankApiDto.setStockCode("655080");
            rankApiDto.setPrice((float)i*10);
            rankApiDto.setUptickRate(i * 0.1f);
            rankApiDto.setSurgeRate((float)i * 0.2f);
            rankApiDto.setDealNum((float)i* 10000f);
            rankApiDto.setDealMoney((float)i* 100000f);
            apiDtoList.add(rankApiDto);
        }
        return apiDtoList;
    }

    /**
     * 初始化排行名称头部
     */
    private void initStockRankNameRV() {
        List<RankApiDto> apiDtoList = this.getRankTestValue();
        StockRankNameAdapter stockRankNameAdapter = new StockRankNameAdapter();
        stockRankNameAdapter.addData(apiDtoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        stockRankNameRV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        stockRankNameRV.setAdapter(stockRankNameAdapter);
        stockRankNameRV.addOnScrollListener(this.getRecyclerViewOnScrollListener());
        stockRankNameRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    /**
     * 初始化排行数值头部
     */
    private void initStockRankValueRV() {
        List<RankApiDto> apiDtoList = this.getRankTestValue();
        StockRankValueAdapter stockRankValueAdapter = new StockRankValueAdapter();
        stockRankValueAdapter.addData(apiDtoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        stockRankValueRV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        stockRankValueRV.setAdapter(stockRankValueAdapter);
        stockRankValueRV.addOnScrollListener(this.getRecyclerViewOnScrollListener());
        stockRankValueRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    /**
     * RecyclerView滑动监听
     * @return
     */
    private RecyclerView.OnScrollListener getRecyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (R.id.stockRankHeaderRV != recyclerView.getId()) {
                    if (0 != newState && 0 == mainVerticalScrollId) {
                        mainVerticalScrollId = recyclerView.getId();
                    }
                    if (0 == newState && 0 != mainVerticalScrollId && mainVerticalScrollId == recyclerView.getId()) {
                        mainVerticalScrollId = 0;
                    }
                } else {
                    if (0 != newState && 0 == mainHorizontalScrollId) {
                        mainHorizontalScrollId = recyclerView.getId();
                    }
                    if (0 == newState && 0 != mainHorizontalScrollId && mainHorizontalScrollId == recyclerView.getId()) {
                        mainHorizontalScrollId = 0;
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                switch (recyclerView.getId()) {
                    case R.id.stockRankHeaderRV:
                        if (dx != 0 && dy == 0 && mainHorizontalScrollId == R.id.stockRankHeaderRV) {
                            stockRankValueSLHSV.scrollBy(dx, dy);
                        }
                        break;
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
            }
        };
    }

    /**
     * 消息处理
     * @param message
     */
    @Override
    public void handleMessage(Message message) {
    }

    /**
     * 横向ScrollView滑动监听
     * @return
     */
    private ScrollListenerHorizontalScrollView.ScrollViewListener getOnScrollStateChangedListener() {
        return new ScrollListenerHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollStateChanged(int scrollType) {
                Log.e("scrollType", String.valueOf(scrollType));
                if (0 != scrollType && 0 == mainVerticalScrollId) {
                    mainHorizontalScrollId = R.id.stockRankValueSLHSV;
                }
                if (0 == scrollType && 0 != mainHorizontalScrollId && mainHorizontalScrollId == R.id.stockRankValueSLHSV) {
                    mainHorizontalScrollId = 0;
                }
            }

            @Override
            public void onScrolled(int dx) {
                if (dx != 0 && mainHorizontalScrollId == R.id.stockRankValueSLHSV) {
                    stockRankHeaderRV.scrollBy(dx, 0);
                }
            }
        };
    }
}
