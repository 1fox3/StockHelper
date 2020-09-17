package com.fox.stockhelper.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fox.stockhelper.entity.dto.api.stock.realtime.RankApiDto;
import com.fox.stockhelper.ui.view.StockRankInfoView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * 股票排行适配器
 * @author lusongsong
 * @date 2020/8/28 11:45
 */
public class StockRankAdapter extends ArrayAdapter<RankApiDto> {
    /**
     * 资源id
     */
    private int resourceId;
    /**
     * 股票排行数据
     */
    private List<RankApiDto> rankApiDtoList = new ArrayList<>();

    /**
     * 构造器
     * @param context
     * @param resource
     */
    public StockRankAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.resourceId = resource;
    }

    /**
     * 添加元素
     * @param collection
     */
    @Override
    public void addAll(Collection<? extends RankApiDto> collection) {
        rankApiDtoList.addAll(collection);
    }

    /**
     * 清除
     */
    @Override
    public void clear() {
        rankApiDtoList = new ArrayList<>();
    }

    /**
     * 选择元素
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankApiDto rankApiDto = rankApiDtoList.get(position);
        if (null == convertView) {
            convertView = new StockRankInfoView(this.getContext());
        }
        ((StockRankInfoView)convertView).setRankInfo(rankApiDto);
        return convertView;
    }

    /**
     * 获取数量
     * @return
     */
    @Override
    public int getCount() {
        return this.rankApiDtoList.size();
    }
}
