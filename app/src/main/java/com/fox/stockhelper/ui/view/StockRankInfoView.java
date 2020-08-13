package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fox.stockhelper.R;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;

public class StockRankInfoView extends LinearLayout {
    public StockRankInfoView(Context context) {
        super(context);
        this.initView();
    }

    public StockRankInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public StockRankInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }

    public StockRankInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView();
    }

    public void initView() {
        View view = LayoutInflater.from(this.getContext()).inflate(
                R.layout.view_stock_rank_info, this, true
        );
        ButterKnife.bind(this, view);
    }
}
