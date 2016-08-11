package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.adapter.AccountAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.GoldAccount;
import com.jinzht.pro.bean.ShareBean;
import com.jinzht.pro.bean.WebBean;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.ShareUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 我的金条界面
 */
public class MyGoldActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvGoldAmount;// 金条数量
    private RecyclerView recyclerView;// 条目网格

    private List<Integer> icons;// 填充的item图标
    private List<String> names;// 填充的item名
    private AccountAdapter mAdapter;// Recycler填充器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_my_gold;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        btnBack = (LinearLayout) findViewById(R.id.gold_btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvGoldAmount = (TextView) findViewById(R.id.gold_tv_amount);// 金条数量
        recyclerView = (RecyclerView) findViewById(R.id.gold_rv_items);// 条目网格

        // 填充RecyclerView
        initItems();

        GetGoldCount getGoldCount = new GetGoldCount();
        getGoldCount.execute();
    }

    private void initItems() {
        // 准备数据
        icons = new ArrayList<>(Arrays.asList(R.mipmap.icon_inout, R.mipmap.icon_give, R.mipmap.icon_accumulate, R.mipmap.icon_useage));
        names = new ArrayList<>(Arrays.asList("收支明细", "邀请送金条", "金条积累规则", "金条使用规则"));
        mAdapter = new AccountAdapter(mContext, icons, names);
        // 填充数据
        RecyclerViewData.setGrid(recyclerView, mContext, mAdapter, 2);
        // 条目点击事件
        mAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:// 收支明细
                        intent.setClass(mContext, GoldInOutActivity.class);
                        startActivity(intent);
                        break;
                    case 1:// 邀请送金条
                        InviteFriendTask inviteFriendTask = new InviteFriendTask();
                        inviteFriendTask.execute();
                        break;
                    case 2:// 金条积累规则
                        GoldGetRule goldGetRule = new GoldGetRule();
                        goldGetRule.execute();
                        break;
                    case 3:// 金条使用规则
                        GoldUseRule goldUseRule = new GoldUseRule();
                        goldUseRule.execute();
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gold_btn_back:// 返回上一页
                finish();
                break;
        }
    }

    // 获取金条数量
    private class GetGoldCount extends AsyncTask<Void, Void, GoldAccount> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected GoldAccount doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETGOLDACCOUNT)),
                            Constant.BASE_URL + Constant.GETGOLDACCOUNT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("金条账户", body);
                return FastJsonTools.getBean(body, GoldAccount.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(GoldAccount goldAccount) {
            super.onPostExecute(goldAccount);
            dismissProgressDialog();
            if (goldAccount == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (goldAccount.getStatus() == 200) {
                    tvGoldAmount.setText(String.valueOf(goldAccount.getData().getCount()));
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, goldAccount.getMessage());
                }
            }
        }
    }

    // 邀请好友送金条
    private class InviteFriendTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.INVITEFRIEND)),
                            Constant.BASE_URL + Constant.INVITEFRIEND,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("邀请好友送金条", body);
                return FastJsonTools.getBean(body, ShareBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ShareBean shareBean) {
            super.onPostExecute(shareBean);
            if (shareBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(MyGoldActivity.this);
                    DialogUtils.newShareDialog(MyGoldActivity.this, shareUtils, shareBean.getData().getTitle(), shareBean.getData().getContent(), shareBean.getData().getImage(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
                }
            }
        }
    }

    // 金条积累规则
    private class GoldGetRule extends AsyncTask<Void, Void, WebBean> {
        @Override
        protected WebBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GOLDGETRULE)),
                            Constant.BASE_URL + Constant.GOLDGETRULE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("金条积累规则", body);
                return FastJsonTools.getBean(body, WebBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(WebBean webBean) {
            super.onPostExecute(webBean);
            if (webBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (webBean.getStatus() == 200) {
                    if (webBean.getData() != null && !StringUtils.isBlank(webBean.getData().getUrl())) {
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra("title", "金条积累规则");
                        intent.putExtra("url", webBean.getData().getUrl());
                        startActivity(intent);
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, webBean.getMessage());
                }
            }
        }
    }

    // 金条使用规则
    private class GoldUseRule extends AsyncTask<Void, Void, WebBean> {
        @Override
        protected WebBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GOLDUSERULE)),
                            Constant.BASE_URL + Constant.GOLDUSERULE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("金条使用规则", body);
                return FastJsonTools.getBean(body, WebBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(WebBean webBean) {
            super.onPostExecute(webBean);
            if (webBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (webBean.getStatus() == 200) {
                    if (webBean.getData() != null && !StringUtils.isBlank(webBean.getData().getUrl())) {
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra("title", "金条使用规则");
                        intent.putExtra("url", webBean.getData().getUrl());
                        startActivity(intent);
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, webBean.getMessage());
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
