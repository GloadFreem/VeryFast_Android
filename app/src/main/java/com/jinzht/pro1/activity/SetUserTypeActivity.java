package com.jinzht.pro1.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.CommonBean;
import com.jinzht.pro1.bean.CustomerServiceBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SharedPreferencesUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.CircleImageView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 设置身份类型界面
 */
public class SetUserTypeActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageButton improveInfoBtBack;// 返回
    private ImageButton improveInfoBtContactService;// 联系客服
    private CircleImageView improveInfoIvUserimage;// 选择头像
    private TextView improveInfoTvSelectUsertype;// 选择用户类型提示，选择后消失
    private TextView tvDescXiangmufang;// 项目方描述
    private TextView tvDescTouziren;// 投资人描述
    private TextView tvDescTouzijigou;// 投资机构描述
    private TextView tvDescZhinangtuan;// 智囊团描述
    private RadioGroup improveInfoRgSelectUsertype;// 选择用户类型的RadioGroup
    private RadioButton improveInfoRbXiangmufang;// 项目方选项
    private RadioButton improveInfoRbTouziren;// 投资人选项
    private RadioButton improveInfoRbTouzijigou;// 投资机构选项
    private RadioButton improveInfoRbZhinangtuan;// 智囊团选项
    private Button improveInfoCompleteRegister;// 完成注册按钮

    private int usertype;// 1:项目方,2:投资人,3:机构投资人,4:智囊团
    private Bitmap bitmap = null;// 从相册选择的头像
    private File photoFile = null; // 拍照得到的头像
    private String photo_path;// 头像保存地址


    @Override
    protected int getResourcesId() {
        return R.layout.activity_set_usertype;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        improveInfoBtBack = (ImageButton) findViewById(R.id.improve_info_bt_back);// 返回
        improveInfoBtBack.setOnClickListener(this);
        improveInfoBtContactService = (ImageButton) findViewById(R.id.improve_info_bt_contact_service);// 联系客服
        improveInfoBtContactService.setOnClickListener(this);
        improveInfoIvUserimage = (CircleImageView) findViewById(R.id.improve_info_iv_userimage);// 选择头像
        improveInfoIvUserimage.setOnClickListener(this);
        improveInfoTvSelectUsertype = (TextView) findViewById(R.id.improve_info_tv_select_usertype);// 选择用户类型提示，选择后消失
        tvDescXiangmufang = (TextView) findViewById(R.id.tv_desc_xiangmufang);// 项目方描述
        tvDescTouziren = (TextView) findViewById(R.id.tv_desc_touziren);// 投资人描述
        tvDescTouzijigou = (TextView) findViewById(R.id.tv_desc_touzijigou);// 投资机构描述
        tvDescZhinangtuan = (TextView) findViewById(R.id.tv_desc_zhinangtuan);// 智囊团描述
        improveInfoRgSelectUsertype = (RadioGroup) findViewById(R.id.improve_info_rg_select_usertype);// 选择用户类型的RadioGroup
        improveInfoRgSelectUsertype.setOnCheckedChangeListener(this);
        improveInfoRbXiangmufang = (RadioButton) findViewById(R.id.improve_info_rb_xiangmufang);// 项目方选项
        improveInfoRbTouziren = (RadioButton) findViewById(R.id.improve_info_rb_touziren);// 投资人选项
        improveInfoRbTouzijigou = (RadioButton) findViewById(R.id.improve_info_rb_touzijigou);// 投资机构选项
        improveInfoRbZhinangtuan = (RadioButton) findViewById(R.id.improve_info_rb_zhinangtuan);// 智囊团选项
        improveInfoCompleteRegister = (Button) findViewById(R.id.improve_info_complete_register);// 完成注册按钮
        improveInfoCompleteRegister.setOnClickListener(this);

        photo_path = getCacheDir() + "/" + "favicon.jpg";// 头像保存地址
        photoFile = new File(Environment.getExternalStorageDirectory() + "/" + "favicon.jpg");

        if (!StringUtils.isBlank(SharedPreferencesUtils.getLocalFavicon(mContext))) {
            Glide.with(mContext).load(SharedPreferencesUtils.getLocalFavicon(mContext)).into(improveInfoIvUserimage);
        } else if (!StringUtils.isBlank(SharedPreferencesUtils.getOnlineFavicon(mContext))) {
            Glide.with(mContext).load(SharedPreferencesUtils.getOnlineFavicon(mContext)).into(improveInfoIvUserimage);
        } else {
            Glide.with(mContext).load(R.drawable.ic_launcher).into(improveInfoIvUserimage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.improve_info_bt_back:// 点击了返回
                onBackPressed();
                break;
            case R.id.improve_info_bt_contact_service:// 点击了联系客服
                CustomerServiceTask customerServiceTask = new CustomerServiceTask();
                customerServiceTask.execute();
                break;
            case R.id.improve_info_iv_userimage:// 点击了选择头像
                choosePhoto();
                break;
            case R.id.improve_info_complete_register:// 点击完成注册，跳转至获得邀请码界面
                if (usertype == 0) {
                    SuperToastUtils.showSuperToast(this, 2, "请选择您的身份");
                } else {
                    SetUserTypeTask setUserTypeTask = new SetUserTypeTask();
                    setUserTypeTask.execute();
                }
                break;
        }
    }

    // 返回到登录页
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    // 选择头像
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
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 300);
                            intent.putExtra("outputY", 300);
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

    // 选择头像
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.TAKE_PHOTO) {// 拍照选择
            UiUtils.crop(Uri.fromFile(photoFile), 1, 1, 300, 300, this, Constant.CUT_PHOTO);
        } else if (resultCode == RESULT_OK && requestCode == Constant.CUT_PHOTO) {// 剪裁过的照片
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(photoFile)));

                int byteCount0 = bitmap.getByteCount();
                String size0 = StringUtils.bytes2kb(byteCount0);
                Log.i("大小", size0);
                int width0 = bitmap.getWidth();
                int height0 = bitmap.getHeight();
                Log.i("分辨率", String.valueOf(width0) + "*" + String.valueOf(height0));

                FileOutputStream fos = new FileOutputStream(photo_path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 压缩另存，100表示不压缩
                bitmap = BitmapFactory.decodeFile(photo_path);
                improveInfoIvUserimage.setImageBitmap(bitmap);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        improveInfoTvSelectUsertype.setVisibility(View.GONE);
        switch (checkedId) {
            case R.id.improve_info_rb_xiangmufang:// 选择了项目方
                tvDescXiangmufang.setVisibility(View.VISIBLE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                usertype = 1;
                break;
            case R.id.improve_info_rb_touziren:// 选择了投资人
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.VISIBLE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                usertype = 2;
                break;
            case R.id.improve_info_rb_touzijigou:// 选择了投资机构
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.VISIBLE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                usertype = 3;
                break;
            case R.id.improve_info_rb_zhinangtuan:// 选择了智囊团
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.VISIBLE);
                usertype = 4;
                break;
        }
    }

    // 提交身份类型
    private class SetUserTypeTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    File file = new File(photo_path);
                    if (file.exists()) {
                        body = OkHttpUtils.usertypePost(
                                MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SETUSERTYPE)),
                                "ideniyType", String.valueOf(usertype),
                                "isWebchatLogin", "false",
                                "file", photo_path,
                                Constant.BASE_URL + Constant.SETUSERTYPE,
                                mContext
                        );
                    } else {
                        Log.i("没有选择头像", "没有选择头像");
                        body = OkHttpUtils.usertypePost(
                                MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SETUSERTYPE)),
                                "ideniyType", String.valueOf(usertype),
                                "isWebchatLogin", "false",
                                Constant.BASE_URL + Constant.SETUSERTYPE,
                                mContext
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("身份信息", body);
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
                return;
            } else {
                if (commonBean.getStatus() == 200) {
                    SharedPreferencesUtils.setChoseUserType(mContext, true);
                    SharedPreferencesUtils.saveUserType(mContext, usertype);
                    SharedPreferencesUtils.saveLocalFavicon(mContext, photo_path);
                    Intent intent = new Intent(mContext, CompleteRegisterActivity.class);// 跳转至完成完成注册送指环码界面
                    intent.putExtra("usertype", usertype);
                    startActivity(intent);
                    finish();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
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
