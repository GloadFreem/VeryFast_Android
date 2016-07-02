package com.jinzht.pro.activity;

import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.AuthenticateBean;
import com.jinzht.pro.bean.CapacityListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择投资能力界面
 */
public class CertificationCapacityActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private ListView certificationLvCapacity;// 投资能力列表
    private Button certificationBtnComplete;// 完成按钮

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体
    private int usertype;// 1:项目方,2:投资人,3:机构投资人,4:智囊团

    private List<String> capacitys = new ArrayList<String>();// 投资能力
    private List<String> checkeds = new ArrayList<>();// 已选择的能力

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_capacity;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        certificationLvCapacity = (ListView) findViewById(R.id.certification_lv_capacity);// 投资能力列表
        certificationBtnComplete = (Button) findViewById(R.id.certification_btn_complete);// 完成按钮
        certificationBtnComplete.setOnClickListener(this);

        usertype = getIntent().getIntExtra("usertype", -1);
        setMytitle();

        GetCapacityListTask getCapacityListTask = new GetCapacityListTask();
        getCapacityListTask.execute();
    }

    // 根据身份类型不同而加载不同标题
    private void setMytitle() {
        // 投资人是3个步骤
        if (Constant.USERTYPE_TZR == usertype) {
            str = "实名认证(3/3)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
            // 投资机构4个步骤
        } else if (Constant.USERTYPE_TZJG == usertype) {
            str = "实名认证(4/4)";
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
            case R.id.certification_btn_complete:// 完成实名认证，跳转至主页
                if (checkeds == null) {
                    SuperToastUtils.showSuperToast(this, 2, "请选择投资能力");
                } else if (checkeds.size() == 0) {
                    SuperToastUtils.showSuperToast(this, 2, "请选择投资能力");
                } else {
                    AuthenticateTask authenticateTask = new AuthenticateTask();
                    authenticateTask.execute();
                }
                break;
        }
    }

    private class InvestCapacityAdapt extends BaseAdapter {

        @Override
        public int getCount() {
            return capacitys.size();
        }

        @Override
        public Object getItem(int position) {
            return capacitys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_invest_capacity, null);
                holder.capacity = (CheckBox) convertView.findViewById(R.id.item_capacity);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.capacity.setText(capacitys.get(position));
            holder.capacity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkeds.add(String.valueOf(position));
                    } else {
                        checkeds.remove(String.valueOf(position));
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public CheckBox capacity;
        }
    }

    // 获取投资能力列表
    private class GetCapacityListTask extends AsyncTask<Void, Void, CapacityListBean> {
        @Override
        protected CapacityListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETCAPACITYLIST)),
                            Constant.BASE_URL + Constant.GETCAPACITYLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("领域列表", body);
                return FastJsonTools.getBean(body, CapacityListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CapacityListBean capacityListBean) {
            super.onPostExecute(capacityListBean);
            if (capacityListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (capacityListBean.getStatus() == 200) {
                    capacitys = capacityListBean.getData();
                    certificationLvCapacity.setAdapter(new InvestCapacityAdapt());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, capacityListBean.getMessage());
                }
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
                    String optional = checkeds.toString();
                    optional = optional.substring(1, optional.length() - 1);
                    if (usertype == Constant.USERTYPE_TZR) {// 投资人不上传营业执照
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
                                "areaId", getIntent().getStringExtra("areaId"),
                                "introduce", getIntent().getStringExtra("introduce"),
                                "companyIntroduce", getIntent().getStringExtra("companyIntroduce"),
                                "optional", optional,
                                Constant.BASE_URL + Constant.AUTHENTICATE,
                                mContext
                        );
                    } else {
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
                                "areaId", getIntent().getStringExtra("areaId"),
                                "buinessLicence", getIntent().getStringExtra("buinessLicence"),
                                "buinessLicenceNo", getIntent().getStringExtra("buinessLicenceNo"),
                                "introduce", getIntent().getStringExtra("introduce"),
                                "companyIntroduce", getIntent().getStringExtra("companyIntroduce"),
                                "optional", optional,
                                Constant.BASE_URL + Constant.AUTHENTICATE,
                                mContext
                        );
                    }
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
                    DialogUtils.confirmToMainDialog(CertificationCapacityActivity.this, authenticateBean.getMessage());
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
