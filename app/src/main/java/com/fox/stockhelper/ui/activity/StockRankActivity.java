package com.fox.stockhelper.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fox.stockhelper.R;
import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.ui.adapter.recyclerview.StockRankHeaderAdapter;
import com.fox.stockhelper.ui.adapter.recyclerview.StockRankListAdapter;
import com.fox.stockhelper.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股票排行
 * @author lusongsong
 * @date 2020/9/3 14:51
 */
public class StockRankActivity extends BaseActivity {
    /**
     * 排行头部
     */
    @BindView(R.id.stockRankHeaderRV)
    RecyclerView stockRankHeaderRV;
    /**
     * 排行列表
     */
    @BindView(R.id.stockRankListRV)
    RecyclerView stockRankListRV;
    /**
     * 当前坐标
     */
    private int posX = 0;
    private int posY = 0;
    StockRankListAdapter listAdapter;
    LinearLayoutManager listLinearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_rank);
        ButterKnife.bind(StockRankActivity.this);
        //初始化排行头部
        this.initStockRankHeaderRV();
        //初始化排行列表
        this.initStockRankListRV();
    }

    /**
     * 初始化排行头部
     */
    private void initStockRankHeaderRV() {
        List<String> headerList = new ArrayList<>(5);
        headerList.add("价格");
        headerList.add("增幅");
        headerList.add("波动");
        headerList.add("成交量");
        headerList.add("成交金额");
        StockRankHeaderAdapter headerAdapter = new StockRankHeaderAdapter();
        headerAdapter.addData(headerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        stockRankHeaderRV.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        stockRankHeaderRV.setAdapter(headerAdapter);
        stockRankHeaderRV.addOnScrollListener(this.getRecyclerViewOnScrollListener());
    }

    /**
     * 初始化排行列表
     */
    private void initStockRankListRV() {
        int len = 50;
        List<RankApiDto> apiDtoList = new ArrayList<>(len);
        RankApiDto rankApiDto = new RankApiDto();
        rankApiDto.setStockId(12);
        rankApiDto.setStockName("同花顺");
        rankApiDto.setStockCode("655080");
        rankApiDto.setPrice(161.25f);
        rankApiDto.setUptickRate(0.0865f);
        rankApiDto.setSurgeRate(0.1865f);
        rankApiDto.setDealNum(4421212165f);
        rankApiDto.setDealMoney(112121221265f);
        for (int i = 0; i < len; i++) {
            apiDtoList.add(rankApiDto);
        }
        listAdapter = new StockRankListAdapter();
        listAdapter.addData(apiDtoList);
        listLinearLayoutManager = new LinearLayoutManager(this);
        stockRankListRV.setLayoutManager(listLinearLayoutManager);
        listLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        stockRankListRV.setAdapter(listAdapter);
//        stockRankListRV.addOnScrollListener(this.getRecyclerViewOnScrollListener());
    }

    private RecyclerView.OnScrollListener getRecyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("ScrollState", String.valueOf(newState));
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                posX += dx;
                posY += dy;
                switch (recyclerView.getId()) {
                    case R.id.stockRankHeaderRV:
                        int firstVisibleItem = listLinearLayoutManager.findFirstVisibleItemPosition();
                        int lastVisibleItem = listLinearLayoutManager.findLastVisibleItemPosition();
                        for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
                            View view = listLinearLayoutManager.getChildAt(i);
                            if (null == view) {
                                continue;
                            }
                            RecyclerView stockRankItemRV = (RecyclerView) view.findViewById(R.id.stockRankItemRV);
                            stockRankItemRV.scrollBy(dx, dy);
                        }
                        Log.e("StockRankActivity", String.valueOf(posX));
                        listAdapter.setPosX(posX);
                        break;
                    case R.id.stockRankListRV:
                        stockRankHeaderRV.scrollBy(dx, dy);
                        break;
                }
            }
        };
    }
}
