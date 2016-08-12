package com.jinzht.pro.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.UploadProjectInfo;
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
 * 上传项目界面
 */
public class UploadActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回键
    private TextView tvTitle;// 标题
    private TextView uploadEmail;// 复制邮箱地址
    private TextView uploadTel;// 点击拨打电话

    private String tel;// 获取到的电话号码

    @Override
    protected int getResourcesId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回键
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("上传项目");
        uploadEmail = (TextView) findViewById(R.id.upload_email);// 复制邮箱地址
        uploadEmail.setOnClickListener(this);
        uploadEmail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        uploadTel = (TextView) findViewById(R.id.upload_tel);// 点击拨打电话
        uploadTel.setOnClickListener(this);

        GetUploadInfo getUploadInfo = new GetUploadInfo();
        getUploadInfo.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.upload_email:// 点击复制邮箱地址
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(uploadEmail.getText().toString());
                SuperToastUtils.showSuperToast(mContext, 2, "已复制到剪贴板");
                break;
            case R.id.upload_tel:// 点击拨打电话
                if (!StringUtils.isBlank(tel)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + tel);
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
        }
    }

    // 获取上传项目的邮箱和电话号码
    private class GetUploadInfo extends AsyncTask<Void, Void, UploadProjectInfo> {
        @Override
        protected UploadProjectInfo doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETUPLOADPROJECTINFO)),
                            Constant.BASE_URL + Constant.GETUPLOADPROJECTINFO,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("上传项目信息", body);
                return FastJsonTools.getBean(body, UploadProjectInfo.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(UploadProjectInfo uploadProjectInfo) {
            super.onPostExecute(uploadProjectInfo);
            if (uploadProjectInfo == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (uploadProjectInfo.getStatus() == 200) {
                    if (uploadProjectInfo.getData() != null) {
                        uploadEmail.setText(uploadProjectInfo.getData().getEmail());
//                    uploadTel.setText(uploadProjectInfo.getData().getTel());
                        tel = uploadProjectInfo.getData().getTel();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, uploadProjectInfo.getMessage());
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
