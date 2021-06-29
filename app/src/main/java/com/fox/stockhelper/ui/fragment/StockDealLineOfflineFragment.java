package com.fox.stockhelper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fox.spider.stock.constant.StockConst;
import com.fox.spider.stock.entity.vo.StockVo;
import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.activity.StockAllKlineLandActivity;
import com.fox.stockhelper.ui.base.StockBaseFragment;
import com.fox.stockhelper.ui.view.StockDealInfoView;
import com.fox.stockhelperchart.StockKLineChart;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 股市离线成交线图
 *
 * @author lusongsong
 * @date 2020/9/14 15:58
 */
public class StockDealLineOfflineFragment extends StockBaseFragment {
    StockVo stockVo;

    @BindView(R.id.stockDealInfoSDIV)
    StockDealInfoView stockDealInfoSDIV;
    /**
     * k线图
     */
    @BindView(R.id.stockKLineChart)
    StockKLineChart stockKLineChart;
    /**
     * 查看所以线图图片
     */
    @BindView(R.id.allKlineIV)
    ImageView allKlineIV;

    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public StockDealLineOfflineFragment(Context context, StockVo stockVo) {
        super(context);
        if (null != stockVo) {
            stockMarket = stockVo.getStockMarket();
            this.stockVo = stockVo;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_deal_line_offline, null);
        ButterKnife.bind(this, view);
        stockKLineChart.setDateType(StockConst.DT_DAY);
        stockKLineChart.initChart(stockVo);
        allKlineIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StockAllKlineLandActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("stockMarket", stockMarket);
                bundle.putString("stockCode", stockVo.getStockCode());
                intent.putExtra("stock", bundle);
                getContext().startActivity(intent);
            }
        });
        return view;
    }

}
