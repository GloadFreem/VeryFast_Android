package com.jinzht.pro1.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 自适应高度的ViewPager，有bug，暂时解决不了，但能出效果
 */
public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

        final View view = getChildAt(0);
        int width = getMeasuredWidth();
        int tabHeight = view.getMeasuredHeight();

        if (wrapHeight) {
            // Keep the current measured width.
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }

        int fragmentHeight = measureFragment(((Fragment) getAdapter().instantiateItem(this,
                getCurrentItem())).getView());
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(tabHeight + fragmentHeight +
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()), MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int measureFragment(View view) {
        if (view == null) {
            return 0;
        } else {
            view.measure(0, 0);
            return view.getMeasuredHeight();
        }
    }
}
