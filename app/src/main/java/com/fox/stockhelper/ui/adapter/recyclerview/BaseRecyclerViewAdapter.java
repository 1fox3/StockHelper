package com.fox.stockhelper.ui.adapter.recyclerview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author lusongsong
 * @date 2020/9/3 17:01
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    /**
     * 列表数据
     */
    private List<? extends Object> listData = new ArrayList<>();

    /**
     * 设置数据
     *
     * @param dataList
     */
    public void setData(List dataList) {
        listData = dataList;
    }

    /**
     * 添加数据
     *
     * @param dataList
     */
    public void addData(List dataList) {
        listData.addAll(dataList);
    }

    /**
     * 清空数据
     */
    public void clearData() {
        listData = new ArrayList<>();
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(position, this.listData.get(position));
    }

    /**
     * 获取列表长度
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return null == listData ? 0 : listData.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }
}
