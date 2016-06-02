package com.jinzht.pro1.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

import java.io.FileNotFoundException;
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

    private String photoPath = "";// 选择的头像地址
    private Bitmap bitmap = null;// 选择的头像
    private final String FILE_PATH = Environment.getExternalStorageDirectory() + "/" + "favicon.jpg";// 照相保存地址

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

        improveInfoIvUserimage.setImageResource(R.drawable.ic_launcher);
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
                SetUserTypeTask setUserTypeTask = new SetUserTypeTask();
                setUserTypeTask.execute();
                break;
        }
    }

    // 返回到登录页
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

                        } else {// 从相册选择
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, Constant.CHOOSE_PHOTO);
                        }
                    }
                });
        builder.create().show();
    }

    // 选择头像
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.CHOOSE_PHOTO && data != null) {// 从相册选择照片
            try {
                Uri chosePhoto = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(chosePhoto, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                photoPath = c.getString(columnIndex);
                c.close();
                Log.i("照片地址", photoPath);
                // 压缩图片
                bitmap = createThumbnail(photoPath, 1);
                // 保存图片
                FileOutputStream fos = new FileOutputStream(FILE_PATH);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Bitmap bitmap1 = BitmapFactory.decodeFile(FILE_PATH);
                improveInfoIvUserimage.setImageBitmap(bitmap1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // 压缩图片
    private Bitmap createThumbnail(String photoPath, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = i;
        return BitmapFactory.decodeFile(photoPath, options);
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
                break;
            case R.id.improve_info_rb_touziren:// 选择了投资人
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.VISIBLE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                break;
            case R.id.improve_info_rb_touzijigou:// 选择了投资机构
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.VISIBLE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                break;
            case R.id.improve_info_rb_zhinangtuan:// 选择了智囊团
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.VISIBLE);
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
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SETUSERTYPE)),
                            "ideniyType", "2",
//                            "file", "",
                            Constant.BASE_URL + Constant.SETUSERTYPE,
                            mContext
                    );
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
                    Intent intent = new Intent(mContext, CompleteRegisterActivity.class);
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

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
