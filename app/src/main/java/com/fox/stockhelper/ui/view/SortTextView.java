package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fox.stockhelper.R;
import com.fox.stockhelper.ui.listener.SortTextListener;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 排序文案
 */
public class SortTextView extends LinearLayout {
    /**
     * 未排序
     */
    private static final int SORT_NO = -1;
    /**
     * 升序
     */
    private static final int SORT_ASC = 0;
    /**
     * 降序
     */
    private static final int SORT_DESC = 1;
    /**
     * 当前排序方式
     */
    int currentSortType = SortTextView.SORT_NO;
    /**
     * 升降序处理
     */
    SortTextListener sortTextListener;
    /**
     * 文案
     */
    String sortTextStr = "价格";
    /**
     * 默认图片
     */
    int defaultImg = 0;
    /**
     * 非排序图片
     */
    int noSortImg = 0;
    /**
     * 升序图片
     */
    int ascImg = R.drawable.arrow_up;
    /**
     * 降序图片
     */
    int descImg = R.drawable.arrow_down;
    /**
     * 整体布局
     */
    @BindView(R.id.sortTextLL)
    LinearLayout sortTextLL;
    /**
     * 文本组件
     */
    @BindView(R.id.sortTextTV)
    TextView sortTextTV;
    /**
     * 图片组件
     */
    ImageView sortImgIV;
    /**
     * 图片是否已显示
     */
    Boolean imgIsShow = false;

    /**
     * 构造方法
     * @param context
     */
    public SortTextView(Context context) {
        super(context);
        this.initView();
    }

    /**
     * 构造方法
     * @param context
     */
    public SortTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initAttrs(attrs);
        this.initView();
    }

    /**
     * 构造方法
     * @param context
     */
    public SortTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initAttrs(attrs);
        this.initView();
    }

    /**
     * 构造方法
     * @param context
     */
    public SortTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initAttrs(attrs);
        this.initView();
    }

    /**
     * 初始化属性
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(
                attrs, R.styleable.SortTextView
        );
        String text = typedArray.getString(R.styleable.SortTextView_text);
        if (null != text && !"".equals(text)) {
            this.sortTextStr = text;
        }
    }

    /**
     * 初始化组件
     * @return
     */
    private View initView() {
        View view = LayoutInflater.from(this.getContext()).inflate(
                R.layout.view_sort_text, this, true
        );
        ButterKnife.bind(this, view);
        //初始化图片组件
        this.initImageView();
        //文案
        sortTextTV.setText(sortTextStr);
        //图片
        if(0 != defaultImg){
            this.changeImg(defaultImg);
        }
        return view;
    }

    /**
     * 初始化图片组件
     */
    private void initImageView() {
        sortImgIV = new ImageView(this.getContext());

        sortImgIV.setLayoutParams(
                new ViewGroup.LayoutParams(
                        40,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
    }

    /**
     * 修改图片
     * @param imgId
     */
    private void changeImg(int imgId) {
        sortImgIV.setImageResource(imgId);
        //判断图片是否已加载
        if (false == imgIsShow) {
            sortTextLL.addView(sortImgIV);
            imgIsShow = true;
        }
    }

    /**
     * 设置排序监听器
     * @param sortTextListener
     */
    public void setSortTextListener(SortTextListener sortTextListener) {
        this.sortTextListener = sortTextListener;
    }

    /**
     * 状态重置
     */
    public void reset() {
        sortImgIV.setImageResource(defaultImg);
        if (true == imgIsShow) {
            sortImgIV.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.sortTextLL)
    public void onViewClicked(View view) {
        switch (currentSortType) {
            case SortTextView.SORT_NO:
            case SortTextView.SORT_ASC:
                currentSortType = SortTextView.SORT_DESC;
                this.handleAsc();
                break;
            case SortTextView.SORT_DESC:
                currentSortType = SortTextView.SORT_ASC;
                this.handleDesc();
                break;
        }
    }

    /**
     * 处理升序
     */
    private void handleAsc(){
        //修改图片
        this.changeImg(ascImg);
        //处理升序
        if (null != sortTextListener) {
            sortTextListener.asc(this);
        }
    }

    /**
     * 处理降序
     */
    private void handleDesc(){
        //修改图片
        this.changeImg(descImg);
        //处理降序
        if (null != sortTextListener) {
            sortTextListener.desc(this);
        }
    }
}
