package com.fox.stockhelper.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fox.spider.stock.constant.StockConst;
import com.fox.stockhelper.R;
import com.fox.stockhelper.config.ActivityRequestCodeConfig;
import com.fox.stockhelper.ui.adapter.StockMarketFragmentAdapter;
import com.fox.stockhelper.ui.base.BaseActivity;
import com.fox.stockhelper.ui.fragment.StockMarketFragment;
import com.fox.stockhelper.ui.view.HomeBottomView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * APP入口类
 *
 * @author lusongsong
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    /**
     * 搜索输入框
     */
    @BindView(R.id.search)
    EditText searchET;
    /**
     * 当前选择的股市序号
     */
    int currentStockMarketNum = 0;
    /**
     * 股市页面切换
     */
    @BindView(R.id.stock_type_vp)
    ViewPager stockTypeVP;
    /**
     * 股市页面title列表
     */
    List<TextView> smTextViewList;
    /**
     * 股市选择下划线列表
     */
    List<ImageView> smImageViewList;
    /**
     * 股市页面列表
     */
    List<Fragment> smFragmentList;
    @BindView(R.id.homeRL)
    RelativeLayout homeRL;
    /**
     * 股市名称
     */
    @BindView(R.id.smNameLL)
    LinearLayout smNameLL;
    /**
     * 股市选中下划线图片
     */
    @BindView(R.id.smCursorLL)
    LinearLayout smCursorLL;
    /**
     * 底部菜单
     */
    HomeBottomView homeBottomView;

    /**
     * 股市列表
     */
    public List<Integer> SM_ALL = Arrays.asList(StockConst.SM_A, StockConst.SM_HK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        //将搜索输入框里的图片处理到合适大小
        Drawable searchDraw = getResources().getDrawable(R.drawable.search);
        searchDraw.setBounds(0, 0, 80, 80);
        searchET.setCompoundDrawables(searchDraw, null, null, null);

        //用户未登录，则跳转到登录页面
        if (!this.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fromUI", getLocalClassName());
            intent.putExtra("login", bundle);
            startActivityForResult(intent, ActivityRequestCodeConfig.LOGIN);
        }

        //初始化底部菜单按钮
        this.initHomeBottom();

        //股市页面初始化
        this.initStockMarket();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case 1:
                Log.e("loginSuccess", this.getLoginSession());
                break;
        }
    }

    /**
     * 股市内容初始化
     */
    private void initStockMarket() {
        if (SM_ALL.isEmpty()) {
            return;
        }

        smFragmentList = new ArrayList<>(SM_ALL.size());
        smTextViewList = new ArrayList<>(SM_ALL.size());
        smImageViewList = new ArrayList<>(SM_ALL.size());
        for (Integer stockMarket : SM_ALL) {
            //初始化股市页面列表
            smFragmentList.add(new StockMarketFragment(this, stockMarket));
            //股市名称
            TextView textView = getSMNameTextView(stockMarket, smFragmentList.size() - 1);
            smTextViewList.add(textView);
            smNameLL.addView(textView);
            //股市下滑线
            ImageView imageView = getSMCursorImageView(stockMarket);
            smImageViewList.add(imageView);
            smCursorLL.addView(imageView);
        }

        //股市页面切换器
        StockMarketFragmentAdapter stockMarketFragmentAdapter =
                new StockMarketFragmentAdapter(getSupportFragmentManager(), smFragmentList);
        stockTypeVP.setAdapter(stockMarketFragmentAdapter);
        stockTypeVP.addOnPageChangeListener(this);
        this.chooseStockMarket(currentStockMarketNum);
    }

    /**
     * 获取集市名称组件
     *
     * @param stockMarket
     * @return
     */
    private TextView getSMNameTextView(Integer stockMarket, Integer currentSMId) {
        TextView textView = new TextView(this);
        textView.setText(StockConst.stockMarketName(stockMarket));
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1
        ));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setOnClickListener(view -> chooseStockMarket(currentSMId));
        return textView;
    }

    /**
     * 获取股市选中图片组件
     *
     * @param stockMarket
     * @return
     */
    private ImageView getSMCursorImageView(Integer stockMarket) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1
        ));
        return imageView;
    }

    /**
     * 选择股市
     *
     * @param smNum
     */
    @SuppressLint("ResourceAsColor")
    private void chooseStockMarket(int smNum) {
        if (smNum < 0 || smNum >= smFragmentList.size()) {
            return;
        }
        this.unChooseStockMarket();
        currentStockMarketNum = smNum;
        //切换页面
        stockTypeVP.setCurrentItem(smNum);
        (smTextViewList.get(smNum)).setTextColor(getResources().getColor(R.color.main));
        (smImageViewList.get(smNum)).setBackgroundColor(getResources().getColor(R.color.main));
    }

    /**
     * 反选股市
     */
    private void unChooseStockMarket() {
        if (currentStockMarketNum < 0 || currentStockMarketNum >= smFragmentList.size()) {
            return;
        }
        smTextViewList.get(currentStockMarketNum).setTextColor(Color.GRAY);
        smImageViewList.get(currentStockMarketNum).setBackgroundResource(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        chooseStockMarket(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 初始化迪比菜单
     */
    private void initHomeBottom() {
        homeBottomView = new HomeBottomView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        homeRL.addView(homeBottomView, layoutParams);
    }
}
