package com.jinzht.pro1.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.AuthenticateBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SharePreferencesUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 实名认证自我介绍界面
 */
public class CertificationDescActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout llDescComp;// 公司介绍整体布局
    private EditText edDescComp;// 公司介绍输入框
    private EditText edDescPerson;// 个人介绍输入框
    private Button btnNext;// 下一步

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体
    private Intent intent;
    private int usertype;// 1:项目方,2:投资人,3:机构投资人,4:智囊团
    private String introduce = "";// 个人介绍
    private String companyIntroduce = "";// 公司介绍
    private String companyName = "";// 公司名
    private String position = "";// 职位

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_desc;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        llDescComp = (LinearLayout) findViewById(R.id.ll_desc_comp);// 公司介绍整体布局
        edDescComp = (EditText) findViewById(R.id.ed_desc_comp);// 公司介绍输入框
        edDescPerson = (EditText) findViewById(R.id.ed_desc_person);// 个人介绍输入框
        btnNext = (Button) findViewById(R.id.btn_next);// 下一步
        btnNext.setOnClickListener(this);

        usertype = getIntent().getIntExtra("usertype", 0);
        setMytitle();
    }

    // 根据身份类型不同而加载不同标题
    private void setMytitle() {
        // 投资人是3个步骤
        if (Constant.USERTYPE_TZR == usertype) {
            str = "实名认证(2/3)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
            llDescComp.setVisibility(View.GONE);
            // 智囊团3个步骤
        } else if (Constant.USERTYPE_ZNT == usertype) {
            str = "实名认证(3/3)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
            btnNext.setText("完成");
            // 投资机构是4个步骤
        } else if (Constant.USERTYPE_TZJG == usertype) {
            str = "实名认证(3/4)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_next:// 跳转到一下页
                introduce = edDescPerson.getText().toString();
                if (StringUtils.isBlank(introduce)) {
                    introduce = "";
                }
                companyIntroduce = edDescComp.getText().toString();
                if (StringUtils.isBlank(companyIntroduce)) {
                    companyIntroduce = "";
                }
                companyName = getIntent().getStringExtra("companyName");
                if (StringUtils.isBlank(companyName)) {
                    companyName = "";
                }
                position = getIntent().getStringExtra("position");
                if (StringUtils.isBlank(position)) {
                    position = "";
                }
                if (usertype == Constant.USERTYPE_ZNT) {// 智囊团完成认证
                    AuthenticateTask authenticateTask = new AuthenticateTask();
                    authenticateTask.execute();
                } else {// 投资人和投资机构跳转至选择投资能力
                    intent = new Intent(this, CertificationCapacityActivity.class);
                    intent.putExtra("usertype", usertype);
                    intent.putExtra("identiyCarA", getIntent().getStringExtra("identiyCarA"));
                    intent.putExtra("identiyCarB", getIntent().getStringExtra("identiyCarB"));
                    intent.putExtra("identiyCarNo", getIntent().getStringExtra("identiyCarNo"));
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("companyName", companyName);
                    intent.putExtra("cityId", getIntent().getStringExtra("cityId"));
                    intent.putExtra("position", position);
                    intent.putExtra("areaId", getIntent().getStringExtra("areaId"));
                    intent.putExtra("buinessLicence", getIntent().getStringExtra("buinessLicence"));
                    intent.putExtra("buinessLicenceNo", getIntent().getStringExtra("buinessLicenceNo"));
                    intent.putExtra("introduce", introduce);
                    intent.putExtra("companyIntroduce", companyIntroduce);
                    startActivity(intent);
                }
                break;
        }
    }

    // 上传认证信息
    private class AuthenticateTask extends AsyncTask<Void, Void, AuthenticateBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("");
        }

        @Override
        protected AuthenticateBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {

                    body = OkHttpUtils.authenticate(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.AUTHENTICATE)),
                            "identiyTypeId", String.valueOf(usertype),
                            "identiyCarA", getIntent().getStringExtra("identiyCarA"),
                            "identiyCarB", getIntent().getStringExtra("identiyCarB"),
                            "identiyCarNo", getIntent().getStringExtra("identiyCarNo"),
                            "name", getIntent().getStringExtra("name"),
                            "companyName", companyName,
                            "cityId", getIntent().getStringExtra("cityId"),
                            "position", position,
                            "areaId", getIntent().getStringExtra("areaId"),
                            "buinessLicence", getIntent().getStringExtra("buinessLicence"),
                            "buinessLicenceNo", getIntent().getStringExtra("buinessLicenceNo"),
                            "introduce", introduce,
                            "companyIntroduce", companyIntroduce,
                            "optional", "",
                            Constant.BASE_URL + Constant.AUTHENTICATE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("认证信息", body);
                return FastJsonTools.getBean(body, AuthenticateBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(AuthenticateBean authenticateBean) {
            super.onPostExecute(authenticateBean);
            dismissProgressDialog();
            if (authenticateBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (authenticateBean.getStatus() == 200) {
                    SharePreferencesUtils.setAuth(mContext, true);
                    // TODO: 2016/6/6 弹出认证成功提示
                    SuperToastUtils.showSuperToast(mContext, 2, authenticateBean.getMessage());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, authenticateBean.getMessage());
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
