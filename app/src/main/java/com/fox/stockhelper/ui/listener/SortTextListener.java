package com.fox.stockhelper.ui.listener;

import com.fox.stockhelper.ui.view.SortTextView;

/**
 * 排序组件的状态切换
 */
public interface SortTextListener {
    /**
     * 处理升序
     */
    void asc(SortTextView sortTextView);

    /**
     * 处理降序
     */
    void desc(SortTextView sortTextView);
}
