package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @author lusongsong
 * @date 2020/9/14 16:29
 */
public class NoTouchScrollViewpager extends ViewPager {
    public NoTouchScrollViewpager(@NonNull Context context) {
        super(context);
    }

    public NoTouchScrollViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }
}
