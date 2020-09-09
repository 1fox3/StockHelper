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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 排序文案
 */
public class SortTextView extends LinearLayout {
    /**
     * 未排序
     */
    public static final int SORT_NO = -1;
    /**
     * 升序
     */
    public static final int SORT_ASC = 0;
    /**
     * 降序
     */
    public static final int SORT_DESC = 1;
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
     * 排序字段
     */
    String sortColumn;
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
     * 单击事件监听器列表
     */
    List<OnClickListener> onClickListenerList;

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
        String sortColumn = typedArray.getString(R.styleable.SortTextView_sortColumn);
        if (null != text && !"".equals(text)) {
            this.sortColumn = sortColumn;
        }
        int sortType = typedArray.getInt(R.styleable.SortTextView_sortType, SortTextView.SORT_NO);
        currentSortType = sortType;
    }

    /**
     * 设置文案
     * @param sortTextStr
     */
    public void setSortTextStr(String sortTextStr) {
        this.sortTextStr = sortTextStr;
        //文案
        sortTextTV.setText(this.sortTextStr);
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
        switch (currentSortType) {
            case SortTextView.SORT_NO:
                defaultImg = noSortImg;
                break;
            case SortTextView.SORT_ASC:
                defaultImg = ascImg;
                break;
            case SortTextView.SORT_DESC:
                defaultImg = descImg;
                break;
        }
        //图片
        if (0 != defaultImg) {
            this.changeImg(defaultImg);
        }
        //点击事件监听
        this.initOnClicked();
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
        //判断图片是否已加载
        if (!imgIsShow) {
            this.initImageView();
            sortTextLL.addView(sortImgIV);
            imgIsShow = true;
        }
        sortImgIV.setImageResource(imgId);
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
        if (imgIsShow) {
            sortImgIV.setVisibility(View.GONE);
            imgIsShow = false;
        }
    }

    /**
     * 增加点击事件箭筒
     */
    public void initOnClicked() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentSortType) {
                    case SortTextView.SORT_NO:
                    case SortTextView.SORT_ASC:
                        currentSortType = SortTextView.SORT_DESC;
                        handleDesc();
                        break;
                    case SortTextView.SORT_DESC:
                        currentSortType = SortTextView.SORT_ASC;
                        handleAsc();
                        break;
                }
            }
        });
    }

    /**
     * 处理升序
     */
    private void handleAsc() {
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
    private void handleDesc() {
        //修改图片
        this.changeImg(descImg);
        //处理降序
        if (null != sortTextListener) {
            sortTextListener.desc(this);
        }
    }

    /**
     * 允许添加过个ClickListener
     * @param onClickListener
     */
    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        if (null == onClickListenerList) {
            onClickListenerList = new ArrayList<>();
        }
        onClickListenerList.add(onClickListener);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (OnClickListener clickListener : onClickListenerList){
                    clickListener.onClick(view);
                }
            }
        });
    }

    /**
     * 排序字段
     * @return
     */
    public String getSortColumn() {
        return null == sortColumn ? "" : sortColumn;
    }

    /**
     * 排序方式
     * @return
     */
    public int getSortType() {
        return currentSortType;
    }
}
