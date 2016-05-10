package com.jinzht.pro1.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.jinzht.pro1.R;

/**
 * 加载中的进度动画效果
 */
public class LoadingProssbar extends ProgressDialog {

    private SpinKitView spinKitView;

    public LoadingProssbar(Context context) {
        super(context, R.style.MyWidget_DialogFull);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        ChasingDots chasingDots = new ChasingDots();
        spinKitView.setIndeterminateDrawable(chasingDots);
    }

}
