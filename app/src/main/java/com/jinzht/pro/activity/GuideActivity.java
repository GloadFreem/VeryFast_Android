package com.jinzht.pro.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jinzht.pro.R;
import com.jinzht.pro.base.FullBaseActivity;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends FullBaseActivity {

    private ViewPager viewpager;
    private LinearLayout llPoints;
    private ImageButton btnEnter;

    private List<ImageView> imageViews = new ArrayList<>();
    private List<Integer> imgs = new ArrayList<>(Arrays.asList(R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3, R.mipmap.guide_4, R.mipmap.guide_5));

    @Override
    protected int getResourcesId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        llPoints = (LinearLayout) findViewById(R.id.ll_points);
        btnEnter = (ImageButton) findViewById(R.id.btn_enter);

        imageViews.clear();
        llPoints.removeAllViews();
        for (int i = 0; i < imgs.size(); i++) {
            ImageView imageView = new ImageView(this);
            // 设置图片缩放类型
            imageView.setBackgroundResource(imgs.get(i));
            imageViews.add(imageView);
            // 创建圆点指示器
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UiUtils.dip2px(7), UiUtils.dip2px(7));
            if (i != 0) {
                params.leftMargin = UiUtils.dip2px(10);
            }
            point.setLayoutParams(params);
            point.setEnabled(false);
            point.setBackgroundResource(R.drawable.selector_guide_point);
            // 将圆点指示器添加到线性容器中
            llPoints.addView(point);
        }
        viewpager.setAdapter(new MyAdapter());
        viewpager.addOnPageChangeListener(new MyListener());
        llPoints.getChildAt(0).setEnabled(true);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                SharedPreferencesUtils.setIsNotFirst(mContext, true);
                finish();
            }
        });
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class MyListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < imageViews.size(); i++) {
                if (position == i) {
                    llPoints.getChildAt(i).setEnabled(true);
                } else {
                    llPoints.getChildAt(i).setEnabled(false);
                }
            }
            if (position == imageViews.size() - 1) {
                llPoints.setVisibility(View.GONE);
                btnEnter.setVisibility(View.VISIBLE);
            } else {
                llPoints.setVisibility(View.VISIBLE);
                btnEnter.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
