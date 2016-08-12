package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.FullBaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.InvestorDetailBean;
import com.jinzht.pro.bean.ShareBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.ShareUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.CircleImageView;

/**
 * 智囊团详情页
 */
public class BrainDetailActivity extends FullBaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private LinearLayout btnShare;// 分享
    private CircleImageView ivFavicon;// 头像
    private TextView tvName;// 姓名
    private TextView tvPosition;// 职位
    private TextView tvCompName;// 公司名
    private TextView tvAddr;// 所在地
    private TextView tvService;// 服务领域
    private TextView tvDesc;// 个人简介
    private LinearLayout btnCollect;// 关注
    private TextView tvCollect;// 关注文字

    private InvestorDetailBean.DataBean data;

    public final static int RESULT_CODE = 4;
    private int FLAG = 0;// 关注或取消关注的标识
    public int needRefresh = 0;// 是否进行了关注交互，返回时是否刷新

    @Override
    protected int getResourcesId() {
        return R.layout.activity_brain_detail;
    }

    @Override
    protected void init() {
        btnBack = (LinearLayout) findViewById(R.id.title_btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnShare = (LinearLayout) findViewById(R.id.title_btn_share);// 分享
        btnShare.setOnClickListener(this);
        ivFavicon = (CircleImageView) findViewById(R.id.brain_detail_favicon);// 头像
        tvName = (TextView) findViewById(R.id.brain_detail_name);// 姓名
        tvPosition = (TextView) findViewById(R.id.brain_detail_position);// 职位
        tvCompName = (TextView) findViewById(R.id.brain_detail_comp_name);// 公司名
        tvAddr = (TextView) findViewById(R.id.brain_detail_addr);// 所在地
        tvService = (TextView) findViewById(R.id.brain_detail_service);// 服务领域
        tvDesc = (TextView) findViewById(R.id.brain_detail_desc);// 个人简介
        btnCollect = (LinearLayout) findViewById(R.id.brain_btn_collect);// 关注
        btnCollect.setOnClickListener(this);
        tvCollect = (TextView) findViewById(R.id.brain_tv_collect);// 关注文字

        GetInvestorDetail getInvestorDetail = new GetInvestorDetail();
        getInvestorDetail.execute();
    }

    // 填充数据
    private void initData() {
        Glide.with(this).load(data.getUser().getHeadSculpture()).into(ivFavicon);
        tvName.setText(data.getUser().getName());
        tvPosition.setText(data.getUser().getAuthentics().get(0).getPosition());
        tvCompName.setText(data.getUser().getAuthentics().get(0).getCompanyName());
        String province = data.getUser().getAuthentics().get(0).getCity().getProvince().getName();
        String city = data.getUser().getAuthentics().get(0).getCity().getName();
        if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
            tvAddr.setText(province);
        } else {
            tvAddr.setText(province + " | " + city);
        }
        tvService.setText(data.getUser().getAuthentics().get(0).getIntroduce());
        tvDesc.setText(data.getUser().getAuthentics().get(0).getCompanyIntroduce());
        if (data.isCollected()) {
            btnCollect.setBackgroundResource(R.drawable.bg_code_gray);
            tvCollect.setText("已关注");
        } else {
            btnCollect.setBackgroundResource(R.drawable.bg_btn_green);
            if (data.getCollectCount() >= 1000) {
                tvCollect.setText("关注(999...)");
            } else {
                tvCollect.setText("关注(" + data.getCollectCount() + ")");
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_btn_back:// 返回
                onBackPressed();
                break;
            case R.id.title_btn_share:// 分享
                if (clickable) {
                    clickable = false;
                    ShareTask shareTask = new ShareTask();
                    shareTask.execute();
                }
                break;
            case R.id.brain_btn_collect:// 关注
                if (clickable) {
                    clickable = false;
                    if (data.isCollected()) {
                        CollectInvestorTask collectInvestorTask = new CollectInvestorTask(2);
                        collectInvestorTask.execute();
                    } else {
                        CollectInvestorTask collectInvestorTask = new CollectInvestorTask(1);
                        collectInvestorTask.execute();
                    }
                }
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

    // 获取投资人详情
    private class GetInvestorDetail extends AsyncTask<Void, Void, InvestorDetailBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected InvestorDetailBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETINVESTORDETAIL)),
                            "investorId", getIntent().getStringExtra("id"),
                            Constant.BASE_URL + Constant.GETINVESTORDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("智囊团详情", body);
                return FastJsonTools.getBean(body, InvestorDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(InvestorDetailBean investorDetailBean) {
            super.onPostExecute(investorDetailBean);
            dismissProgressDialog();
            if (investorDetailBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (investorDetailBean.getStatus() == 200) {
                    data = investorDetailBean.getData();
                    if (data != null) {
                        initData();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, investorDetailBean.getMessage());
                }
            }
        }
    }

    // 关注智囊团
    private class CollectInvestorTask extends AsyncTask<Void, Void, CommonBean> {
        int flag;

        public CollectInvestorTask(int flag) {
            this.flag = flag;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COLLECTINVESTOR)),
                            "userId", String.valueOf(data.getUser().getUserId()),
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.COLLECTINVESTOR,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("关注智囊团返回信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            clickable = true;
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    if (flag == 1) {
                        data.setCollected(true);
                        data.setCollectCount(data.getCollectCount() + 1);
                        FLAG = 1;
                    } else if (flag == 2) {
                        data.setCollected(false);
                        data.setCollectCount(data.getCollectCount() - 1);
                        FLAG = 2;
                    }
                    needRefresh++;
                    initData();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    // 分享智囊团
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREINVESTOR)),
                            "type", "7",
                            "investorId", String.valueOf(data.getUser().getUserId()),
                            Constant.BASE_URL + Constant.SHAREINVESTOR,
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
            clickable = true;
            if (shareBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(BrainDetailActivity.this);
                    DialogUtils.newShareDialog(BrainDetailActivity.this, shareUtils, data.getUser().getName(), data.getUser().getAuthentics().get(0).getCompanyIntroduce(), data.getUser().getHeadSculpture(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
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
