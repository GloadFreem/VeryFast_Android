package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.AuthenticateBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 实名认证上传营业执照界面
 */
public class CertificationCompActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回键
    private TextView tvTitle;// 标题
    private TextView tvHint;// 上传营业执照提示
    private RelativeLayout rlCompPhoto;// 营业执照照片
    private TextView tvCompPhoto;// 营业执照照片加号
    private EditText edCompnum;// 营业执照注册号
    private Button btnNext;// 下一步按钮

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体
    private Intent intent;
    private int usertype;// 1:项目方,2:投资人,3:机构投资人,4:智囊团

    private Bitmap bitmap = null;// 从相册选择的营业执照照片
    private File photoFile = null; // 拍照得到的营业执照照片
    private String photoPath;// 头像保存地址

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_comp;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回键
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvHint = (TextView) findViewById(R.id.tv_hint);// 上传营业执照提示
        str = UiUtils.getString(R.string.business_license_photo);
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(16, true), 6, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#474747")), 6, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvHint.setText(span);
        rlCompPhoto = (RelativeLayout) findViewById(R.id.rl_comp_photo);// 营业执照照片
        rlCompPhoto.setOnClickListener(this);
        tvCompPhoto = (TextView) findViewById(R.id.tv_comp_photo);// 营业执照照片加号
        edCompnum = (EditText) findViewById(R.id.ed_compnum);// 营业执照注册号
        btnNext = (Button) findViewById(R.id.btn_next);// 下一步按钮
        btnNext.setOnClickListener(this);

        usertype = getIntent().getIntExtra("usertype", 0);
        setMytitle();
        photoPath = getCacheDir() + "/" + "buinessLicence.jpg";// 头像保存地址
        photoFile = new File(Environment.getExternalStorageDirectory() + "/" + "buinessLicence.jpg");
    }

    // 根据身份类型不同而加载不同标题
    private void setMytitle() {
        // 项目方是2个步骤
        if (Constant.USERTYPE_XMF == usertype) {
            str = "实名认证(2/2)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
            btnNext.setText("完成");
            // 智囊团是3个步骤
        } else if (Constant.USERTYPE_ZNT == usertype) {
            str = "实名认证(2/3)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
            // 投资机构是4个步骤
        } else if (Constant.USERTYPE_TZJG == usertype) {
            str = "实名认证(2/4)";
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
            case R.id.rl_comp_photo:// 点击上传营业执照照片
                choosePhoto();
                break;
            case R.id.btn_next:// 跳转到下一步界面
                File file = new File(photoPath);
                if (!file.exists()) {
                    SuperToastUtils.showSuperToast(this, 2, "请上传营业执照照片");
                } else if (StringUtils.isBlank(edCompnum.getText().toString())) {
                    SuperToastUtils.showSuperToast(this, 2, "请填写营业执照注册号");
                } else {
                    if (usertype == Constant.USERTYPE_XMF) {// 项目方完成认证，弹出完成对话框
                        AuthenticateTask authenticateTask = new AuthenticateTask();
                        authenticateTask.execute();
                    } else {// 投资机构、智囊团跳转到个人介绍
                        intent = new Intent(this, CertificationDescActivity.class);
                        intent.putExtra("usertype", usertype);
                        intent.putExtra("identiyCarA", getIntent().getStringExtra("identiyCarA"));
                        intent.putExtra("identiyCarB", getIntent().getStringExtra("identiyCarB"));
                        intent.putExtra("identiyCarNo", getIntent().getStringExtra("identiyCarNo"));
                        intent.putExtra("name", getIntent().getStringExtra("name"));
                        intent.putExtra("companyName", getIntent().getStringExtra("companyName"));
                        intent.putExtra("cityId", getIntent().getStringExtra("cityId"));
                        intent.putExtra("position", getIntent().getStringExtra("position"));
                        intent.putExtra("areaId", getIntent().getStringExtra("areaId"));
                        intent.putExtra("buinessLicence", photoPath);
                        intent.putExtra("buinessLicenceNo", edCompnum.getText().toString());
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    // 选择营业执照
    private void choosePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择打开方式")
                .setItems(R.array.chose_photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {// 打开照相机
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(intent, Constant.TAKE_PHOTO);
                        } else {// 从相册选择
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 141);
                            intent.putExtra("aspectY", 100);
                            intent.putExtra("outputX", 1128);
                            intent.putExtra("outputY", 800);
                            intent.putExtra("scale", true);
                            intent.putExtra("return-data", false);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            intent.putExtra("noFaceDetection", true);
                            startActivityForResult(intent, Constant.CUT_PHOTO);
                        }
                    }
                });
        builder.create().show();
    }

    // 选择营业执照
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.TAKE_PHOTO) {// 拍照选择
            UiUtils.crop(Uri.fromFile(photoFile), 141, 100, 1128, 800, this, Constant.CUT_PHOTO);
        } else if (resultCode == RESULT_OK && requestCode == Constant.CUT_PHOTO) {// 剪裁过的照片
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(photoFile)));

                int byteCount0 = bitmap.getByteCount();
                String size0 = StringUtils.bytes2kb(byteCount0);
                Log.i("大小", size0);
                int width0 = bitmap.getWidth();
                int height0 = bitmap.getHeight();
                Log.i("分辨率", String.valueOf(width0) + "*" + String.valueOf(height0));

                FileOutputStream fos = new FileOutputStream(photoPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 压缩另存，100表示不压缩
                bitmap = BitmapFactory.decodeFile(photoPath);
                tvCompPhoto.setVisibility(View.GONE);
                rlCompPhoto.setBackgroundDrawable(new BitmapDrawable(bitmap));
                fos.close();

                int byteCount = bitmap.getByteCount();
                String size = StringUtils.bytes2kb(byteCount);
                Log.i("大小", size);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Log.i("分辨率", String.valueOf(width) + "*" + String.valueOf(height));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                            "companyName", getIntent().getStringExtra("companyName"),
                            "cityId", getIntent().getStringExtra("cityId"),
                            "position", getIntent().getStringExtra("position"),
                            "areaId", "",
                            "buinessLicence", photoPath,
                            "buinessLicenceNo", edCompnum.getText().toString(),
                            "introduce", "",
                            "companyIntroduce", "",
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
                    SharedPreferencesUtils.setAuth(mContext, true);
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
