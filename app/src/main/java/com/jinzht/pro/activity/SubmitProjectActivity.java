package com.jinzht.pro.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.CustomerServiceBean;
import com.jinzht.pro.bean.ProCenter4ProBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.RoundProgressBar;

/**
 * 向投资人提交项目页面
 */
public class SubmitProjectActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView title;// 本页标题
    private LinearLayout btnService;// 联系客服
    private ImageView ivService;// 联系客服图标
    private CircleImageView ivfavicon;// 投资人头像
    private TextView tvName;// 投资人姓名
    private TextView tvPosition;// 投资人职位
    private TextView tvCompName;// 投资人公司名
    private TextView tvAddr;// 投资人所在地
    private TextView tvRecommendReason;// 推荐理由四个字
    private EditText edRecommend;// 推荐理由输入框
    private RelativeLayout rlRecommend;// 推荐理由输入框背景
    private CircleImageView ivProLogo;// 项目logo
    private TextView tvProTitle;// 项目标题
    private ImageView ivCompletedTag;// 项目状态
    private TextView tvProName;// 项目名
    private TextView tvProDesc;// 项目描述
    private TextView tvPopularity;// 人气指数
    private TextView tvRemainingTime;// 剩余时间
    private TextView tvFinancingAmount;// 融资金额
    private RoundProgressBar submitFinancingProgress;// 圆形进度条
    private TextView btnSubmit;// 确认按钮

    private ProCenter4ProBean.DataBean proData;// 项目数据
    private boolean progressStop = false;// 圆形进度条正在滑动的标识
    private ProThread progressThread;// 圆形进度条的线程
    private int proTotal = 0;// 要显示的全部进度 = 已融资/需融资*100
    private int progress = 0;// 当前进度

    @Override
    protected int getResourcesId() {
        return R.layout.activity_submit_project;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.title_btn_left);// 返回
        btnBack.setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_title);// 本页标题
        title.setText("提交项目");
        btnService = (LinearLayout) findViewById(R.id.title_btn_right);// 联系客服
        btnService.setOnClickListener(this);
        ivService = (ImageView) findViewById(R.id.title_iv_right);// 联系客服图标
        ivService.setBackgroundResource(R.mipmap.iconfont_kefu);
        ivfavicon = (CircleImageView) findViewById(R.id.submit_iv_favicon);// 投资人头像
        tvName = (TextView) findViewById(R.id.submit_tv_name);// 投资人姓名
        tvPosition = (TextView) findViewById(R.id.submit_tv_position);// 投资人职位
        tvCompName = (TextView) findViewById(R.id.submit_tv_comp_name);// 投资人公司名
        tvAddr = (TextView) findViewById(R.id.submit_tv_addr);// 投资人所在地
        tvRecommendReason = (TextView) findViewById(R.id.submit_tv_recommend_reason);// 推荐理由四个字
        // 将推荐理由的字数限制字体变小
        SpannableString span = new SpannableString("推荐理由 (20～300字)");
        span.setSpan(new AbsoluteSizeSpan(12, true), 5, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRecommendReason.setText(span);
        edRecommend = (EditText) findViewById(R.id.submit_et_recommend);// 推荐理由输入框
        rlRecommend = (RelativeLayout) findViewById(R.id.submit_rl_recommend);// 推荐理由输入框背景
        rlRecommend.setOnClickListener(this);
        ivProLogo = (CircleImageView) findViewById(R.id.submit_iv_pro_img);// 项目logo
        tvProTitle = (TextView) findViewById(R.id.submit_tv_pro_title);// 项目标题
        ivCompletedTag = (ImageView) findViewById(R.id.iv_tag_completed);// 项目状态
        tvProName = (TextView) findViewById(R.id.submit_tv_pro_name);// 项目名
        tvProDesc = (TextView) findViewById(R.id.submit_tv_pro_desc);// 项目描述
        tvPopularity = (TextView) findViewById(R.id.submit_popularity);// 人气指数
        tvRemainingTime = (TextView) findViewById(R.id.submit_remaining_time);// 剩余时间
        tvFinancingAmount = (TextView) findViewById(R.id.submit_financing_amount);// 融资金额
        submitFinancingProgress = (RoundProgressBar) findViewById(R.id.submit_financing_progress);// 圆形进度条
        btnSubmit = (TextView) findViewById(R.id.submit_btn_confirm);// 确认按钮
        btnSubmit.setOnClickListener(this);

        Glide.with(this).load(getIntent().getStringExtra("favicon")).into(ivfavicon);
        tvName.setText(getIntent().getStringExtra("name"));
        tvPosition.setText(getIntent().getStringExtra("position"));
        tvCompName.setText(getIntent().getStringExtra("compName"));
        tvAddr.setText(getIntent().getStringExtra("addr"));

        GetProjectTask getProjectTask = new GetProjectTask();
        getProjectTask.execute();
    }

    private void initData() {
        Glide.with(this).load(proData.getStartPageImage()).into(ivProLogo);
        tvProTitle.setText(proData.getAbbrevName());
        if ("融资成功".equals(proData.getFinancestatus().getName())) {
            ivCompletedTag.setVisibility(View.VISIBLE);
        } else {
            ivCompletedTag.setVisibility(View.INVISIBLE);
        }
        tvProName.setText(proData.getFullName());
        tvProDesc.setText(proData.getDescription());
        tvPopularity.setText(String.valueOf(proData.getCollectionCount()));
        tvRemainingTime.setText(String.valueOf(proData.getTimeLeft()));
        tvFinancingAmount.setText(proData.getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");

        proTotal = (int) ((double) (proData.getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (proData.getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
        progressThread = new ProThread();
        progressThread.start();
    }

    // 项目圆形进度条的线程
    private class ProThread extends Thread {
        @Override
        public void run() {
            while (!progressStop) {
                try {
                    progress += 1;
                    Message msg = new Message();
                    msg.obj = progress;
                    Thread.sleep(50);
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopThread() {
            progressStop = true;
        }
    }

    // 控制项目圆形进度条，更新UI
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int temp = (int) msg.obj;
            if (proTotal - temp > 0) {
                submitFinancingProgress.setProgress(temp);
            } else {
                submitFinancingProgress.setProgress(proTotal);
                progressThread.stopThread();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 返回
                finish();
                break;
            case R.id.title_btn_right:// 联系客服
                if (clickable) {
                    clickable = false;
                    CustomerServiceTask customerServiceTask = new CustomerServiceTask();
                    customerServiceTask.execute();
                }
                break;
            case R.id.submit_rl_recommend:// 点击此区域弹出键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edRecommend, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.submit_btn_confirm:// 确认提交
                if (StringUtils.isBlank(edRecommend.getText().toString()) || StringUtils.length(edRecommend.getText().toString()) < 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入20字以上的推荐理由");
                } else {
                    if (clickable) {
                        clickable = false;
                        SubmitProjectTask submitProjectTask = new SubmitProjectTask(edRecommend.getText().toString());
                        submitProjectTask.execute();
                    }
                }
                break;
        }
    }

    // 获取项目数据
    private class GetProjectTask extends AsyncTask<Void, Void, ProCenter4ProBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected ProCenter4ProBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROCENTERLIST)),
                            "type", "0",
                            "page", "0",
                            Constant.BASE_URL + Constant.GETPROCENTERLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("项目信息", body);
                return FastJsonTools.getBean(body, ProCenter4ProBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProCenter4ProBean proCenter4ProBean) {
            super.onPostExecute(proCenter4ProBean);
            dismissProgressDialog();
            if (proCenter4ProBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (proCenter4ProBean.getStatus() == 200) {
                    if (proCenter4ProBean.getData() != null) {
                        if (proCenter4ProBean.getData().size() == 0) {
                            confirmDialog();
                        } else {
                            proData = proCenter4ProBean.getData().get(0);
                            if (proData != null) {
                                initData();
                            }
                        }
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, proCenter4ProBean.getMessage());
                }
            }
        }
    }

    // 提交项目
    private class SubmitProjectTask extends AsyncTask<Void, Void, CommonBean> {
        private String content;

        public SubmitProjectTask(String content) {
            this.content = content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SUBMITPROJECT)),
                            "projectId", String.valueOf(proData.getProjectId()),
                            "content", content,
                            "userId", getIntent().getStringExtra("id"),
                            Constant.BASE_URL + Constant.SUBMITPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("提交项目放回信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            clickable = true;
            dismissProgressDialog();
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    Intent intent = new Intent(mContext, ProCenterActivity.class);
                    intent.putExtra("usertype", 1);
                    startActivity(intent);
                    finish();
                }
                SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
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
            clickable = true;
            if (customerServiceBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
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

    // 弹窗确认
    private void confirmDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_confirm);
        ImageView ivTag = (ImageView) window.findViewById(R.id.iv_tag);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        ivTag.setImageResource(R.mipmap.icon_exclamation);
        tvContent.setText("您还没有上传项目\n请先上传项目");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressThread != null) {// 停止进度条
            progressThread.stopThread();
        }
    }
}
