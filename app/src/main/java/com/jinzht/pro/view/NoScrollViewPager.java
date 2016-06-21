package com.jinzht.pro.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不可滑动的ViewPager，用于主页框架
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;// 去掉ViewPager自带的滑动效果
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;// 不拦截事件
    }
}
