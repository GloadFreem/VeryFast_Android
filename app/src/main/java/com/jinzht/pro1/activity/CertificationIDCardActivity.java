package com.jinzht.pro1.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 实名认证上传身份证界面
 */
public class CertificationIDCardActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RelativeLayout idcardZheng;// 身份证正面
    private RelativeLayout idcardFan;// 身份证反面
    private TextView tvIDcardZheng;// 正面加号标识
    private TextView tvIDcardFan;// 反面加号标识
    private EditText edIdnum;// 身份证号码
    private EditText edName;// 姓名
    private RelativeLayout rlCompanyName;// 公司名称整体布局
    private EditText edCompanyName;// 公司名称
    private TextView tvCompanyAddr;// 公司所在地提示
    private TextView btnCompanyAddr1;// 公司所在地选择按钮
    private RelativeLayout rlPosition;// 担任职务整体布局
    private EditText edPosition;// 职位
    private View line;// 职务下面的分割线
    private RelativeLayout rlField;// 投资领域整体布局
    private TextView tvField;// 投资领域提示
    private TextView tvSelectField;// 选择投资领域按钮
    private Button btnNext;// 下一步按钮

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体
    private Intent intent;

    private int usertype = 0;// 1:项目方,2:投资人,3:机构投资人,4:智囊团
    private Bitmap bitmap1 = null;// 正面
    private File file1 = null; // 拍照得到的身份证正面
    private String photopath1;// 身份证正面保存地址
    private Bitmap bitmap2 = null;// 反面
    private File file2 = null; // 拍照得到的身份证反面
    private String photopath2;// 身份证反面保存地址

    private String cityId;// 城市id
    private List<String> areaId = new ArrayList<>();// 领域id列表

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_idcard;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        idcardZheng = (RelativeLayout) findViewById(R.id.certification_rl_idcard_zheng);// 身份证正面
        idcardZheng.setOnClickListener(this);
        idcardFan = (RelativeLayout) findViewById(R.id.certification_rl_idcard_fan);// 身份证反面
        idcardFan.setOnClickListener(this);
        tvIDcardZheng = (TextView) findViewById(R.id.tv_idcard_zheng);// 正面加号标识
        tvIDcardFan = (TextView) findViewById(R.id.tv_idcard_fan);// 正面加号标识
        edIdnum = (EditText) findViewById(R.id.certification_ed_idnum);// 身份证号码
        edName = (EditText) findViewById(R.id.certification_ed_name);// 姓名
        rlCompanyName = (RelativeLayout) findViewById(R.id.rl_company_name);// 公司名称整体布局
        edCompanyName = (EditText) findViewById(R.id.certification_ed_company_name);// 公司名称
        tvCompanyAddr = (TextView) findViewById(R.id.certification_tv_company_addr);// 公司所在地提示
        btnCompanyAddr1 = (TextView) findViewById(R.id.certification_tv_company_addr1);// 公司所在地选择按钮
        btnCompanyAddr1.setOnClickListener(this);
        rlPosition = (RelativeLayout) findViewById(R.id.rl_position);// 担任职务整体布局
        edPosition = (EditText) findViewById(R.id.certification_ed_position);// 职位
        line = findViewById(R.id.line);// 职务下面的分割线
        rlField = (RelativeLayout) findViewById(R.id.rl_field);// 投资领域整体布局
        tvField = (TextView) findViewById(R.id.tv_field);// 投资领域提示
        tvSelectField = (TextView) findViewById(R.id.tv_select_field);// 选择投资领域按钮
        tvSelectField.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.certification_btn_next);// 下一步按钮
        btnNext.setOnClickListener(this);

        setMytitle();
        initDifferent();
        initData();
    }

    // 根据身份类型不同而加载不同标题
    private void setMytitle() {
        // 项目方、投资人、智囊团都是3个步骤
        if (Constant.USERTYPE_XMF == getIntent().getIntExtra("usertype", 0)
                || Constant.USERTYPE_TZR == getIntent().getIntExtra("usertype", 0)
                || Constant.USERTYPE_ZNT == getIntent().getIntExtra("usertype", 0)) {
            str = "实名认证(1/3)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
            // 投资机构是4个步骤
        } else if (Constant.USERTYPE_TZJG == getIntent().getIntExtra("usertype", 0)) {
            str = "实名认证(1/4)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
        }
    }

    // 根据身份类型不同而加载不同控件
    private void initDifferent() {
        switch (getIntent().getIntExtra("usertype", 0)) {
            case Constant.USERTYPE_XMF:// 项目方，无领域选择
                rlField.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                break;
            case Constant.USERTYPE_TZR:// 投资人，无公司名、职务
                rlCompanyName.setVisibility(View.GONE);
                rlPosition.setVisibility(View.GONE);
                tvCompanyAddr.setText("所在地");
                btnCompanyAddr1.setText("请选择所在地");
                break;
            case Constant.USERTYPE_TZJG:// 投资机构，无领域选择
                rlField.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                break;
            case Constant.USERTYPE_ZNT:// 智囊团，公司名和职务选填，投资领域改为服务领域
                edCompanyName.setHint("请输入公司名称(选填)");
                edPosition.setHint("请输入担任职务(选填)");
                tvField.setText("服务领域");
                tvSelectField.setText("请选择服务领域");
                break;
        }
    }

    private void initData() {
        usertype = getIntent().getIntExtra("usertype", 0);
        photopath1 = getCacheDir() + "/" + "idcard_zheng.jpg";
        photopath2 = getCacheDir() + "/" + "idcard_fan.jpg";
        file1 = new File(Environment.getExternalStorageDirectory() + "/" + "idcard_zheng.jpg");
        file2 = new File(Environment.getExternalStorageDirectory() + "/" + "idcard_fan.jpg");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.certification_rl_idcard_zheng:// 点击上传身份证正面照片
                chooseIDcard(0);
                break;
            case R.id.certification_rl_idcard_fan:// 点击上传身份证反面照片
                chooseIDcard(1);
                break;
            case R.id.certification_tv_company_addr1:// 点击选择公司所在地
                intent = new Intent(this, SelectProvinceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_select_field:// 点击选择投资领域
                intent = new Intent(this, SelectInvestFieldActivity.class);
                intent.putExtra("usertype", usertype);
                startActivity(intent);
                break;
            case R.id.certification_btn_next:// 跳转到下一步界面
                File file1 = new File(photopath1);
                File file2 = new File(photopath2);
                if (!file1.exists()) {
                    SuperToastUtils.showSuperToast(this, 2, "请上传身份证正面照片");
                } else if (!file2.exists()) {
                    SuperToastUtils.showSuperToast(this, 2, "请上传身份证反面照片");
                } else if (StringUtils.isBlank(edIdnum.getText().toString())) {
                    SuperToastUtils.showSuperToast(this, 2, "请填写身份证号码");
                } else if (StringUtils.isBlank(edName.getText().toString())) {
                    SuperToastUtils.showSuperToast(this, 2, "请填写真实姓名");
                } else if (StringUtils.isBlank(cityId)) {
                    SuperToastUtils.showSuperToast(this, 2, "请选择所在地");
                } else if (usertype == Constant.USERTYPE_XMF || usertype == Constant.USERTYPE_TZJG) {
                    if (StringUtils.isBlank(edCompanyName.getText().toString())) {
                        SuperToastUtils.showSuperToast(this, 2, "请填写公司名称");
                    }
                    if (StringUtils.isBlank(edPosition.getText().toString())) {
                        SuperToastUtils.showSuperToast(this, 2, "请填写职务");
                    }
                } else if (usertype == Constant.USERTYPE_TZR || usertype == Constant.USERTYPE_ZNT) {
                    if (areaId == null) {
                        SuperToastUtils.showSuperToast(this, 2, "请选择领域");
                    } else {
                        if (areaId.size() == 0) {
                            SuperToastUtils.showSuperToast(this, 2, "请选择领域");
                        }
                    }
                } else {
                    intent = new Intent(this, CertificationCompActivity.class);
                    intent.putExtra("identiyCarA", photopath1);
                    intent.putExtra("identiyCarB", photopath2);
                    intent.putExtra("identiyCarNo", edIdnum.getText().toString());
                    intent.putExtra("name", edName.getText().toString());
                    intent.putExtra("companyName", edCompanyName.getText().toString());// 项目方和投资机构必填
                    intent.putExtra("cityId", cityId);
                    intent.putExtra("position", edPosition.getText().toString());// 项目方和投资机构必填
                    intent.putExtra("areaId", (Serializable) areaId);// 投资人和智囊团必填
                    startActivity(intent);
                }
                break;
        }
    }

    // 选择身份证
    private void chooseIDcard(final int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择打开方式")
                .setItems(R.array.chose_photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {// 打开照相机
                            if (flag == 0) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
                                startActivityForResult(intent, 10);// 拍照正面
                            } else {// 反面
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
                                startActivityForResult(intent, 11);// 拍照反面
                            }
                        } else {// 从相册选择
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 856);
                            intent.putExtra("aspectY", 540);
                            intent.putExtra("outputX", 856);
                            intent.putExtra("outputY", 540);
                            intent.putExtra("scale", true);
                            intent.putExtra("return-data", false);
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            intent.putExtra("noFaceDetection", true);
                            if (flag == 0) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
                                startActivityForResult(intent, 0);// 相册正面
                            } else {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
                                startActivityForResult(intent, 1);// 相册反面
                            }
                        }
                    }
                });
        builder.create().show();
    }

    // 选择身份证
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10) {// 拍照正面
            UiUtils.crop(Uri.fromFile(file1), 856, 540, 856, 540, this, 0);
        } else if (resultCode == RESULT_OK && requestCode == 11) {// 拍照反面
            UiUtils.crop(Uri.fromFile(file2), 856, 540, 856, 540, this, 1);
        } else if (resultCode == RESULT_OK && requestCode == 0) {// 剪裁过的正面
            try {
                bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(file1)));

                int byteCount0 = bitmap1.getByteCount();
                String size0 = StringUtils.bytes2kb(byteCount0);
                Log.i("正面大小", size0);
                int width0 = bitmap1.getWidth();
                int height0 = bitmap1.getHeight();
                Log.i("正面分辨率", String.valueOf(width0) + "*" + String.valueOf(height0));

                FileOutputStream fos = new FileOutputStream(photopath1);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 压缩另存，100表示不压缩
                bitmap1 = BitmapFactory.decodeFile(photopath1);
                tvIDcardZheng.setVisibility(View.GONE);
                idcardZheng.setBackgroundDrawable(new BitmapDrawable(bitmap1));
                fos.close();

                int byteCount = bitmap1.getByteCount();
                String size = StringUtils.bytes2kb(byteCount);
                Log.i("正面大小", size);
                int width = bitmap1.getWidth();
                int height = bitmap1.getHeight();
                Log.i("正面分辨率", String.valueOf(width) + "*" + String.valueOf(height));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == 1) {// 剪裁过的反面
            try {
                bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(file2)));

                int byteCount0 = bitmap2.getByteCount();
                String size0 = StringUtils.bytes2kb(byteCount0);
                Log.i("反面大小", size0);
                int width0 = bitmap2.getWidth();
                int height0 = bitmap2.getHeight();
                Log.i("反面分辨率", String.valueOf(width0) + "*" + String.valueOf(height0));

                FileOutputStream fos = new FileOutputStream(photopath2);
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 压缩另存，100表示不压缩
                bitmap2 = BitmapFactory.decodeFile(photopath2);
                tvIDcardFan.setVisibility(View.GONE);
                idcardFan.setBackgroundDrawable(new BitmapDrawable(bitmap2));
                fos.close();

                int byteCount = bitmap2.getByteCount();
                String size = StringUtils.bytes2kb(byteCount);
                Log.i("反面大小", size);
                int width = bitmap2.getWidth();
                int height = bitmap2.getHeight();
                Log.i("反面分辨率", String.valueOf(width) + "*" + String.valueOf(height));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        cityId = intent.getStringExtra("cityId");
        String province = intent.getStringExtra("provinceName");
        String city = intent.getStringExtra("cityName");
        if (!StringUtils.isBlank(province) && !StringUtils.isBlank(city)) {
            btnCompanyAddr1.setText(province + " | " + city);
        }
        Log.i("选择的城市id", cityId + "");
        areaId = (List<String>) intent.getSerializableExtra("areaId");
        if (areaId != null) {
            String files = intent.getSerializableExtra("areaName").toString();
            files = files.substring(1, files.length() - 1).replaceAll(",", " | ");
            tvSelectField.setText(files);
            Log.i("选择的领域id", areaId.toString());
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
