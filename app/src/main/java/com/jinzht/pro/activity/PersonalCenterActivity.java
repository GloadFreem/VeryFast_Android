package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinzht.pro.R;
import com.jinzht.pro.adapter.PersonalCenterRVAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.ShareBean;
import com.jinzht.pro.bean.UserInfoBean;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.ShareUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 个人中心界面
 */
public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView ivFavicon;// 头像
    private ImageView ivLevel;// 等级
    private TextView tvName;// 姓名
    private TextView tvPosition;// 公司名和职位
    private RecyclerView recyclerView;// 条目集合
    private RelativeLayout btnExit;// 退出按钮

    private List<Integer> itemIcons;// 条目图标
    private List<String> itemNames;// 条目名
    private PersonalCenterRVAdapter rvAdapter;// 条目适配器

    private UserInfoBean.DataBean data;// 个人信息
    private final static int REQUEST_CODE = 1;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        ivFavicon = (CircleImageView) findViewById(R.id.personal_center_iv_favicon);// 头像
        ivFavicon.setOnClickListener(this);
        ivLevel = (ImageView) findViewById(R.id.personal_center_v);// 等级
        tvName = (TextView) findViewById(R.id.personal_center_tv_name);// 姓名
        tvPosition = (TextView) findViewById(R.id.personal_center_tv_position);// 公司名和职位
        recyclerView = (RecyclerView) findViewById(R.id.personal_center_rv);// 条目集合
        btnExit = (RelativeLayout) findViewById(R.id.personal_center_btn_exit);// 退出按钮
        btnExit.setOnClickListener(this);

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

        // 处理RecyclerView的item
        initRecyclerView();

        GetUserInfo getUserInfo = new GetUserInfo();
        getUserInfo.execute();
    }

    private void initData() {
        if (!StringUtils.isBlank(data.getHeadSculpture())) {
            Glide.with(mContext).load(data.getHeadSculpture()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivFavicon);
            SharedPreferencesUtils.saveOnlineFavicon(mContext, data.getHeadSculpture());
        } else {
            ivFavicon.setImageResource(R.drawable.ic_launcher);
        }
        if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
            // 微信登录
            tvName.setText(SharedPreferencesUtils.getWechatNick(mContext));
        } else {
            if (data.getAuthentics() != null && data.getAuthentics().size() != 0) {
                if (data.getAuthentics().size() == 1) {
                    ivLevel.setImageResource(R.mipmap.icon_v);
                } else if (data.getAuthentics().size() == 2) {
                    ivLevel.setImageResource(R.mipmap.icon_v2);
                }
                if (!StringUtils.isBlank(data.getAuthentics().get(0).getName())) {
                    tvName.setText(data.getAuthentics().get(0).getName());
                } else {
                    tvName.setText(SharedPreferencesUtils.getTelephone(mContext));
                }
                if (!StringUtils.isBlank(data.getAuthentics().get(0).getCompanyName()) && !StringUtils.isBlank(data.getAuthentics().get(0).getPosition())) {
                    tvPosition.setText(data.getAuthentics().get(0).getCompanyName() + " | " + data.getAuthentics().get(0).getPosition());
                }
            } else {
                tvName.setText(SharedPreferencesUtils.getTelephone(mContext));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_center_iv_favicon:// 点击头像，进入个人信息
                Intent intent = new Intent(mContext, MyInfoActivity.class);
                if (data != null) {
                    EventBus.getDefault().postSticky(data);
                }
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.personal_center_btn_exit:// 返回上一页
                finish();
                break;
        }
    }

    private void initRecyclerView() {
        // 准备数据
        itemIcons = new ArrayList<>(Arrays.asList(R.mipmap.icon_account, R.mipmap.icon_mycollect, R.mipmap.icon_myactivity, R.mipmap.icon_mygold, R.mipmap.icon_project_center, R.mipmap.icon_setting, R.mipmap.icon_aboutus, R.mipmap.icon_share_friends));
        itemNames = new ArrayList<>(Arrays.asList("资金账户", "我的关注", "我的活动", "我的金条", "项目中心", "软件设置", "关于平台", "推荐好友"));
        rvAdapter = new PersonalCenterRVAdapter(mContext, itemIcons, itemNames);
        // 填充数据
        RecyclerViewData.setGrid(recyclerView, mContext, rvAdapter, 4);
        // 条目点击事件
        rvAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:// 资金账户
                        if (data == null) {
                            SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                        } else if ("已认证".equals(data.getAuthentics().get(0).getAuthenticstatus().getName())) {
                            intent.setClass(mContext, AccountActivity.class);
                            if (data != null) {
                                EventBus.getDefault().postSticky(data);
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if ("认证中".equals(data.getAuthentics().get(0).getAuthenticstatus().getName())) {
                            DialogUtils.confirmDialog(PersonalCenterActivity.this, "您的信息正在认证中，通过后方可查看！", "确定");
                        } else {
                            DialogUtils.goAuthentic(PersonalCenterActivity.this);
                        }
                        break;
                    case 1:// 我的关注
                        if (data == null) {
                            SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                        } else {
                            intent.setClass(mContext, MyCollectActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:// 我的活动
                        if (data == null) {
                            SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                        } else {
                            intent.setClass(mContext, MyActivitysActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 3:// 我的金条
                        intent.setClass(mContext, MyGoldActivity.class);
                        startActivity(intent);
                        break;
                    case 4:// 项目中心
                        if (data == null) {
                            SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                        } else {
                            intent.setClass(mContext, ProCenterActivity.class);
                            intent.putExtra("usertype", data.getAuthentics().get(0).getIdentiytype().getIdentiyTypeId());
                            startActivity(intent);
                        }
                        break;
                    case 5:// 软件设置
                        intent.setClass(mContext, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case 6:// 关于平台
                        intent.setClass(mContext, AboutUsActivity.class);
                        startActivity(intent);
                        break;
                    case 7:// 推荐好友
                        if (clickable) {
                            clickable = false;
                            ShareTask shareTask = new ShareTask();
                            shareTask.execute();
                        }
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

    // 获取认证信息
    private class GetUserInfo extends AsyncTask<Void, Void, UserInfoBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected UserInfoBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETUSERINFO)),
                            Constant.BASE_URL + Constant.GETUSERINFO,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("用户信息", body);
                return FastJsonTools.getBean(body, UserInfoBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserInfoBean userInfoBean) {
            super.onPostExecute(userInfoBean);
            dismissProgressDialog();
            if (userInfoBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (userInfoBean.getStatus() == 200) {
                    data = userInfoBean.getData();
                    if (data != null) {
                        SharedPreferencesUtils.saveExtUserId(mContext, String.valueOf(data.getExtUserId()));
                        SharedPreferencesUtils.saveUserId(mContext, data.getUserId());
                        SharedPreferencesUtils.saveIsAuthentic(mContext, data.getAuthentics().get(0).getAuthenticstatus().getName());
                        initData();
                    }
                } else if (userInfoBean.getStatus() == 401) {
                    AutoLoginTask autoLoginTask = new AutoLoginTask();
                    autoLoginTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, userInfoBean.getMessage());
                }
            }
        }
    }

    @Override
    public void doAgain() {
        super.doAgain();
        GetUserInfo getUserInfo = new GetUserInfo();
        getUserInfo.execute();
    }

    // 分享APP
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREAPP)),
                            "type", "3",
                            Constant.BASE_URL + Constant.SHAREAPP,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("分享返回信息", body);
                return FastJsonTools.getBean(body, ShareBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ShareBean shareBean) {
            super.onPostExecute(shareBean);
            clickable = true;
            if (shareBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(PersonalCenterActivity.this);
                    DialogUtils.newShareDialog(PersonalCenterActivity.this, shareUtils, shareBean.getData().getTitle(), shareBean.getData().getContent(), shareBean.getData().getImage(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == MyInfoActivity.RESULT_CODE) {
                if (data.getBooleanExtra("needRefresh", false)) {// 修改了信息
                    GetUserInfo getUserInfo = new GetUserInfo();
                    getUserInfo.execute();
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
