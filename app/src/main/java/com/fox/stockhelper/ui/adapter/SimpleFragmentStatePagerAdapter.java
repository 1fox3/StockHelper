package com.fox.stockhelper.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @author lusongsong
 * @date 2020/9/14 16:16
 */
public class SimpleFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public SimpleFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentList = fragments;
    }

    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        this(fm, fragments);
        titleList = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList != null && titleList.size() > 0 && titleList.size() > position) {
            return titleList.get(position);
        }
        return super.getPageTitle(position);
    }


}
