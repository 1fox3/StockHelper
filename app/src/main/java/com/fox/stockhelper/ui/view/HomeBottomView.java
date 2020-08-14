package com.fox.stockhelper.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页底部
 *
 * @author lusongsong
 */
public class HomeBottomView extends LinearLayout {
    @BindView(R.id.trendL)
    LinearLayout trendL;
    @BindView(R.id.trendIV)
    ImageView trendIV;
    @BindView(R.id.trendTV)
    TextView trendTV;
    @BindView(R.id.followL)
    LinearLayout followL;
    @BindView(R.id.followIV)
    ImageView followIV;
    @BindView(R.id.followTV)
    TextView followTV;
    @BindView(R.id.messageL)
    LinearLayout messageL;
    @BindView(R.id.messageIV)
    ImageView messageIV;
    @BindView(R.id.messageTV)
    TextView messageTV;
    @BindView(R.id.tradeL)
    LinearLayout tradeL;
    @BindView(R.id.tradeIV)
    ImageView tradeIV;
    @BindView(R.id.tradeTV)
    TextView tradeTV;
    @BindView(R.id.userL)
    LinearLayout userL;
    @BindView(R.id.userIV)
    ImageView userIV;
    @BindView(R.id.userTV)
    TextView userTV;
    int currentId;
    int textColor;
    int textSelectedColor;
    public HomeBottomView(Context context) {
        super(context);
        this.initView();
    }

    public HomeBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public HomeBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }

    public HomeBottomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView();
    }

    public View initView() {
        View view = LayoutInflater.from(this.getContext()).inflate(
                R.layout.view_home_bottom, this, true
        );
        ButterKnife.bind(this, view);
        textColor = this.getContext().getResources().getColor(R.color.home_bottom_text_color);
        textSelectedColor = this.getContext().getResources().getColor(R.color.home_bottom_selected_bg);

        return view;
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.trendL, R.id.followL, R.id.messageL, R.id.tradeL, R.id.userL})
    public void onViewClicked(View view) {
        int id = view.getId();
        this.select(id);
        this.unSelect(currentId);
        this.currentId = id;
    }

    private void select(int id) {
        switch (id) {
            case R.id.trendL:
                trendIV.setImageResource(R.drawable.trend_red);
                trendTV.setTextColor(textSelectedColor);
                break;
            case R.id.followL:
                followIV.setImageResource(R.drawable.follow_red);
                followTV.setTextColor(textSelectedColor);
                break;
            case R.id.messageL:
                messageIV.setImageResource(R.drawable.message_red);
                messageTV.setTextColor(textSelectedColor);
                break;
            case R.id.tradeL:
                tradeIV.setImageResource(R.drawable.trade_red);
                tradeTV.setTextColor(textSelectedColor);
                break;
            case R.id.userL:
                userIV.setImageResource(R.drawable.user_red);
                userTV.setTextColor(textSelectedColor);
                break;
        }
    }

    private void unSelect(int id) {
        switch (id) {
            case R.id.trendL:
                trendIV.setImageResource(R.drawable.trend);
                trendTV.setTextColor(textColor);
                break;
            case R.id.followL:
                followIV.setImageResource(R.drawable.follow);
                followTV.setTextColor(textColor);
                break;
            case R.id.messageL:
                messageIV.setImageResource(R.drawable.message);
                messageTV.setTextColor(textColor);
                break;
            case R.id.tradeL:
                tradeIV.setImageResource(R.drawable.trade);
                tradeTV.setTextColor(textColor);
                break;
            case R.id.userL:
                userIV.setImageResource(R.drawable.user);
                userTV.setTextColor(textColor);
                break;
        }
    }
}
