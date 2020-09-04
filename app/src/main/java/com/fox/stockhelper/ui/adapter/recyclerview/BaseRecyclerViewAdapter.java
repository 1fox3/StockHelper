package com.fox.stockhelper.ui.adapter.recyclerview;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author lusongsong
 * @date 2020/9/3 17:01
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    /**
     * 列表数据
     */
    private List<? extends Object> listData;

    /**
     * 添加数据
     * @param dataList
     */
    public void addData(List dataList) {
        if (null == this.listData) {
            this.listData = new ArrayList<>();
        }
        listData.addAll(dataList);
    }

    /**
     * 设置数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(this.listData.get(position));
    }

    /**
     * 获取列表长度
     * @return
     */
    @Override
    public int getItemCount() {
        return null == listData ? 0 : listData.size();
    }
}
