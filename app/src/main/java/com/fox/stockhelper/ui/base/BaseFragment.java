package com.fox.stockhelper.ui.base;

import android.content.Context;

import androidx.fragment.app.Fragment;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseFragment extends Fragment {
    /**
     * 上下文
     */
    protected Context context;

    /**
     * 指定上下文构造器
     *
     * @param context
     */
    public BaseFragment(Context context) {
        this.context = context;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public Context getContext() {
        return this.context;
    }
}
