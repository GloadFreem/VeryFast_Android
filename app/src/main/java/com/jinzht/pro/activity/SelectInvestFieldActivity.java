package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.FieldListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择投资领域界面
 */
public class SelectInvestFieldActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private LinearLayout btnConfirm; // 确定
    private ListView lvInvestField;// ListView

    private List<FieldListBean.DataBean> fields = new ArrayList<>();// 领域列表
    private List<String> checkeds = new ArrayList<>();// 已选择的领域
    private List<String> checkedNames = new ArrayList<>();// 已选择的领域名称

    @Override
    protected int getResourcesId() {
        return R.layout.activity_select_invest_field;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏背景与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        pageError = (LinearLayout) findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        btnConfirm = (LinearLayout) findViewById(R.id.btn_confirm);// 确定按钮
        btnConfirm.setOnClickListener(this);
        lvInvestField = (ListView) findViewById(R.id.lv_invest_field);// 投资领域列表

        switch (getIntent().getIntExtra("usertype", 0)) {
            case Constant.USERTYPE_TZR:// 投资人，显示投资领域
                tvTitle.setText("投资领域");
                break;
            case Constant.USERTYPE_ZNT:// 智囊团，显示服务领域
                tvTitle.setText("服务领域");
                break;
        }

        GetFieldListTask getFieldListTask = new GetFieldListTask();
        getFieldListTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_confirm:// 确定
                System.out.println(checkeds);
                System.out.println(checkedNames);
                if (checkeds.size() > 3) {
                    SuperToastUtils.showSuperToast(this, 2, "最多可选3项");
                } else if (checkeds.size() == 0) {
                    finish();
                } else {
                    Intent intent = new Intent(mContext, CertificationIDCardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("TAG", "领域");
                    intent.putExtra("areaId", (Serializable) checkeds);
                    intent.putExtra("areaName", (Serializable) checkedNames);
                    startActivity(intent);
                }
                break;
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetFieldListTask getFieldListTask = new GetFieldListTask();
                    getFieldListTask.execute();
                }
                break;
        }
    }

    private class InvestFieldAdapt extends BaseAdapter {

        @Override
        public int getCount() {
            return fields.size();
        }

        @Override
        public Object getItem(int position) {
            return fields.get(position);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_invest_field, null);
                holder.field = (CheckBox) convertView.findViewById(R.id.item_field);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.field.setText(fields.get(position).getName());
            holder.field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkeds.add(String.valueOf(fields.get(position).getAreaId()));
                        checkedNames.add(fields.get(position).getName());
                    } else {
                        checkeds.remove(String.valueOf(fields.get(position).getAreaId()));
                        checkedNames.remove(fields.get(position).getName());
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public CheckBox field;
        }
    }

    // 获取领域列表
    private class GetFieldListTask extends AsyncTask<Void, Void, FieldListBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected FieldListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETFIELDLIST)),
                            Constant.BASE_URL + Constant.GETFIELDLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("领域列表", body);
                return FastJsonTools.getBean(body, FieldListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(FieldListBean fieldListBean) {
            super.onPostExecute(fieldListBean);
            clickable = true;
            dismissProgressDialog();
            if (fieldListBean == null) {
                pageError.setVisibility(View.VISIBLE);
            } else {
                if (fieldListBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    fields = fieldListBean.getData();
                    lvInvestField.setAdapter(new InvestFieldAdapt());
                } else {
                    pageError.setVisibility(View.VISIBLE);
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
