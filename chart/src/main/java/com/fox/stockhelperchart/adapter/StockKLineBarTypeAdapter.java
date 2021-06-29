package com.fox.stockhelperchart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fox.stockhelperchart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lusongsong
 * @date 2021/5/28 16:52
 */
public class StockKLineBarTypeAdapter extends ArrayAdapter<String> {
    List<String> stockLineBarTypeList;

    int resourceId;
    int selectedPosition = 0;
    int selectColor = 0;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public StockKLineBarTypeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        resourceId = resource;
    }

    /**
     * 柱图类型列表
     * @param barTypeList
     */
    public void setStockLineBarTypeList(List<String> barTypeList) {
        stockLineBarTypeList = barTypeList;
    }

    /**
     * 清除
     */
    @Override
    public void clear() {
        stockLineBarTypeList = new ArrayList<>();
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }

    public void setSelectColor(int color) {
        selectColor = color;
    }

    /**
     * 选择元素
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(resourceId, parent, false);
            convertView = view;
        }
        TextView textView = convertView.findViewById(R.id.kLineBarTypeItemTV);
        textView.setText(stockLineBarTypeList.get(position));
        textView.setTextColor(position == selectedPosition ? selectColor : Color.BLACK);
        return convertView;
    }

    /**
     * 获取数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return stockLineBarTypeList.size();
    }
}
