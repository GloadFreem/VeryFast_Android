package com.jinzht.pro.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可开关滑动的ViewPager，用于PPT
 */
public class ScrollableViewPager extends ViewPager {

    private boolean scrollable = true;

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollable) {
            return super.onTouchEvent(ev);
        }
        return false;// 去掉ViewPager自带的滑动效果
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;// 不拦截事件
    }

    // 判断是否可滑动
    public boolean isScrollable() {
        return scrollable;
    }

    // 设置是否可滑动
    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
