package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jinzht.pro.R;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.adapter.ReleasePhotosAdapter;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.ReleaseCircleBean;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 在圈子发布话题界面
 */
public class ReleaseCircleActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private LinearLayout btnRelease;// 发布
    private EditText edContent;// 发布的内容
    private RecyclerView recyclerview;// 图片

    ReleasePhotosAdapter adapter;// RecyclerView数据填充器
    List<String> photos = new ArrayList<>();// 要发布的图片地址

    public final static int RESULT_CODE = 0;// 返回到圈子列表
    public static final int REQUEST_CODE_GALLERY = 2;// 跳转到图片选择器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_release_circle;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnRelease = (LinearLayout) findViewById(R.id.title_btn_right);// 发布
        btnRelease.setOnClickListener(this);
        edContent = (EditText) findViewById(R.id.ed_content);// 发布的内容
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);// 图片

        initRecyclerView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.title_btn_right:// 发布
                Log.i("选择的图片路径", photos.toString());
                if (!StringUtils.isBlank(edContent.getText().toString()) || photos.size() != 0) {
                    ReleaseCircleTask releaseCircleTask = new ReleaseCircleTask();
                    releaseCircleTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入内容或选择图片");
                }
                break;
        }
    }

    private void initRecyclerView() {
        // 准备数据
        adapter = new ReleasePhotosAdapter(this, photos);
        // 填充数据
        RecyclerViewData.setGrid(recyclerview, mContext, adapter, 3);
        // 条目点击事件
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (photos.size() == 9) {
//                    SuperToastUtils.showSuperToast(mContext, 2, "点击了照片" + position);
                } else if (photos.size() == 0) {
                    GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, 9 - photos.size(), mOnHanlderResultCallback);
                } else if (photos.size() > 0 && photos.size() < 9) {
                    if (position == photos.size()) {
                        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, 9 - photos.size(), mOnHanlderResultCallback);
                    } else {
//                        SuperToastUtils.showSuperToast(mContext, 2, "点击了照片" + position);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    // 返回选择的图片
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                for (PhotoInfo item : resultList) {
                    photos.add(item.getPhotoPath());
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    // 发表动态
    private class ReleaseCircleTask extends AsyncTask<Void, Void, ReleaseCircleBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("");
        }

        @Override
        protected ReleaseCircleBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.releaseCirclePost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.RELEASECIRCLE)),
                            "content", edContent.getText().toString(),
                            "images", photos,
                            Constant.BASE_URL + Constant.RELEASECIRCLE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("发表状态", body);
                return FastJsonTools.getBean(body, ReleaseCircleBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ReleaseCircleBean releaseCircleBean) {
            super.onPostExecute(releaseCircleBean);
            dismissProgressDialog();
            if (releaseCircleBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (releaseCircleBean.getStatus() == 200) {
                    Intent intent = new Intent();
                    setResult(RESULT_CODE, intent);
                    finish();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, releaseCircleBean.getMessage());
                }
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
