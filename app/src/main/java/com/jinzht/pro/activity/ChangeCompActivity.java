package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

/**
 * 更改公司界面
 */
public class ChangeCompActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private TextView btnSave;// 保存
    private ImageView btnClean;// 清空
    private EditText edCompName;// 公司名输入框

    @Override
    protected int getResourcesId() {
        return R.layout.activity_change_comp;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText(getIntent().getStringExtra("TAG"));
        btnSave = (TextView) findViewById(R.id.btn_save);// 保存
        btnSave.setOnClickListener(this);
        btnClean = (ImageView) findViewById(R.id.btn_clean);// 清空
        btnClean.setOnClickListener(this);
        edCompName = (EditText) findViewById(R.id.ed_comp_name);// 公司名输入框
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_save:// 保存
                switch (getIntent().getStringExtra("TAG")) {
                    case "公司":
                        if (StringUtils.isBlank(edCompName.getText().toString())) {
                            SuperToastUtils.showSuperToast(mContext, 2, "请输入新的公司名称");
                        } else {
                            if (clickable) {
                                clickable = false;
                                ChangeCompany changeCompany = new ChangeCompany();
                                changeCompany.execute();
                            }
                        }
                        break;
                    case "职位":
                        if (StringUtils.isBlank(edCompName.getText().toString())) {
                            SuperToastUtils.showSuperToast(mContext, 2, "请输入新的职位名称");
                        } else {
                            if (clickable) {
                                clickable = false;
                                ChangePosition changePosition = new ChangePosition();
                                changePosition.execute();
                            }
                        }
                        break;
                }
                break;
            case R.id.btn_clean:// 清空
                edCompName.setText("");
                break;
        }
    }

    // 修改公司名
    private class ChangeCompany extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.endsWith(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CHANGECOMPANY)),
                            "name", edCompName.getText().toString(),
                            Constant.BASE_URL + Constant.CHANGECOMPANY,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("修改公司名", body);
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
                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("TAG", "changeCompany");
                    intent.putExtra("companyName", edCompName.getText().toString());
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    // 修改职位
    private class ChangePosition extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.endsWith(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CHANGEPOSITION)),
                            "position", edCompName.getText().toString(),
                            Constant.BASE_URL + Constant.CHANGEPOSITION,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("修改职位", body);
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
                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("TAG", "changePosition");
                    intent.putExtra("position", edCompName.getText().toString());
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
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
