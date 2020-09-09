package com.fox.stockhelper.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * 监听滚动的横向ScrollView
 * @author lusongsong
 * @date 2020/9/8 17:46
 */
public class ScrollListenerHorizontalScrollView extends HorizontalScrollView {

    public ScrollListenerHorizontalScrollView(Context context) {
        super(context);
    }

    public ScrollListenerHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListenerHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface ScrollViewListener {
        void onScrollStateChanged(int scrollType);

        void onScrolled(int dx);
    }

    private Handler mHandler;
    private ScrollViewListener scrollViewListener;

    /**
     * 滚动状态 IDLE 滚动停止  TOUCH_SCROLL 手指拖动滚动         FLING滚动
     */
    public static int IDLE = 0;
    public static int TOUCH_SCROLL = 1;
    public static int FLING = 2;

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
    private long scrollDelay = 10;
    /**
     * 滚动监听runnable
     */
    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (getScrollX() == currentX) {
                //滚动停止 取消监听线程
                scrollType = IDLE;
                if (scrollViewListener != null) {
                    scrollViewListener.onScrollStateChanged(scrollType);
                }
                return;
            } else {
                //手指离开屏幕 view还在滚动的时候
                scrollType = FLING;
                if (scrollViewListener != null) {
                    scrollViewListener.onScrollStateChanged(scrollType);
                }
            }
            currentX = getScrollX();
            mHandler.postDelayed(this, scrollDelay);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("event", String.valueOf(ev.getAction()));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.scrollType = TOUCH_SCROLL;
                if (scrollViewListener != null) {
                    scrollViewListener.onScrollStateChanged(scrollType);
                }
                //手指移动的时候
                mHandler.post(scrollRunnable);
                break;
            case MotionEvent.ACTION_UP:
                //手指在上面移动的时候   取消滚动监听线程
                mHandler.removeCallbacks(scrollRunnable);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 必须先调用这个方法设置Handler  不然会出错
     * 2014-12-7 下午3:55:39
     *
     * @param handler
     * @return void
     * @author DZC
     * @TODO
     */
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    /**
     * 设置滚动监听
     * 2014-12-7 下午3:59:51
     *
     * @param listener
     * @return void
     * @author DZC
     * @TODO
     */
    public void setOnScrollStateChangedListener(ScrollViewListener listener) {
        this.scrollViewListener = listener;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        scrollViewListener.onScrolled(scrollX - currentX);
    }
}
