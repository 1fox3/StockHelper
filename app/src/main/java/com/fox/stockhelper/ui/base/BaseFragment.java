package com.fox.stockhelper.ui.base;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    /**
     * 上下文
     */
    protected Context context;

    /**
     * 设置上下文
     * @param context
     */
    protected void setContext(Context context) {
        this.context = context;
    }
}
