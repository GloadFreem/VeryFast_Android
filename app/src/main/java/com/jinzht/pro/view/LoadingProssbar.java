package com.jinzht.pro.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;

/**
 * 加载中的进度动画效果
 */
public class LoadingProssbar extends ProgressDialog {

//    private SpinKitView spinKitView;
    private ImageView image;
    private Context context;

    public LoadingProssbar(Context context) {
        super(context, R.style.MyWidget_DialogFull);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_loading);
//        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
//        ChasingDots chasingDots = new ChasingDots();
//        spinKitView.setIndeterminateDrawable(chasingDots);
        setContentView(R.layout.dialog_loading_gif);
        image = (ImageView) findViewById(R.id.image);
        Glide.with(context).load(R.mipmap.gif_loading).into(image);
    }

}
