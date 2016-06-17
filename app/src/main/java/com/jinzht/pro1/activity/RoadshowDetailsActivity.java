package com.jinzht.pro1.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.RoadshowFragmentAdaper;
import com.jinzht.pro1.base.BaseFragmentActivity;
import com.jinzht.pro1.bean.CustomerServiceBean;
import com.jinzht.pro1.bean.ProjectCollectBean;
import com.jinzht.pro1.bean.ProjectDetailBean;
import com.jinzht.pro1.bean.ShareBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.DialogUtils;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.ShareUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 路演项目详情
 */
public class RoadshowDetailsActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout btnCollect;// 收藏
    private ImageView ivCollect;// title右侧第二个按钮图标
    private LinearLayout btnShare;// title最右侧按钮，分享
    private ViewPager vpPPt;// 播放PPT区域
    private RadioGroup rgTab;// 详情、成员、现场总合tab
    private RadioButton rbtnDetail;// 详情按钮
    private RadioButton rbtnMember;// 成员按钮
    private RadioButton rbtnLive;// 现场按钮
    private LinearLayout llInvest;// 客服和投资按钮布局
    private ImageButton btnService;// 客服按钮
    private RelativeLayout btnInvest;// 认投按钮
    private WrapContentHeightViewPager vpModule;// 详情页的ViewPager
    private ScrollView sv;// ScrollView

    private ProjectDetailBean.DataBean.ProjectBean data;// 项目数据
    private List<ProjectDetailBean.DataBean.ExtrBean> reportDatas = new ArrayList<>();// 报表数据

    private int FLAG = 0;// 关注或取消关注的标识
    private int needRefresh = 0;// 是否需要在项目列表中刷新
    public final static int RESULT_CODE = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_details_roadshow;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        // 设置tab的单选事件
        rgTab.setOnCheckedChangeListener(this);
        // 给详情ViewPager填充数据
        vpModule.setAdapter(new RoadshowFragmentAdaper(getSupportFragmentManager()));
        vpModule.setCurrentItem(0);
        // 设置tab和ViewPager联动
        vpModule.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        rgTab.check(R.id.details_rbtn_detail);
                        llInvest.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        rgTab.check(R.id.details_rbtn_member);
                        llInvest.setVisibility(View.GONE);
                        break;
                    case 2:
                        rgTab.check(R.id.details_rbtn_live);
                        llInvest.setVisibility(View.GONE);
                        break;
                }
            }
        });

        GetDetailTask getDetailTask = new GetDetailTask();
        getDetailTask.execute();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.title_btn_left);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题，此页不显示
        tvTitle.setVisibility(View.GONE);
        btnCollect = (LinearLayout) findViewById(R.id.title_btn_right2);// 收藏
        btnCollect.setOnClickListener(this);
        ivCollect = (ImageView) findViewById(R.id.title_iv_right2);// title右侧第二个按钮图标
        btnShare = (LinearLayout) findViewById(R.id.title_btn_right);// title最右侧按钮，分享
        btnShare.setOnClickListener(this);
        vpPPt = (ViewPager) findViewById(R.id.details_roadshow_ppt);// 播放PPT区域
        rgTab = (RadioGroup) findViewById(R.id.details_rg_tab);// 详情、成员、现场总合tab
        rbtnDetail = (RadioButton) findViewById(R.id.details_rbtn_detail);// 详情按钮
        rbtnMember = (RadioButton) findViewById(R.id.details_rbtn_member);// 成员按钮
        rbtnLive = (RadioButton) findViewById(R.id.details_rbtn_live);// 现场按钮
        llInvest = (LinearLayout) findViewById(R.id.details_ll_invest);// 客服和投资按钮布局
        btnService = (ImageButton) findViewById(R.id.details_btn_service);// 客服按钮
        btnService.setOnClickListener(this);
        btnInvest = (RelativeLayout) findViewById(R.id.details_btn_invest);// 认投按钮
        btnInvest.setOnClickListener(this);
        vpModule = (WrapContentHeightViewPager) findViewById(R.id.details_vp_module);// 详情页的ViewPager
        sv = (ScrollView) findViewById(R.id.details_sl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 返回上一页
                onBackPressed();
                break;
            case R.id.title_btn_right2:// 收藏
                if (data.isCollected()) {
                    CollectTask collectTask = new CollectTask(2);
                    collectTask.execute();
                } else {
                    CollectTask collectTask = new CollectTask(1);
                    collectTask.execute();
                }
                break;
            case R.id.title_btn_right:// 分享
                ShareTask shareTask = new ShareTask();
                shareTask.execute();
                break;
            case R.id.details_btn_service:// 给客服打电话
                CustomerServiceTask customerServiceTask = new CustomerServiceTask();
                customerServiceTask.execute();
                break;
            case R.id.details_btn_invest:// 认投
                SuperToastUtils.showSuperToast(mContext, 2, "认投");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (needRefresh % 2 != 0 && FLAG != 0) {
            Intent intent = new Intent();
            intent.putExtra("FLAG", FLAG);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }

    // 设置tab的单选事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.details_rbtn_detail:// 选择了详情
                vpModule.setCurrentItem(0);
                rbtnDetail.setTextColor(UiUtils.getColor(R.color.custom_orange));
                rbtnMember.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnLive.setTextColor(UiUtils.getColor(R.color.bg_text));
                llInvest.setVisibility(View.VISIBLE);
                break;
            case R.id.details_rbtn_member:// 选择了成员
                vpModule.setCurrentItem(1);
                rbtnDetail.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnMember.setTextColor(UiUtils.getColor(R.color.custom_orange));
                rbtnLive.setTextColor(UiUtils.getColor(R.color.bg_text));
                llInvest.setVisibility(View.GONE);
                break;
            case R.id.details_rbtn_live:// 选择了现场
                vpModule.setCurrentItem(2);
                rbtnDetail.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnMember.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnLive.setTextColor(UiUtils.getColor(R.color.custom_orange));
                llInvest.setVisibility(View.GONE);
                break;
        }
    }

    // 关注处理
    private void initCollect() {
        if (data.isCollected()) {
            ivCollect.setBackgroundResource(R.mipmap.icon_collected);
        } else {
            ivCollect.setBackgroundResource(R.mipmap.icon_collect);
        }
    }

    // 获取详情
    private class GetDetailTask extends AsyncTask<Void, Void, ProjectDetailBean> {
        @Override
        protected ProjectDetailBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTDETAIL)),
                            "projectId", getIntent().getStringExtra("id"),
                            Constant.BASE_URL + Constant.GETPROJECTDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("预选项目详情", body);
                return FastJsonTools.getBean(body, ProjectDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectDetailBean projectDetailBean) {
            super.onPostExecute(projectDetailBean);
            if (projectDetailBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (projectDetailBean.getStatus() == 200) {
                    data = projectDetailBean.getData().getProject();
                    reportDatas = projectDetailBean.getData().getExtr();
                    if (data != null) {
                        initCollect();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, projectDetailBean.getMessage());
                }
            }
        }
    }

    // 关注
    private class CollectTask extends AsyncTask<Void, Void, ProjectCollectBean> {
        int flag;

        public CollectTask(int flag) {
            this.flag = flag;
        }

        @Override
        protected ProjectCollectBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COLLECTPROJECT)),
                            "projectId", String.valueOf(data.getProjectId()),
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.COLLECTPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("关注项目返回信息", body);
                return FastJsonTools.getBean(body, ProjectCollectBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectCollectBean projectCollectBean) {
            super.onPostExecute(projectCollectBean);
            if (projectCollectBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (projectCollectBean.getStatus() == 200) {
                    if (flag == 1) {
                        data.setCollected(true);
                        data.setCollectionCount(data.getCollectionCount() + 1);
                        FLAG = 1;
                    } else if (flag == 2) {
                        data.setCollected(false);
                        data.setCollectionCount(data.getCollectionCount() - 1);
                        FLAG = 2;
                    }
                    needRefresh++;
                    initCollect();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, projectCollectBean.getMessage());
                }
            }
        }
    }

    // 分享
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREPROJECT)),
                            "type", "1",
                            "projectId", String.valueOf(data.getProjectId()),
                            Constant.BASE_URL + Constant.SHAREPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("分享返回信息", body);
                return FastJsonTools.getBean(body, ShareBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ShareBean shareBean) {
            super.onPostExecute(shareBean);
            if (shareBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(RoadshowDetailsActivity.this);
                    DialogUtils.shareDialog(RoadshowDetailsActivity.this, btnShare, shareUtils, data.getAbbrevName(), data.getDescription(), data.getStartPageImage(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
                }
            }
        }
    }

    // 客服接口
    private class CustomerServiceTask extends AsyncTask<Void, Void, CustomerServiceBean> {
        @Override
        protected CustomerServiceBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CUSTOMERSERVICE)),
                            Constant.BASE_URL + Constant.CUSTOMERSERVICE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("客服信息", body);
                return FastJsonTools.getBean(body, CustomerServiceBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CustomerServiceBean customerServiceBean) {
            super.onPostExecute(customerServiceBean);
            if (customerServiceBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (customerServiceBean.getStatus() == 200) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + customerServiceBean.getData().getTel());
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, customerServiceBean.getMessage());
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
