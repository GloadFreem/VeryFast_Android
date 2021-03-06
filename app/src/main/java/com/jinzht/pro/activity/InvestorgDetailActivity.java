package com.jinzht.pro.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.CircleImageView;

/**
 * 投资机构详情页
 */
public class InvestorgDetailActivity extends FullBaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private LinearLayout btnShare;// 分享
    private CircleImageView ivFavicon;// 头像
    private TextView tvName;// 姓名
    private TextView tvPosition;// 职位
    private TextView tvCompName;// 公司名称
    private TextView tvAddr;// 所在地
    private TextView tvField1;// 投资领域1
    private TextView tvField2;// 投资领域2
    private TextView tvField3;// 投资领域3
    private TextView tvDescs;// 机构介绍
    private TextView tvDesc;// 机构介绍
    private RelativeLayout btnSubmit;// 提交项目
    private RelativeLayout btnCollect;// 关注
    private TextView tvSubmit;// 提交
    private TextView tvCollect;// 关注

    private InvestorDetailBean.DataBean data;

    public final static int RESULT_CODE = 3;
    private int FLAG = 0;// 关注或取消关注的标识
    public int needRefresh = 0;// 是否进行了关注交互，返回时是否刷新

    @Override
    protected int getResourcesId() {
        return R.layout.activity_investor_detail;
    }

    @Override
    protected void init() {
//        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景
        btnBack = (LinearLayout) findViewById(R.id.title_btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnShare = (LinearLayout) findViewById(R.id.title_btn_share);// 分享
        btnShare.setOnClickListener(this);
        ivFavicon = (CircleImageView) findViewById(R.id.investor_detail_favicon);// 头像
        tvName = (TextView) findViewById(R.id.investor_detail_name);// 姓名
        tvPosition = (TextView) findViewById(R.id.investor_detail_position);// 职位
        tvCompName = (TextView) findViewById(R.id.investor_detail_comp_name);// 公司名称
        tvCompName.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvAddr = (TextView) findViewById(R.id.investor_detail_addr);// 所在地
        tvField1 = (TextView) findViewById(R.id.investor_detail_field1);// 投资领域1
        tvField2 = (TextView) findViewById(R.id.investor_detail_field2);// 投资领域2
        tvField3 = (TextView) findViewById(R.id.investor_detail_field3);// 投资领域3
        tvDescs = (TextView) findViewById(R.id.investor_detail_descs);// 机构介绍
        tvDescs.setText("机构 · 介绍");
        tvDesc = (TextView) findViewById(R.id.investor_detail_desc);// 机构介绍
        btnSubmit = (RelativeLayout) findViewById(R.id.investor_detail_btn_submit);// 提交项目
        btnSubmit.setOnClickListener(this);
        btnCollect = (RelativeLayout) findViewById(R.id.investor_detail_btn_collect);// 关注
        btnCollect.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.investor_detail_tv_submit);// 提交
        tvCollect = (TextView) findViewById(R.id.investor_detail_tv_collect);// 关注
        // 只有项目方才显示提交项目按钮
        if (SharedPreferencesUtils.getUserType(mContext) == Constant.USERTYPE_XMF) {
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnSubmit.setVisibility(View.GONE);
        }

        GetInvestorDetail getInvestorDetail = new GetInvestorDetail();
        getInvestorDetail.execute();
    }

    // 填充数据
    private void initData() {
        Glide.with(this)
                .load(data.getUser().getHeadSculpture())
//                .placeholder(R.mipmap.ic_default_favicon)
                .error(R.mipmap.ic_default_favicon)
                .into(ivFavicon);
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
        if (data.getAreas().size() == 1) {
            tvField1.setText(data.getAreas().get(0));
            tvField2.setVisibility(View.GONE);
            tvField3.setVisibility(View.GONE);
        } else if (data.getAreas().size() == 2) {
            tvField1.setText(data.getAreas().get(0));
            tvField2.setText(data.getAreas().get(1));
            tvField3.setVisibility(View.GONE);
        } else if (data.getAreas().size() == 3) {
            tvField1.setText(data.getAreas().get(0));
            tvField2.setText(data.getAreas().get(1));
            tvField3.setText(data.getAreas().get(2));
        }
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
        if (data.isCommited()) {
            btnSubmit.setBackgroundResource(R.drawable.bg_code_gray);
            btnSubmit.setClickable(false);
            tvSubmit.setText("已提交");
        } else {
            btnSubmit.setBackgroundResource(R.drawable.bg_code_orange);
            btnSubmit.setClickable(true);
            tvSubmit.setText("提交项目");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_btn_back:// 返回上一页
                onBackPressed();
                break;
            case R.id.title_btn_share:// 分享
                if (clickable) {
                    clickable = false;
                    ShareTask shareTask = new ShareTask();
                    shareTask.execute();
                }
                break;
            case R.id.investor_detail_btn_submit:// 提交项目
                Intent intent1 = new Intent(mContext, SubmitProjectActivity.class);
                intent1.putExtra("id", String.valueOf(data.getUser().getUserId()));
                intent1.putExtra("favicon", data.getUser().getHeadSculpture());
                intent1.putExtra("name", data.getUser().getName());
                intent1.putExtra("position", data.getUser().getAuthentics().get(0).getPosition());
                intent1.putExtra("compName", data.getUser().getAuthentics().get(0).getCompanyName());
                String province = data.getUser().getAuthentics().get(0).getCity().getProvince().getName();
                String city = data.getUser().getAuthentics().get(0).getCity().getName();
                if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
                    intent1.putExtra("addr", province);
                } else {
                    intent1.putExtra("addr", province + " | " + city);
                }
                startActivity(intent1);
                break;
            case R.id.investor_detail_btn_collect:// 关注
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
                Log.i("投资机构详情", body);
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

    // 关注投资人
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
                Log.i("关注投资人返回信息", body);
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

    // 分享投资机构
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREINVESTOR)),
                            "type", "6",
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
                    ShareUtils shareUtils = new ShareUtils(InvestorgDetailActivity.this);
                    DialogUtils.newShareDialog(InvestorgDetailActivity.this, shareUtils, data.getUser().getName(), data.getUser().getAuthentics().get(0).getCompanyIntroduce(), data.getUser().getHeadSculpture(), shareBean.getData().getUrl());
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
