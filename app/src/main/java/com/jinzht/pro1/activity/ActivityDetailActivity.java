package com.jinzht.pro1.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ActivityPhotosAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.ActivityAllApplysBean;
import com.jinzht.pro1.bean.ActivityAllCommentsBean;
import com.jinzht.pro1.bean.ActivityDetailBean;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.DateUtils;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.CircleImageView;

import java.util.ArrayList;
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
    private RelativeLayout rlApply1;// 报名人布局
    private CircleImageView favicon1;// 报名人头像
    private TextView name1;// 报名人姓名
    private TextView position1;// 报名人职位
    private TextView time1;// 报名时间
    private RelativeLayout rlApply2;
    private CircleImageView favicon2;
    private TextView name2;
    private TextView position2;
    private TextView time2;
    private RelativeLayout rlApply3;
    private CircleImageView favicon3;
    private TextView name3;
    private TextView position3;
    private TextView time3;
    private RelativeLayout rlApply4;
    private CircleImageView favicon4;
    private TextView name4;
    private TextView position4;
    private TextView time4;
    private RelativeLayout rlApply5;
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

    private ActivityDetailBean.DataBean data = new ActivityDetailBean.DataBean();// 内容数据
    private List<ActivityDetailBean.DataBean.ActionimagesBean> photos = new ArrayList<>();// 活动照片资源
    private ActivityPhotosAdapter photosAdapter;// 活动照片的适配器

    private List<ActivityAllApplysBean.DataBean> applys = new ArrayList<>();// 全部报名人
    private List<ActivityAllCommentsBean.DataBean.CommentsBean> comments = new ArrayList<>();// 全部评论人

    @Override
    protected int getResourcesId() {
        return R.layout.activity_activity_detail;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        GetActivityDetailTask getActivityDetailTask = new GetActivityDetailTask();
        getActivityDetailTask.execute();
        GetAllApplysTask getAllApplysTask = new GetAllApplysTask();
        getAllApplysTask.execute();
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
        rlApply1 = (RelativeLayout) findViewById(R.id.rl_apply1);// 报名人整体布局
        favicon1 = (CircleImageView) findViewById(R.id.activity_iv_favicon1);// 报名人头像
        name1 = (TextView) findViewById(R.id.activity_tv_name1);// 报名人姓名
        position1 = (TextView) findViewById(R.id.activity_tv_position1);// 报名人职位
        time1 = (TextView) findViewById(R.id.activity_tv_time1);// 报名时间
        rlApply2 = (RelativeLayout) findViewById(R.id.rl_apply2);
        favicon2 = (CircleImageView) findViewById(R.id.activity_iv_favicon2);
        name2 = (TextView) findViewById(R.id.activity_tv_name2);
        position2 = (TextView) findViewById(R.id.activity_tv_position2);
        time2 = (TextView) findViewById(R.id.activity_tv_time2);
        rlApply3 = (RelativeLayout) findViewById(R.id.rl_apply3);
        favicon3 = (CircleImageView) findViewById(R.id.activity_iv_favicon3);
        name3 = (TextView) findViewById(R.id.activity_tv_name3);
        position3 = (TextView) findViewById(R.id.activity_tv_position3);
        time3 = (TextView) findViewById(R.id.activity_tv_time3);
        rlApply4 = (RelativeLayout) findViewById(R.id.rl_apply4);
        favicon4 = (CircleImageView) findViewById(R.id.activity_iv_favicon4);
        name4 = (TextView) findViewById(R.id.activity_tv_name4);
        position4 = (TextView) findViewById(R.id.activity_tv_position4);
        time4 = (TextView) findViewById(R.id.activity_tv_time4);
        rlApply5 = (RelativeLayout) findViewById(R.id.rl_apply5);
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

    // 加载活动内容数据
    private void initData() {
        activityTitle.setText(data.getName());
        activityDesc.setText(data.getDescription());
        String time = data.getStartTime();
        activityTime.setText(time.substring(0, 10).replaceAll("-", ".") + " " + DateUtils.getWeek(time) + " " + time.substring(11, 16));
        activityNum.setText(data.getMemberLimit() + "人");
        if (data.getType() == 1) {
            isFree.setText("免费");
        } else {
            isFree.setText("付费");
        }
        activityAddr.setText(data.getAddress());
        activityDistance.setText("");
        initPhotos();
        if (data.getActionprises() != null && data.getActionprises().size() != 0) {
            praiseNames.setText("");
        } else {
            String names = data.getActionprises().toString();
            praiseNames.setText(names.substring(1, names.length() - 1));
        }
        if (data.getFlag() == 1) {
            btnApply.setText("已报名");
            btnApply.setBackgroundColor(Color.parseColor("#cccccc"));
            btnApply.setClickable(false);
        } else {
            btnApply.setText("我要报名");
            btnApply.setBackgroundColor(UiUtils.getColor(R.color.custom_orange));
            btnApply.setClickable(true);
        }
    }

    // 活动照片处理
    private void initPhotos() {
        // 准备数据
        photos.add(data.getActionimages().get(0));
        photos.add(data.getActionimages().get(1));
        photos.add(data.getActionimages().get(2));
        photosAdapter = new ActivityPhotosAdapter(mContext, photos);
        // 填充数据
        RecyclerViewData.setGrid(activityPhotos, mContext, photosAdapter, 3);
        // 活动照片的点击事件
        photosAdapter.setItemClickListener(new ItemClickListener() {
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

    // 加载全部报名人
    private void initApplys() {
        applyQuantity.setText("(" + applys.size() + ")");
        if (applys.size() == 0) {
            rlApply1.setVisibility(View.GONE);
            rlApply2.setVisibility(View.GONE);
            rlApply3.setVisibility(View.GONE);
            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 1) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            rlApply2.setVisibility(View.GONE);
            rlApply3.setVisibility(View.GONE);
            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 2) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            rlApply3.setVisibility(View.GONE);
            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 3) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            Glide.with(mContext).load(applys.get(2).getUsers().getHeadSculpture()).into(favicon3);
            name3.setText(applys.get(2).getUsers().getAuthentics().get(0).getName());
            position3.setText(applys.get(2).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(2).getUsers().getAuthentics().get(0).getPosition());
            time3.setText(DateUtils.timeLogic(applys.get(2).getEnrollDate()));

            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 4) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            Glide.with(mContext).load(applys.get(2).getUsers().getHeadSculpture()).into(favicon3);
            name3.setText(applys.get(2).getUsers().getAuthentics().get(0).getName());
            position3.setText(applys.get(2).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(2).getUsers().getAuthentics().get(0).getPosition());
            time3.setText(DateUtils.timeLogic(applys.get(2).getEnrollDate()));

            Glide.with(mContext).load(applys.get(3).getUsers().getHeadSculpture()).into(favicon4);
            name4.setText(applys.get(3).getUsers().getAuthentics().get(0).getName());
            position4.setText(applys.get(3).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(3).getUsers().getAuthentics().get(0).getPosition());
            time4.setText(DateUtils.timeLogic(applys.get(3).getEnrollDate()));

            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() >= 5) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            Glide.with(mContext).load(applys.get(2).getUsers().getHeadSculpture()).into(favicon3);
            name3.setText(applys.get(2).getUsers().getAuthentics().get(0).getName());
            position3.setText(applys.get(2).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(2).getUsers().getAuthentics().get(0).getPosition());
            time3.setText(DateUtils.timeLogic(applys.get(2).getEnrollDate()));

            Glide.with(mContext).load(applys.get(3).getUsers().getHeadSculpture()).into(favicon4);
            name4.setText(applys.get(3).getUsers().getAuthentics().get(0).getName());
            position4.setText(applys.get(3).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(3).getUsers().getAuthentics().get(0).getPosition());
            time4.setText(DateUtils.timeLogic(applys.get(3).getEnrollDate()));

            Glide.with(mContext).load(applys.get(4).getUsers().getHeadSculpture()).into(favicon5);
            name5.setText(applys.get(4).getUsers().getAuthentics().get(0).getName());
            position5.setText(applys.get(4).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(4).getUsers().getAuthentics().get(0).getPosition());
            time5.setText(DateUtils.timeLogic(applys.get(4).getEnrollDate()));
        }
    }

    // 加载全部评论
    private void initComments(){

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
        photos.clear();
        photos.add(data.getActionimages().get(0));
        photos.add(data.getActionimages().get(1));
        photos.add(data.getActionimages().get(2));
        photosAdapter.notifyDataSetChanged();
    }

    // 打开TextView
    private void descOpen() {
        activityDesc.setEllipsize(null);
        activityDesc.setMaxLines(Integer.MAX_VALUE);
        imgMore.setBackgroundResource(R.mipmap.icon_activity_less);
        // 项目照片
        photos.clear();
        for (ActivityDetailBean.DataBean.ActionimagesBean bean : data.getActionimages()) {
            photos.add(bean);
        }
        photosAdapter.notifyDataSetChanged();
    }

    // 获取活动内容详情
    private class GetActivityDetailTask extends AsyncTask<Void, Void, ActivityDetailBean> {
        @Override
        protected ActivityDetailBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETACTIVITYDETAIL)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            Constant.BASE_URL + Constant.GETACTIVITYDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("活动详情", body);
                return FastJsonTools.getBean(body, ActivityDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityDetailBean activityDetailBean) {
            super.onPostExecute(activityDetailBean);
            if (activityDetailBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityDetailBean.getStatus() == 200) {
                    data = activityDetailBean.getData();
                    if (data != null) {
                        initData();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityDetailBean.getMessage());
                }
            }
        }
    }

    // 获取全部报名人
    private class GetAllApplysTask extends AsyncTask<Void, Void, ActivityAllApplysBean> {
        @Override
        protected ActivityAllApplysBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETALLACTIVITYAPPLYS)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            "page", "0",
                            Constant.BASE_URL + Constant.GETALLACTIVITYAPPLYS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("全部报名人", body);
                return FastJsonTools.getBean(body, ActivityAllApplysBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityAllApplysBean activityAllApplysBean) {
            super.onPostExecute(activityAllApplysBean);
            if (activityAllApplysBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityAllApplysBean.getStatus() == 200) {
                    applys = activityAllApplysBean.getData();
                    initApplys();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityAllApplysBean.getMessage());
                }
            }
        }
    }

    // 获取全部评论人
    private class GetAllCommentsTask extends AsyncTask<Void, Void, ActivityAllCommentsBean> {
        @Override
        protected ActivityAllCommentsBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETALLACTIVITYCOMMENTS)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            "page", "0",
                            Constant.BASE_URL + Constant.GETALLACTIVITYCOMMENTS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("全部评论人", body);
                return FastJsonTools.getBean(body, ActivityAllCommentsBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityAllCommentsBean activityAllCommentsBean) {
            super.onPostExecute(activityAllCommentsBean);
            if (activityAllCommentsBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityAllCommentsBean.getStatus() == 200) {
                    comments = activityAllCommentsBean.getData().getComments();
                    initComments();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityAllCommentsBean.getMessage());
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
