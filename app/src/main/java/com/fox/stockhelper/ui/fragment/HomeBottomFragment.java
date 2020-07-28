package com.fox.stockhelper.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页底部
 *
 * @author lusongsong
 */
public class HomeBottomFragment extends Fragment {
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
    int textColor = R.color.home_bottom_text_color;
    int textSelectedColor = R.color.home_bottom_selected_bg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_bottom, null);
        ButterKnife.bind(this, view);

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
                trendIV.setImageResource(R.mipmap.trend_red);
                trendTV.setTextColor(getResources().getColor(textSelectedColor));
                break;
            case R.id.followL:
                followIV.setImageResource(R.mipmap.follow_red);
                followTV.setTextColor(getResources().getColor(textSelectedColor));
                break;
            case R.id.messageL:
                messageIV.setImageResource(R.mipmap.message_red);
                messageTV.setTextColor(getResources().getColor(textSelectedColor));
                break;
            case R.id.tradeL:
                tradeIV.setImageResource(R.mipmap.trade_red);
                tradeTV.setTextColor(getResources().getColor(textSelectedColor));
                break;
            case R.id.userL:
                userIV.setImageResource(R.mipmap.user_red);
                userTV.setTextColor(getResources().getColor(textSelectedColor));
                break;
        }
    }

    private void unSelect(int id) {
        switch (id) {
            case R.id.trendL:
                trendIV.setImageResource(R.mipmap.trend);
                trendTV.setTextColor(getResources().getColor(textColor));
                break;
            case R.id.followL:
                followIV.setImageResource(R.mipmap.follow);
                followTV.setTextColor(getResources().getColor(textColor));
                break;
            case R.id.messageL:
                messageIV.setImageResource(R.mipmap.message);
                messageTV.setTextColor(getResources().getColor(textColor));
                break;
            case R.id.tradeL:
                tradeIV.setImageResource(R.mipmap.trade);
                tradeTV.setTextColor(getResources().getColor(textColor));
                break;
            case R.id.userL:
                userIV.setImageResource(R.mipmap.user);
                userTV.setTextColor(getResources().getColor(textColor));
                break;
        }
    }
}
