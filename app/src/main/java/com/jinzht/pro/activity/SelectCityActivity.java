package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CityListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择市界面
 */
public class SelectCityActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout btnConfirm; // 确定
    private ListView lvInvestField;// 市列表

    private List<CityListBean.DataBean> citys = new ArrayList<>();// 市

    @Override
    protected int getResourcesId() {
        return R.layout.activity_select_invest_field;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("公司所在地");
        btnConfirm = (LinearLayout) findViewById(R.id.btn_confirm);// 确定按钮
        btnConfirm.setVisibility(View.GONE);
        lvInvestField = (ListView) findViewById(R.id.lv_invest_field);// 省份列表

        GetCityListTask getCityListTask = new GetCityListTask();
        getCityListTask.execute();

        lvInvestField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("选择了", citys.get(position).getName());
                Intent intent = new Intent(mContext, CertificationIDCardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("TAG", "城市");
                intent.putExtra("cityId", String.valueOf(citys.get(position).getCityId()));
                intent.putExtra("provinceName", getIntent().getStringExtra("provinceName"));
                intent.putExtra("cityName", citys.get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
    }

    private class CityAdapt extends BaseAdapter {

        @Override
        public int getCount() {
            return citys.size();
        }

        @Override
        public Object getItem(int position) {
            return citys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_city, null);
                holder.city = (TextView) convertView.findViewById(R.id.item_tv_city);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.city.setText(citys.get(position).getName());
            return convertView;
        }

        public class ViewHolder {
            public TextView city;
        }
    }

    // 根据省份id获取市列表
    private class GetCityListTask extends AsyncTask<Void, Void, CityListBean> {
        @Override
        protected CityListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETCITYLIST)),
                            "provinceId", getIntent().getStringExtra("provinceId"),
                            Constant.BASE_URL + Constant.GETCITYLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("市列表", body);
                return FastJsonTools.getBean(body, CityListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CityListBean cityListBean) {
            super.onPostExecute(cityListBean);
            if (cityListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (cityListBean.getStatus() == 200) {
                    citys = cityListBean.getData();
                    lvInvestField.setAdapter(new CityAdapt());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, cityListBean.getMessage());
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
