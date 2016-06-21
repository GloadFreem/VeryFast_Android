package com.jinzht.pro.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.UiHelp;

/**
 * 项目中心界面
 */
public class ProCenterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView title;// 标题
    private ListView listView;// 项目列表

    private ProAdapter proAdapter;// 项目方的数据列表填充器
    private InvestorAdapter investorAdapter;// 投资人的数据列表填充器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_pro_center;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_title);// 标题
        title.setText("项目中心");
        listView = (ListView) findViewById(R.id.pro_center_list);// 项目列表

        initList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
    }

    private void initList() {
        listView.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        proAdapter = new ProAdapter();
//        listView.setAdapter(proAdapter);
        investorAdapter = new InvestorAdapter();
        listView.setAdapter(investorAdapter);
    }

    // 项目方的数据列表填充器
    private class ProAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_pro_center_for_pro, null);
            return view;
        }
    }

    // 投资人的数据列表填充器
    private class InvestorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (position == 0) {
                view = inflater.inflate(R.layout.item_pro_center_for_investor_type, null);
            } else if (position == 3) {
                view = inflater.inflate(R.layout.item_pro_center_for_investor_type, null);
            } else if (position > 0 && position < 3) {
                view = inflater.inflate(R.layout.item_pro_center_for_investor_invest, null);
            } else if (position > 3) {
                view = inflater.inflate(R.layout.item_pro_center_for_investor_received, null);
            }
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
