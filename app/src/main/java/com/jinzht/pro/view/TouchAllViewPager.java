package com.jinzht.pro.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 拦截所有滑动的ViewPager，用于banner
 */
public class TouchAllViewPager extends ViewPager {
    public TouchAllViewPager(Context context) {
        super(context);
    }

    public TouchAllViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int startX = 0;
        int startY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int dX = (int) (ev.getX() - startX);
                int dY = (int) (ev.getY() - startY);
                if (Math.abs(dX) > Math.abs(dY)) {//左右滑动
                    return super.onInterceptTouchEvent(ev);
                } else {//上下滑动
                    return true;
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
