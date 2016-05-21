package com.jinzht.pro1.activity;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ActivityPhotosAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 活动详情页
 */
public class ActivityDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView title;// 页面标题
    private Button btnApply;// 报名按钮
    private Button btnShare;// 分享按钮
    private TextView activityTitle;// 活动标题
    private TextView activityDesc;// 活动描述
    private RecyclerView activityPhotos;// 活动照片
    private RelativeLayout btnMore;// 展开更多描述和照片
    private ImageButton imgMore;// 展开更多描述和照片的图标
    private TextView activityTime;// 活动时间
    private TextView activityNum;// 活动人数
    private TextView isFree;// 是否免费
    private TextView activityAddr;// 活动地点
    private TextView activityDistance;// 活动距离
    private TextView applyQuantity;// 报名人数
    private TextView btnQuantityTotle;// 查看全部报名人数
    private CircleImageView favicon1;// 报名人头像
    private TextView name1;// 报名人姓名
    private TextView position1;// 报名人职位
    private TextView time1;// 报名时间
    private CircleImageView favicon2;
    private TextView name2;
    private TextView position2;
    private TextView time2;
    private CircleImageView favicon3;
    private TextView name3;
    private TextView position3;
    private TextView time3;
    private CircleImageView favicon4;
    private TextView name4;
    private TextView position4;
    private TextView time4;
    private CircleImageView favicon5;
    private TextView name5;
    private TextView position5;
    private TextView time5;
    private TextView btnCommentTotle;// 查看全部点赞评论
    private TextView praiseNames;// 点赞人
    private TextView comment1;// 评论
    private TextView comment2;
    private TextView comment3;
    private TextView comment4;
    private TextView comment5;
    private TextView comment6;
    private ImageButton btnPraise;// 点赞按钮
    private ImageButton btnComment;// 评论按钮

    private boolean isOpen = false;// 活动描述的开关状态

    private List<Integer> photo1;// 闭合状态活动照片资源
    private List<Integer> photo2;// 展开状态活动照片资源
    private ActivityPhotosAdapter photosAdapter1;// 闭合状态活动照片的适配器
    private ActivityPhotosAdapter photosAdapter2;// 展开状态活动照片的适配器


    @Override
    protected int getResourcesId() {
        return R.layout.activity_activity_detail;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        // 活动照片处理
        initPhotos();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_title);// 页面标题
        title.setText("活动详情");
        btnApply = (Button) findViewById(R.id.activity_btn_apply);// 报名按钮
        btnApply.setOnClickListener(this);
        btnShare = (Button) findViewById(R.id.activity_btn_share);// 分享按钮
        btnShare.setOnClickListener(this);
        activityTitle = (TextView) findViewById(R.id.activity_tv_title);// 活动标题
        activityDesc = (TextView) findViewById(R.id.activity_tv_desc);// 活动描述
        activityPhotos = (RecyclerView) findViewById(R.id.activity_rv_photos);// 活动照片
        btnMore = (RelativeLayout) findViewById(R.id.activity_btn_more);// 展开更多内容
        btnMore.setOnClickListener(this);
        imgMore = (ImageButton) findViewById(R.id.activity_img_more);// 展开更多图标
        activityTime = (TextView) findViewById(R.id.activity_tv_time);// 活动时间
        activityNum = (TextView) findViewById(R.id.activity_tv_num);// 活动人数
        isFree = (TextView) findViewById(R.id.activity_tv_free);// 是否免费
        activityAddr = (TextView) findViewById(R.id.activity_tv_addr);// 活动地点
        activityDistance = (TextView) findViewById(R.id.activity_tv_distance);// 距离
        applyQuantity = (TextView) findViewById(R.id.activity_tv_quantity);// 报名人数
        btnQuantityTotle = (TextView) findViewById(R.id.activity_btn_quantity_totle);// 查看全部报名人数按钮
        btnQuantityTotle.setOnClickListener(this);
        favicon1 = (CircleImageView) findViewById(R.id.activity_iv_favicon1);// 报名人头像
        name1 = (TextView) findViewById(R.id.activity_tv_name1);// 报名人姓名
        position1 = (TextView) findViewById(R.id.activity_tv_position1);// 报名人职位
        time1 = (TextView) findViewById(R.id.activity_tv_time1);// 报名时间
        favicon2 = (CircleImageView) findViewById(R.id.activity_iv_favicon2);
        name2 = (TextView) findViewById(R.id.activity_tv_name2);
        position2 = (TextView) findViewById(R.id.activity_tv_position2);
        time2 = (TextView) findViewById(R.id.activity_tv_time2);
        favicon3 = (CircleImageView) findViewById(R.id.activity_iv_favicon3);
        name3 = (TextView) findViewById(R.id.activity_tv_name3);
        position3 = (TextView) findViewById(R.id.activity_tv_position3);
        time3 = (TextView) findViewById(R.id.activity_tv_time3);
        favicon4 = (CircleImageView) findViewById(R.id.activity_iv_favicon4);
        name4 = (TextView) findViewById(R.id.activity_tv_name4);
        position4 = (TextView) findViewById(R.id.activity_tv_position4);
        time4 = (TextView) findViewById(R.id.activity_tv_time4);
        favicon5 = (CircleImageView) findViewById(R.id.activity_iv_favicon5);
        name5 = (TextView) findViewById(R.id.activity_tv_name5);
        position5 = (TextView) findViewById(R.id.activity_tv_position5);
        time5 = (TextView) findViewById(R.id.activity_tv_time5);
        btnCommentTotle = (TextView) findViewById(R.id.activity_btn_comment_totle);// 查看全部点赞和评论按钮
        btnCommentTotle.setOnClickListener(this);
        praiseNames = (TextView) findViewById(R.id.activity_tv_praise);// 点赞人
        comment1 = (TextView) findViewById(R.id.activity_tv_comment1);// 评论
        comment2 = (TextView) findViewById(R.id.activity_tv_comment2);
        comment3 = (TextView) findViewById(R.id.activity_tv_comment3);
        comment4 = (TextView) findViewById(R.id.activity_tv_comment4);
        comment5 = (TextView) findViewById(R.id.activity_tv_comment5);
        comment6 = (TextView) findViewById(R.id.activity_tv_comment6);// 省略号，多余5条才显示
        btnPraise = (ImageButton) findViewById(R.id.activity_btn_praise);// 点赞按钮
        btnPraise.setOnClickListener(this);
        btnComment = (ImageButton) findViewById(R.id.activity_btn_comment);// 评论按钮
        btnComment.setOnClickListener(this);
    }

    // 活动照片处理
    private void initPhotos() {
        // 准备数据
        photo1 = new ArrayList<>(Arrays.asList(R.mipmap.activity_photo1, R.mipmap.activity_photo2, R.mipmap.activity_photo3));
        photosAdapter1 = new ActivityPhotosAdapter(mContext, photo1);
        // 填充数据
        RecyclerViewData.setGrid(activityPhotos, mContext, photosAdapter1, 3);
        // 活动照片的点击事件
        photosAdapter1.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + position + "张照片");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.activity_btn_apply:// 点击报名
                SuperToastUtils.showSuperToast(this, 2, "报名");
                break;
            case R.id.activity_btn_share:// 点击分享
                SuperToastUtils.showSuperToast(this, 2, "分享");
                break;
            case R.id.activity_btn_more:// 展开更多内容
                if (isOpen) {// 打开状态，点击关闭
                    descClose();
                    isOpen = false;
                } else {// 关闭状态，点击打开
                    descOpen();
                    isOpen = true;
                }
                break;
            case R.id.activity_btn_quantity_totle:// 查看全部报名人
                SuperToastUtils.showSuperToast(this, 2, "全部报名人");
                break;
            case R.id.activity_btn_comment_totle:// 查看全部点赞和评论
                SuperToastUtils.showSuperToast(this, 2, "全部点赞和评论");
                break;
            case R.id.activity_btn_praise:// 点赞
                SuperToastUtils.showSuperToast(this, 2, "赞");
                break;
            case R.id.activity_btn_comment:// 评论
                SuperToastUtils.showSuperToast(this, 2, "评论");
                break;
        }
    }

    // 闭合TextView
    private void descClose() {
        activityDesc.setEllipsize(TextUtils.TruncateAt.END);
        activityDesc.setMaxLines(3);
        imgMore.setBackgroundResource(R.mipmap.icon_activity_more);
        // 项目照片
        activityPhotos.setAdapter(photosAdapter1);
    }

    // 打开TextView
    private void descOpen() {
        activityDesc.setEllipsize(null);
        activityDesc.setMaxLines(Integer.MAX_VALUE);
        imgMore.setBackgroundResource(R.mipmap.icon_activity_less);
        // 项目照片
        photo2 = new ArrayList<Integer>(Arrays.asList(R.mipmap.activity_photo1, R.mipmap.activity_photo2, R.mipmap.activity_photo3, R.mipmap.activity_photo1, R.mipmap.activity_photo3));
        photosAdapter2 = new ActivityPhotosAdapter(mContext, photo2);
        photosAdapter2.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + position + "张照片");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
        activityPhotos.setAdapter(photosAdapter2);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
