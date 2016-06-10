package com.jinzht.pro1.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Glide模式加载图片
 */
public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存全尺寸
                .skipMemoryCache(true)
                        //.centerCrop()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}