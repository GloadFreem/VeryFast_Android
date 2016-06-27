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
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.CircleImageView;

/**
 * 投资人详情页
 */
public class InvestorDetailActivity extends FullBaseActivity implements View.OnClickListener {

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
    private TextView tvDesc;// 个人简介
    private RelativeLayout btnSubmit;// 提交项目
    private RelativeLayout btnCollect;// 关注
    private TextView tvSubmit;// 提交
    private TextView tvCollect;// 关注

    private InvestorDetailBean.DataBean data;

    private int FLAG = 0;// 关注或取消关注的标识
    public int needRefresh = 0;// 是否进行了关注交互，返回时是否刷新
    public final static int RESULT_CODE = 2;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_investor_detail;
    }

    @Override
    protected void init() {
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
        tvDesc = (TextView) findViewById(R.id.investor_detail_desc);// 个人简介
        btnSubmit = (RelativeLayout) findViewById(R.id.investor_detail_btn_submit);// 提交项目
        btnSubmit.setOnClickListener(this);
        btnCollect = (RelativeLayout) findViewById(R.id.investor_detail_btn_collect);// 关注
        btnCollect.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.investor_detail_tv_submit);// 提交
        tvCollect = (TextView) findViewById(R.id.investor_detail_tv_collect);// 关注

        GetInvestorDetail getInvestorDetail = new GetInvestorDetail();
        getInvestorDetail.execute();
    }

    // 填充数据
    private void initData() {
        Glide.with(this).load(data.getUser().getHeadSculpture()).into(ivFavicon);
        tvName.setText(data.getUser().getName());
        tvPosition.setText(data.getUser().getAuthentics().get(0).getPosition());
        tvCompName.setText(data.getUser().getAuthentics().get(0).getCompanyName());
        tvAddr.setText(data.getUser().getAuthentics().get(0).getCity().getProvince().getName() + " | " + data.getUser().getAuthentics().get(0).getCity().getName());
        if (data.getAreas().size() == 0) {
            tvField1.setVisibility(View.GONE);
            tvField2.setVisibility(View.GONE);
            tvField3.setVisibility(View.GONE);
        } else if (data.getAreas().size() == 1) {
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
        tvDesc.setText(data.getUser().getAuthentics().get(0).getIntroduce());
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
        switch (v.getId()) {
            case R.id.title_btn_back:// 返回上一页
                onBackPressed();
                break;
            case R.id.title_btn_share:// 分享
                ShareTask shareTask = new ShareTask();
                shareTask.execute();
                break;
            case R.id.investor_detail_btn_submit:// 提交项目
                Intent intent = new Intent(mContext, SubmitProjectActivity.class);
                intent.putExtra("id", String.valueOf(data.getUser().getUserId()));
                intent.putExtra("favicon", data.getUser().getHeadSculpture());
                intent.putExtra("name", data.getUser().getName());
                intent.putExtra("position", data.getUser().getAuthentics().get(0).getPosition());
                intent.putExtra("compName", data.getUser().getAuthentics().get(0).getCompanyName());
                intent.putExtra("addr", data.getUser().getAuthentics().get(0).getCity().getProvince().getName() + " | " + data.getUser().getAuthentics().get(0).getCity().getName());
                startActivity(intent);
                break;
            case R.id.investor_detail_btn_collect:// 关注
                if (data.isCollected()) {
                    CollectInvestorTask collectInvestorTask = new CollectInvestorTask(2);
                    collectInvestorTask.execute();
                } else {
                    CollectInvestorTask collectInvestorTask = new CollectInvestorTask(1);
                    collectInvestorTask.execute();
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
                Log.i("投资人详情", body);
                return FastJsonTools.getBean(body, InvestorDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(InvestorDetailBean investorDetailBean) {
            super.onPostExecute(investorDetailBean);
            if (investorDetailBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
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
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
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

    // 分享
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREINVESTOR)),
                            "type", "5",
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
            if (shareBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(InvestorDetailActivity.this);
                    DialogUtils.shareDialog(InvestorDetailActivity.this, btnShare, shareUtils, data.getUser().getName(), data.getUser().getAuthentics().get(0).getIntroduce(), data.getUser().getHeadSculpture(), shareBean.getData().getUrl());
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
