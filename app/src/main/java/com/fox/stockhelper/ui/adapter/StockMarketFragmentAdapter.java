package com.fox.stockhelper.ui.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 股市切换适配器
 * @author lusongsong
 */
public class StockMarketFragmentAdapter extends FragmentPagerAdapter {
    /**
     * 股市页面列表
     */
    private List<Fragment> stockMarketFragmentList;

    /**
     * 股市切换适配器
     * @param fm
     * @param arrayList
     */
    public StockMarketFragmentAdapter(FragmentManager fm, List<Fragment> arrayList) {
        super(fm);
        this.stockMarketFragmentList = arrayList;
    }

    /**
     * 选择页面
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return this.stockMarketFragmentList.get(position);
    }

    /**
     * 获取数量
     * @return
     */
    @Override
    public int getCount() {
        return this.stockMarketFragmentList.size();
    }
}
