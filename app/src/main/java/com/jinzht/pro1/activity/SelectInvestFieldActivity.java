package com.jinzht.pro1.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 选择投资领域界面
 */
public class SelectInvestFieldActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;
    private TextView tvTitle;
    private ListView lvInvestField;

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
        lvInvestField = (ListView) findViewById(R.id.lv_invest_field);// 投资领域列表

        tvTitle.setText("投资领域");
        lvInvestField.setAdapter(new InvestFieldAdapt());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
    }

    private class InvestFieldAdapt extends BaseAdapter {

        @Override
        public int getCount() {
            return 11;
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
            View view = View.inflate(mContext, R.layout.item_invest_field, null);
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
