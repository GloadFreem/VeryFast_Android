package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.InviteCodeBean;
import com.jinzht.pro.bean.UserInfoBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;
import com.jinzht.pro.view.CircleImageView;

import java.io.File;
import java.io.FileOutputStream;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 我的资料界面
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RelativeLayout btnFavicon;// 头像按钮
    private CircleImageView ivFavicon;// 头像图片
    private RelativeLayout btnInvite;// 指环码按钮
    private TextView tvInvite;// 指环码
    private TextView tvTag;// 认证状态
    private TextView tvName;// 姓名
    private RelativeLayout btnType;// 平台身份按钮
    private TextView tvType;// 平台身份
    private RelativeLayout btnIdcard;// 身份证号按钮
    private TextView tvIdcard;// 身份证号
    private RelativeLayout btnComp;// 公司按钮
    private TextView tvComp;// 公司名称
    private ImageView ivArrowComp;// 公司箭头
    private RelativeLayout btnPosition;// 职位按钮
    private TextView tvPosition;// 职位名
    private ImageView ivArrowPosition;// 职位箭头
    private RelativeLayout btnAddr;// 所在地按钮
    private TextView tvAddr;// 所在地
    private ImageView ivArrowAddr;// 所在地箭头
    private TextView tvRemind;// 提示
    private TextView btnBottom;// 底部按钮

    private Intent intent = new Intent();

    private UserInfoBean.DataBean data;// 个人信息
    private String inviteCode = "";
    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体
    public final static int RESULT_CODE = 0;
    public boolean needRefresh = false;// 是否进行了交互，返回时是否刷新

    private Bitmap bitmap = null;// 从相册选择的头像
    private File photoFile = null; // 拍照得到的头像
    private String photo_path;// 头像保存地址

    @Override
    protected int getResourcesId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();

        if (!StringUtils.isBlank(SharedPreferencesUtils.getLocalFavicon(mContext))) {
            Glide.with(mContext)
                    .load(SharedPreferencesUtils.getLocalFavicon(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
//                    .placeholder(R.mipmap.ic_default_favicon)
                    .error(R.mipmap.ic_default_favicon)
                    .into(ivFavicon);
        } else if (!StringUtils.isBlank(SharedPreferencesUtils.getOnlineFavicon(mContext))) {
            Glide.with(mContext)
                    .load(SharedPreferencesUtils.getOnlineFavicon(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
//                    .placeholder(R.mipmap.ic_default_favicon)
                    .error(R.mipmap.ic_default_favicon)
                    .into(ivFavicon);
        } else {
            ivFavicon.setImageResource(R.drawable.ic_launcher);
        }

        photo_path = getCacheDir() + "/" + "favicon.jpg";// 头像保存地址
        photoFile = new File(Environment.getExternalStorageDirectory() + "/" + "favicon.jpg");
        EventBus.getDefault().register(this);
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("我的资料");
        btnFavicon = (RelativeLayout) findViewById(R.id.myinfo_btn_favicon);// 头像按钮
        btnFavicon.setOnClickListener(this);
        ivFavicon = (CircleImageView) findViewById(R.id.myinfo_iv_favicon);// 头像图片
        btnInvite = (RelativeLayout) findViewById(R.id.myinfo_btn_invite);// 指环码按钮
        btnInvite.setOnClickListener(this);
        tvInvite = (TextView) findViewById(R.id.myinfo_tv_invite);// 指环码
        tvTag = (TextView) findViewById(R.id.myinfo_tv_certification_tag);// 认证状态
        tvName = (TextView) findViewById(R.id.myinfo_tv_name);// 姓名
        btnType = (RelativeLayout) findViewById(R.id.myinfo_btn_type);// 平台身份按钮
        btnType.setOnClickListener(this);
        tvType = (TextView) findViewById(R.id.myinfo_tv_type);// 平台身份
        btnIdcard = (RelativeLayout) findViewById(R.id.myinfo_rl_idcard);// 身份证按钮
        btnIdcard.setOnClickListener(this);
        tvIdcard = (TextView) findViewById(R.id.myinfo_tv_idcard);// 身份证号
        btnComp = (RelativeLayout) findViewById(R.id.myinfo_btn_comp);// 公司按钮
        btnComp.setOnClickListener(this);
        tvComp = (TextView) findViewById(R.id.myinfo_tv_comp);// 公司名称
        ivArrowComp = (ImageView) findViewById(R.id.myinfo_arrow_comp);// 公司箭头
        btnPosition = (RelativeLayout) findViewById(R.id.myinfo_btn_position);// 职位按钮
        btnPosition.setOnClickListener(this);
        tvPosition = (TextView) findViewById(R.id.myinfo_tv_position);// 职位名
        ivArrowPosition = (ImageView) findViewById(R.id.myinfo_arrow_position);// 职位箭头
        btnAddr = (RelativeLayout) findViewById(R.id.myinfo_btn_addr);// 所在地按钮
        btnAddr.setOnClickListener(this);
        tvAddr = (TextView) findViewById(R.id.myinfo_tv_addr);// 所在地
        ivArrowAddr = (ImageView) findViewById(R.id.myinfo_arrow_addr);// 所在地箭头
        tvRemind = (TextView) findViewById(R.id.myinfo_tv_remind);// 提示
        btnBottom = (TextView) findViewById(R.id.myinfo_btn_bottom);// 底部按钮
        btnBottom.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getUserInfo(UserInfoBean.DataBean bean) {
        data = bean;
        if (data != null) {
            initData();
        }
    }

    private void initData() {
//        if (!StringUtils.isBlank(data.getHeadSculpture())) {
//            Glide.with(mContext).load(data.getHeadSculpture()).into(ivFavicon);
//        } else {
//            ivFavicon.setImageResource(R.drawable.ic_launcher);
//        }
        GetInviteCode getInviteCode = new GetInviteCode();
        getInviteCode.execute();
        tvType.setText(data.getAuthentics().get(0).getIdentiytype().getName());
        Log.i("认证状态", data.getAuthentics().get(0).getAuthenticstatus().getName());
        switch (data.getAuthentics().get(0).getAuthenticstatus().getName()) {
            case "未认证":
                tvTag.setText("实名认证信息(未认证)");
                str = "未认证";
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(Color.RED), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvIdcard.setText(span);
                tvRemind.setText(UiUtils.getString(R.string.weirenzheng));
                btnBottom.setText("立即认证");
                btnComp.setClickable(false);
                ivArrowComp.setVisibility(View.INVISIBLE);
                btnPosition.setClickable(false);
                ivArrowPosition.setVisibility(View.INVISIBLE);
                btnAddr.setClickable(false);
                ivArrowAddr.setVisibility(View.INVISIBLE);
                break;
            case "认证中":
                tvTag.setText("实名认证信息(认证中)");
                tvName.setText("认证中");
                tvIdcard.setText("认证中");
                tvComp.setText("认证中");
                tvPosition.setText("认证中");
                tvAddr.setText("认证中");
                tvRemind.setText(UiUtils.getString(R.string.renzhengzhong));
                btnBottom.setText("催一催");
                btnIdcard.setClickable(false);
                btnComp.setClickable(false);
                ivArrowComp.setVisibility(View.INVISIBLE);
                btnPosition.setClickable(false);
                ivArrowPosition.setVisibility(View.INVISIBLE);
                btnAddr.setClickable(false);
                ivArrowAddr.setVisibility(View.INVISIBLE);
                break;
            case "已认证":
                tvTag.setText("实名认证信息(已认证)");
                tvName.setText(data.getAuthentics().get(0).getName());
                String idNum = data.getAuthentics().get(0).getIdentiyCarNo();
                idNum = idNum.substring(0, 3) + "******" + idNum.substring(idNum.length() - 4, idNum.length());
                tvIdcard.setText(idNum);
                tvComp.setText(data.getAuthentics().get(0).getCompanyName());
                tvPosition.setText(data.getAuthentics().get(0).getPosition());
                String province = data.getAuthentics().get(0).getCity().getProvince().getName();
                String city = data.getAuthentics().get(0).getCity().getName();
                if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
                    tvAddr.setText(province);
                } else {
                    tvAddr.setText(province + " | " + city);
                }
                btnIdcard.setClickable(false);
                tvRemind.setVisibility(View.GONE);
                btnBottom.setVisibility(View.GONE);
                break;
            case "认证失败":
                tvTag.setText("实名认证信息(认证失败)");
                str = "认证失败";
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(Color.RED), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvIdcard.setText(span);
                span = new SpannableString(UiUtils.getString(R.string.renzhengshibai));
                span.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvRemind.setText(span);
                btnBottom.setText("重新认证");
                btnComp.setClickable(false);
                ivArrowComp.setVisibility(View.INVISIBLE);
                btnPosition.setClickable(false);
                ivArrowPosition.setVisibility(View.INVISIBLE);
                btnAddr.setClickable(false);
                ivArrowAddr.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                onBackPressed();
                break;
            case R.id.myinfo_btn_favicon:// 选择头像
                choosePhoto();
                break;
            case R.id.myinfo_btn_invite:// 点击查看指环码
                intent.setClass(this, InviteCodeActivity.class);
                intent.putExtra("inviteCode", inviteCode);
                startActivity(intent);
                break;
            case R.id.myinfo_btn_type:// 点击查看和新增身份
                intent.setClass(this, UserTypeActivity.class);
                EventBus.getDefault().postSticky(data.getAuthentics());
                startActivity(intent);
                break;
            case R.id.myinfo_rl_idcard:// 未认证和认证失败时点击认证
                if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
                    intent.setClass(this, WechatVerifyActivity.class);
                } else {
                    intent.setClass(this, CertificationIDCardActivity.class);
                }
                intent.putExtra("usertype", data.getAuthentics().get(0).getIdentiytype().getIdentiyTypeId());
                startActivity(intent);
                break;
            case R.id.myinfo_btn_comp:// 点击修改公司名
                intent.setClass(this, ChangeCompActivity.class);
                intent.putExtra("TAG", "公司");
                startActivity(intent);
                break;
            case R.id.myinfo_btn_position:// 点击修改职位
                intent.setClass(this, ChangeCompActivity.class);
                intent.putExtra("TAG", "职位");
                startActivity(intent);
                break;
            case R.id.myinfo_btn_addr:// 点击修改所在地
                intent.setClass(this, SelectProvinceActivity.class);
                intent.putExtra("TAG", "修改");
                startActivity(intent);
                break;
            case R.id.myinfo_btn_bottom:// 立即认证或催一催或者重新认证
                if ("催一催".equals(btnBottom.getText().toString())) {
                    if (clickable) {
                        clickable = false;
                        UrgeTask urgeTask = new UrgeTask();
                        urgeTask.execute();
                    }
                } else {
                    if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
                        intent.setClass(this, WechatVerifyActivity.class);
                    } else {
                        intent.setClass(this, CertificationIDCardActivity.class);
                    }
                    intent.putExtra("usertype", data.getAuthentics().get(0).getIdentiytype().getIdentiyTypeId());
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (needRefresh) {
            Intent intent = new Intent();
            intent.putExtra("needRefresh", needRefresh);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switch (intent.getStringExtra("TAG")) {
            case "changeCompany":
                tvComp.setText(intent.getStringExtra("companyName"));
                needRefresh = true;
                break;
            case "changePosition":
                tvPosition.setText(intent.getStringExtra("position"));
                needRefresh = true;
                break;
            case "城市":
                String province = intent.getStringExtra("provinceName");
                String city = intent.getStringExtra("cityName");
                if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
                    tvAddr.setText(province);
                } else {
                    tvAddr.setText(province + " | " + city);
                }
                ChangeAddr changeAddr = new ChangeAddr(intent.getStringExtra("cityId"));
                changeAddr.execute();
                break;
        }
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
                ivFavicon.setImageBitmap(bitmap);
                fos.close();

                int byteCount = bitmap.getByteCount();
                String size = StringUtils.bytes2kb(byteCount);
                Log.i("大小", size);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Log.i("分辨率", String.valueOf(width) + "*" + String.valueOf(height));

                ChangeFavicon changeFavicon = new ChangeFavicon();
                changeFavicon.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 获取指环码
    private class GetInviteCode extends AsyncTask<Void, Void, InviteCodeBean> {
        @Override
        protected InviteCodeBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETINVITECODE)),
                            Constant.BASE_URL + Constant.GETINVITECODE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("指环码", body);
                return FastJsonTools.getBean(body, InviteCodeBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(InviteCodeBean inviteCodeBean) {
            super.onPostExecute(inviteCodeBean);
            if (inviteCodeBean != null && inviteCodeBean.getStatus() == 200) {
                tvInvite.setText(inviteCodeBean.getData());
                inviteCode = inviteCodeBean.getData();
            }
        }
    }

    // 更换头像
    private class ChangeFavicon extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.changeFavicon(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CHANGEFAVICON)),
                            "file", photo_path,
                            Constant.BASE_URL + Constant.CHANGEFAVICON,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("更换头像后", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    needRefresh = true;
                    SharedPreferencesUtils.saveLocalFavicon(mContext, photo_path);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    // 修改城市
    private class ChangeAddr extends AsyncTask<Void, Void, CommonBean> {
        private String cityId;

        public ChangeAddr(String cityId) {
            this.cityId = cityId;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.endsWith(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CHANGEADDR)),
                            "cityId", cityId,
                            Constant.BASE_URL + Constant.CHANGEADDR,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("修改所在地", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean != null && commonBean.getStatus() == 200) {
                needRefresh = true;
            }
        }
    }

    // 催一催
    private class UrgeTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.URGE)),
                            "authId", String.valueOf(data.getAuthentics().get(0).getAuthId()),
                            Constant.BASE_URL + Constant.URGE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("催一催", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            clickable = true;
            DialogUtils.confirmDialog(MyInfoActivity.this, "认证加速申请成功!", "确定");
        }
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
        EventBus.getDefault().unregister(this);
    }
}
