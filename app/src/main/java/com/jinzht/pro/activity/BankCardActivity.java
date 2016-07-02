package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.YeepaySignBean;
import com.jinzht.pro.bean.YeepayUserInfoBean;
import com.jinzht.pro.bean.YibaoUnbindCallbackInfoBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.thoughtworks.xstream.XStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 资金账户中银行卡界面
 */
public class BankCardActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private TextView tvBankName;// 银行卡名
    private TextView tvBankNo;// 银行卡号
    private TextView tvHint;// 提示
    private TextView btnBind;// 绑卡or解绑

    private String[] bankNames;
    private final static int REQUEST_CODE = 1;

    private String request;// 易宝请求体
    private String sign;// 签名
    private YeepayUserInfoBean yeepayUserInfo;// 用户易宝账户信息

    private String unbindRequest;// 解绑请求
    private String unbindSign;// 解绑签名
    private YibaoUnbindCallbackInfoBean yibaoUnbindCallbackInfoBean;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_bank_card;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        bankNames = getResources().getStringArray(R.array.banks);
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("银行卡");
        tvBankName = (TextView) findViewById(R.id.tv_bank_name);// 银行名
        tvBankNo = (TextView) findViewById(R.id.tv_bank_no);// 银行卡号
        tvHint = (TextView) findViewById(R.id.tv_hint);// 提示
        btnBind = (TextView) findViewById(R.id.btn_bind);// 绑卡or解绑
        btnBind.setOnClickListener(this);

        initData();
    }

    private void initData() {
        if (StringUtils.isBlank(getIntent().getStringExtra("bankNo"))) {
            tvBankName.setText("未绑卡");
            tvBankNo.setText("您暂未绑定银行卡");
            btnBind.setText("绑定银行卡");
            tvHint.setVisibility(View.VISIBLE);
        } else {
            tvBankNo.setText(getIntent().getStringExtra("bankNo"));
            switch (getIntent().getStringExtra("bankName")) {
                case "BOCO":
                    tvBankName.setText(bankNames[0]);
                    break;
                case "CEB":
                    tvBankName.setText(bankNames[1]);
                    break;
                case "SPDB":
                    tvBankName.setText(bankNames[2]);
                    break;
                case "ABC":
                    tvBankName.setText(bankNames[3]);
                    break;
                case "ECITIC":
                    tvBankName.setText(bankNames[4]);
                    break;
                case "CCB":
                    tvBankName.setText(bankNames[5]);
                    break;
                case "CMBC":
                    tvBankName.setText(bankNames[6]);
                    break;
                case "SDB":
                    tvBankName.setText(bankNames[7]);
                    break;
                case "PSBC":
                    tvBankName.setText(bankNames[8]);
                    break;
                case "CMBCHINA":
                    tvBankName.setText(bankNames[9]);
                    break;
                case "CIB":
                    tvBankName.setText(bankNames[10]);
                    break;
                case "ICBC":
                    tvBankName.setText(bankNames[11]);
                    break;
                case "BOC":
                    tvBankName.setText(bankNames[12]);
                    break;
                case "BCCB":
                    tvBankName.setText(bankNames[13]);
                    break;
                case "GDB":
                    tvBankName.setText(bankNames[14]);
                    break;
                case "HX":
                    tvBankName.setText(bankNames[15]);
                    break;
                case "XAYH":
                    tvBankName.setText(bankNames[16]);
                    break;
                case "SHYH":
                    tvBankName.setText(bankNames[17]);
                    break;
                case "TJYH":
                    tvBankName.setText(bankNames[18]);
                    break;
                case "SZNCSYYH":
                    tvBankName.setText(bankNames[19]);
                    break;
                case "BJNCSYYH":
                    tvBankName.setText(bankNames[20]);
                    break;
                case "HZYH":
                    tvBankName.setText(bankNames[21]);
                    break;
                case "KLYH":
                    tvBankName.setText(bankNames[22]);
                    break;
                case "ZHENGZYH":
                    tvBankName.setText(bankNames[23]);
                    break;
                case "WZYH":
                    tvBankName.setText(bankNames[24]);
                    break;
                case "HKYH":
                    tvBankName.setText(bankNames[25]);
                    break;
                case "NJYH":
                    tvBankName.setText(bankNames[26]);
                    break;
                case "XMYH":
                    tvBankName.setText(bankNames[27]);
                    break;
                case "NCYH":
                    tvBankName.setText(bankNames[28]);
                    break;
                case "JISYH":
                    tvBankName.setText(bankNames[29]);
                    break;
                case "HKBEA":
                    tvBankName.setText(bankNames[30]);
                    break;
                case "CDYH":
                    tvBankName.setText(bankNames[31]);
                    break;
                case "NBYH":
                    tvBankName.setText(bankNames[32]);
                    break;
                case "CSYH":
                    tvBankName.setText(bankNames[33]);
                    break;
                case "HBYH":
                    tvBankName.setText(bankNames[34]);
                    break;
                case "GUAZYH":
                    tvBankName.setText(bankNames[35]);
                    break;
            }
            btnBind.setText("解绑银行卡");
            tvHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_bind:
                if ("绑定银行卡".equals(btnBind.getText())) {
                    // 去绑卡
                    Intent intent = new Intent(this, YeepayBindCardActivity.class);
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    // 解绑
                    confirmUnbind();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == YeepayBindCardActivity.RESULT_CODE && data.getBooleanExtra("needRefresh", false)) {
                // 绑卡成功，刷新
                GetSignTask getSignTask = new GetSignTask();
                getSignTask.execute();
            }
        }
    }

    // 获取易宝账户签名
    private class GetSignTask extends AsyncTask<Void, Void, YeepaySignBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            request = "<request platformNo=\"" + Constant.PLATFORMNO + "\"><platformUserNo>" + "jinzht_0000_" + getIntent().getStringExtra("userId") + "</platformUserNo></request>";
        }

        @Override
        protected YeepaySignBean doInBackground(Void... params) {
            String body = "";
            Log.i("request", request);
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SIGN)),
                            "method", "sign",
                            "req", request,
                            "sign", "",
                            "type", "1",
                            Constant.BASE_URL + Constant.SIGN,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("签名", body);
                return FastJsonTools.getBean(body, YeepaySignBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YeepaySignBean yeepaySignBean) {
            super.onPostExecute(yeepaySignBean);
            if (yeepaySignBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (yeepaySignBean.getStatus() == 200) {
                    sign = yeepaySignBean.getData().getSign();
                    GetYeepayAccount getYeepayAccount = new GetYeepayAccount();
                    getYeepayAccount.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, yeepaySignBean.getMessage());
                }
            }
        }
    }

    // 获取易宝账户信息
    private class GetYeepayAccount extends AsyncTask<Void, Void, YeepayUserInfoBean> {
        @Override
        protected YeepayUserInfoBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.yeepayPost(
                            "service", Constant.YEEPAY_ACCOUNTINFO,
                            "req", request,
                            "sign", sign,
                            Constant.YEEPAY_DIRECT
                    );
                    XStream xStream = new XStream();
                    xStream.processAnnotations(YeepayUserInfoBean.class);// 指定对应的Bean
                    yeepayUserInfo = (YeepayUserInfoBean) xStream.fromXML(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return yeepayUserInfo;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YeepayUserInfoBean bean) {
            super.onPostExecute(bean);
            if (bean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                Log.i("易宝账户信息", bean.toString());
                if ("1".equals(bean.getCode())) {
                    if (yeepayUserInfo != null) {
                        initDataForBind();
                    }
                } else {
                    // 提示用户出错
                    SuperToastUtils.showSuperToast(mContext, 2, bean.getDescription());
                }
            }
        }

    }

    // 绑卡后再次填充信息
    private void initDataForBind() {
        tvBankNo.setText(yeepayUserInfo.getCardNo());
        switch (yeepayUserInfo.getBank()) {
            case "BOCO":
                tvBankName.setText(bankNames[0]);
                break;
            case "CEB":
                tvBankName.setText(bankNames[1]);
                break;
            case "SPDB":
                tvBankName.setText(bankNames[2]);
                break;
            case "ABC":
                tvBankName.setText(bankNames[3]);
                break;
            case "ECITIC":
                tvBankName.setText(bankNames[4]);
                break;
            case "CCB":
                tvBankName.setText(bankNames[5]);
                break;
            case "CMBC":
                tvBankName.setText(bankNames[6]);
                break;
            case "SDB":
                tvBankName.setText(bankNames[7]);
                break;
            case "PSBC":
                tvBankName.setText(bankNames[8]);
                break;
            case "CMBCHINA":
                tvBankName.setText(bankNames[9]);
                break;
            case "CIB":
                tvBankName.setText(bankNames[10]);
                break;
            case "ICBC":
                tvBankName.setText(bankNames[11]);
                break;
            case "BOC":
                tvBankName.setText(bankNames[12]);
                break;
            case "BCCB":
                tvBankName.setText(bankNames[13]);
                break;
            case "GDB":
                tvBankName.setText(bankNames[14]);
                break;
            case "HX":
                tvBankName.setText(bankNames[15]);
                break;
            case "XAYH":
                tvBankName.setText(bankNames[16]);
                break;
            case "SHYH":
                tvBankName.setText(bankNames[17]);
                break;
            case "TJYH":
                tvBankName.setText(bankNames[18]);
                break;
            case "SZNCSYYH":
                tvBankName.setText(bankNames[19]);
                break;
            case "BJNCSYYH":
                tvBankName.setText(bankNames[20]);
                break;
            case "HZYH":
                tvBankName.setText(bankNames[21]);
                break;
            case "KLYH":
                tvBankName.setText(bankNames[22]);
                break;
            case "ZHENGZYH":
                tvBankName.setText(bankNames[23]);
                break;
            case "WZYH":
                tvBankName.setText(bankNames[24]);
                break;
            case "HKYH":
                tvBankName.setText(bankNames[25]);
                break;
            case "NJYH":
                tvBankName.setText(bankNames[26]);
                break;
            case "XMYH":
                tvBankName.setText(bankNames[27]);
                break;
            case "NCYH":
                tvBankName.setText(bankNames[28]);
                break;
            case "JISYH":
                tvBankName.setText(bankNames[29]);
                break;
            case "HKBEA":
                tvBankName.setText(bankNames[30]);
                break;
            case "CDYH":
                tvBankName.setText(bankNames[31]);
                break;
            case "NBYH":
                tvBankName.setText(bankNames[32]);
                break;
            case "CSYH":
                tvBankName.setText(bankNames[33]);
                break;
            case "HBYH":
                tvBankName.setText(bankNames[34]);
                break;
            case "GUAZYH":
                tvBankName.setText(bankNames[35]);
                break;
        }
        btnBind.setText("解绑银行卡");
        tvHint.setVisibility(View.GONE);
    }

    AlertDialog dialog;
    ImageView ivTag;
    TextView tvContent;
    TextView btnCancel;
    TextView btnConfirm;

    // 提示用户资金到账前不要解绑
    private void confirmUnbind() {
        dialog = new AlertDialog.Builder(this, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_two_btn);
        ivTag = (ImageView) window.findViewById(R.id.iv_tag);
        tvContent = (TextView) window.findViewById(R.id.tv_content);
        btnCancel = (TextView) window.findViewById(R.id.btn_cancel);
        btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        tvContent.setText("请确认账户中无余额再解绑，是否继续？");
        btnCancel.setText("取消");
        btnConfirm.setText("继续");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUnbindSignTask getUnbindSignTask = new GetUnbindSignTask();
                getUnbindSignTask.execute();
            }
        });
    }

    // 解绑的签名
    private class GetUnbindSignTask extends AsyncTask<Void, Void, YeepaySignBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            time = time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            String requestNo = time + getIntent().getStringExtra("userId");
            unbindRequest = "<request platformNo=\"" + Constant.PLATFORMNO + "\"><platformUserNo>" + "jinzht_0000_" + getIntent().getStringExtra("userId") + "</platformUserNo><requestNo>" + requestNo + "</requestNo></request>";
        }

        @Override
        protected YeepaySignBean doInBackground(Void... params) {
            String body = "";
            Log.i("unbindRequest", unbindRequest);
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SIGN)),
                            "method", "sign",
                            "req", unbindRequest,
                            "sign", "",
                            "type", "1",
                            Constant.BASE_URL + Constant.SIGN,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("签名", body);
                return FastJsonTools.getBean(body, YeepaySignBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YeepaySignBean yeepaySignBean) {
            super.onPostExecute(yeepaySignBean);
            if (yeepaySignBean == null) {
                dialog.dismiss();
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (yeepaySignBean.getStatus() == 200) {
                    unbindSign = yeepaySignBean.getData().getSign();
                    // 解绑
                    UnbindTask unbindTask = new UnbindTask();
                    unbindTask.execute();
                } else {
                    dialog.dismiss();
                    SuperToastUtils.showSuperToast(mContext, 2, yeepaySignBean.getMessage());
                }
            }
        }
    }

    // 解绑
    private class UnbindTask extends AsyncTask<Void, Void, YibaoUnbindCallbackInfoBean> {
        @Override
        protected YibaoUnbindCallbackInfoBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.yeepayPost(
                            "service", Constant.YEEPAY_UNBIND,
                            "req", unbindRequest,
                            "sign", unbindSign,
                            Constant.YEEPAY_DIRECT
                    );
                    XStream xStream = new XStream();
                    xStream.processAnnotations(YibaoUnbindCallbackInfoBean.class);// 指定对应的Bean
                    yibaoUnbindCallbackInfoBean = (YibaoUnbindCallbackInfoBean) xStream.fromXML(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return yibaoUnbindCallbackInfoBean;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YibaoUnbindCallbackInfoBean bean) {
            super.onPostExecute(bean);
            if (bean == null) {
                dialog.dismiss();
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                Log.i("解绑信息", bean.toString());
                if ("1".equals(bean.getCode())) {
                    ivTag.setImageResource(R.mipmap.icon_dialog_confirm);
                    tvContent.setText("解绑申请已提交，需两个工作日生效");
                } else {
                    tvContent.setText(bean.getDescription());
                }
                btnCancel.setVisibility(View.GONE);
                btnConfirm.setText("确定");
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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
