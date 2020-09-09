package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * 监听滚动的横向ScrollView
 * @author lusongsong
 * @date 2020/9/8 17:46
 */
public class ScrollListenerHorizontalScrollView extends HorizontalScrollView {
    /**
     * 构造函数
     * @param context
     */
    public ScrollListenerHorizontalScrollView(Context context) {
        super(context);
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public ScrollListenerHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ScrollListenerHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 滚动状态 IDLE 滚动停止  TOUCH_SCROLL 手指拖动滚动         FLING滚动
     */
    public static int IDLE = 0;
    public static int TOUCH_SCROLL = 1;
    public static int FLING = 2;
    /**
     * 滚动监听接口
     */
    public interface ScrollViewListener {
        /**
         * 滚动状态变化
         * @param scrollType
         */
        void onScrollStateChanged(int scrollType);

        /**
         * 滚动距离
         * @param dx
         */
        void onScrolled(int dx);
    }
    /**
     * 处理类
     */
    private Handler mHandler;
    /**
     * 滚动监听类
     */
    private ScrollViewListener scrollViewListener;
    /**
     * 当前滚动距离
     */
    private int currentScrollX = 0;
    /**
     * 记录当前滚动的距离
     */
    private int currentX = 0;
    /**
     * 当前滚动状态
     */
    private int scrollType = IDLE;
    /**
     * 滚动监听间隔
     */
    private long scrollDelay = 200;
    /**
     * 滚动监听runnable
     */
    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollX = getScrollX();
            boolean isScrolling = scrollX != currentX;
            currentX = scrollX;
            if (isScrolling) {
                //手指离开屏幕 view还在滚动的时候
                if (scrollType != FLING) {
                    scrollType = FLING;
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStateChanged(scrollType);
                    }
                }
            } else {
                //滚动停止 取消监听线程
                if (scrollType != IDLE) {
                    scrollType = IDLE;
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStateChanged(scrollType);
                    }
                    mHandler.removeCallbacks(scrollRunnable);
                }
                return;
            }
            mHandler.postDelayed(this, scrollDelay);
        }
    };

    /**
     * 监听触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (this.scrollType == IDLE) {
                    this.scrollType = TOUCH_SCROLL;
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStateChanged(scrollType);
                    }
                    //手指移动的时候
                    mHandler.post(scrollRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 设置处理类
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    /**
     * 设置滚动监听
     */
    public void setOnScrollStateChangedListener(ScrollViewListener listener) {
        this.scrollViewListener = listener;
    }

    /**
     * 监听滚动
     * @param scrollX
     * @param scrollY
     * @param clampedX
     * @param clampedY
     */
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        scrollX = scrollX < 0 ? 0 : scrollX;
        scrollViewListener.onScrolled(scrollX - currentScrollX);
        currentScrollX = scrollX;
    }

    /**
     * 滚动
     * @param x
     * @param y
     */
    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
        currentScrollX += x;
    }
}
