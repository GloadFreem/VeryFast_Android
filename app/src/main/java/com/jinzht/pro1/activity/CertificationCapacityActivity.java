package com.jinzht.pro1.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 选择投资能力界面
 */
public class CertificationCapacityActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private ListView certificationLvCapacity;// 投资能力列表
    private Button certificationBtnComplete;// 完成按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_capacity;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        certificationLvCapacity = (ListView) findViewById(R.id.certification_lv_capacity);// 投资能力列表
        certificationBtnComplete = (Button) findViewById(R.id.certification_btn_complete);// 完成按钮
        certificationBtnComplete.setOnClickListener(this);

        certificationLvCapacity.setAdapter(new InvestCapacityAdapt());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.certification_btn_complete:// 完成实名认证，跳转至主页
                SuperToastUtils.showSuperToast(this, 2, "完成");
                break;
        }
    }

    private class InvestCapacityAdapt extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.item_invest_capacity, null);

            return view;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
