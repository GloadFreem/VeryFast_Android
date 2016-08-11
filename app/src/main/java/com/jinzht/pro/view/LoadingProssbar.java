package com.jinzht.pro.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.MediaController;

import com.jinzht.pro.R;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 加载中的进度动画效果
 */
public class LoadingProssbar extends ProgressDialog {

    //    private SpinKitView spinKitView;
//    private ImageView image;
    private Context context;
    private GifImageView gif;

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
//        image = (ImageView) findViewById(R.id.image);
//        Glide.with(context).load(R.mipmap.gif_loading).into(image);
        gif = (GifImageView) findViewById(R.id.gif);
        final GifDrawable gifDrawable = (GifDrawable) gif.getDrawable();
        MediaController mc = new MediaController(context);
        mc.setMediaPlayer(gifDrawable);
        mc.show();
        gifDrawable.addAnimationListener(new AnimationListener() {
                                             @Override
                                             public void onAnimationCompleted(int loopNumber) {
                                                 gifDrawable.reset();
                                             }
                                         }
        );
    }

}
