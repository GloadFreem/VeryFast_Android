package com.jinzht.pro.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jinzht.pro.R;
import com.jinzht.pro.activity.ActivityDetailActivity;
import com.jinzht.pro.activity.CommonWebViewActivity;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.ActivityApplyBean;
import com.jinzht.pro.bean.ActivityListBean;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.InvestorListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.CacheUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 活动列表界面
 */
public class ActivityFragment extends BaseFragment implements View.OnClickListener {

    private EditText edSearch;// 搜索输入框
    private RelativeLayout btnSearch;// 搜索按钮
    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 活动列表

    private MyAdapter myAdapter;
    private int pages = 0;
    private int searchPages = 0;
    private boolean isSearch = false;
    private List<ActivityListBean.DataBean> datas = new ArrayList<>();// 数据集合
    private int POSITION = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        edSearch = (EditText) view.findViewById(R.id.activity_edt_search);// 搜索输入框
        btnSearch = (RelativeLayout) view.findViewById(R.id.activity_btn_search);// 搜索按钮
        btnSearch.setOnClickListener(this);
        pageError = (LinearLayout) view.findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) view.findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.activity_lv);// 活动列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        myAdapter = new MyAdapter();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                    Intent intent = new Intent(mContext, ActivityDetailActivity.class);
                    intent.putExtra("id", datas.get(position).getActionId());
                    startActivityForResult(intent, REQUEST_CODE);
                    POSITION = position;
                } else {
                    Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                    intent.putExtra("title", "活动详情");
                    intent.putExtra("url", "file:///android_asset/error.html");
                    startActivity(intent);
                }
            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GetActivityListTask getActivityListTask = new GetActivityListTask(0);
//                getActivityListTask.execute();
//            }
//        }, 2500);

        // 读取缓存
        String cacheActivitysList = (String) CacheUtils.readObject(Constant.CACHE_ACTIVITYS_LIST);
        if (!StringUtils.isBlank(cacheActivitysList)) {
            ActivityListBean bean = FastJsonTools.getBean(cacheActivitysList, ActivityListBean.class);
            if (bean != null && bean.getStatus() == 200) {
                datas = bean.getData();
                if (datas != null && datas.size() != 0) {
                    listview.setAdapter(myAdapter);
                }
            }
        } else {
            pageError.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.INVISIBLE);
        }
    }

    // 接收是否登录成功的提示
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getLoginInfo(EventMsg msg) {
        if ("登录成功".equals(msg.getMsg())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetActivityListTask getActivityListTask = new GetActivityListTask(0);
                    getActivityListTask.execute();
                }
            }, 2500);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_btn_search:// 点击搜索
                if (StringUtils.isBlank(edSearch.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入搜索内容");
                } else {
                    if (clickable) {
                        clickable = false;
                        isSearch = true;
                        ActivitySearch activitySearch = new ActivitySearch(0);
                        activitySearch.execute();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                    }
                }
                break;
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetActivityListTask getActivityListTask = new GetActivityListTask(0);
                    getActivityListTask.execute();
                }
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activity, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.itemTime = (TextView) convertView.findViewById(R.id.item_time);
                holder.itemNum = (TextView) convertView.findViewById(R.id.item_num);
                holder.itemActivityFree = (TextView) convertView.findViewById(R.id.item_activity_free);
                holder.itemAddr = (TextView) convertView.findViewById(R.id.item_addr);
                holder.btnApply = (TextView) convertView.findViewById(R.id.btn_apply);
                holder.itemDistance = (TextView) convertView.findViewById(R.id.item_distance);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText(datas.get(position).getName());
            String time = datas.get(position).getStartTime();
            holder.itemTime.setText(time.substring(0, 10).replaceAll("-", ".") + " " + DateUtils.getWeek(time) + " " + time.substring(11, 16));
            holder.itemNum.setText(datas.get(position).getMemberLimit() + "人");
            if (datas.get(position).getType() == 1) {
                holder.itemActivityFree.setText("免费");
            } else {
                holder.itemActivityFree.setText("付费");
            }
            holder.itemAddr.setText(datas.get(position).getAddress());
            holder.itemDistance.setText("");// 距离

            // 是否过期
            int i = DateUtils.timeDiff4Mins(datas.get(position).getEndTime());
            Log.i("时间差", String.valueOf(i));
            if (DateUtils.timeDiff4Mins(datas.get(position).getEndTime()) > 0) {
                // 已过期
                holder.btnApply.setText("已过期");
                holder.btnApply.setBackgroundResource(R.drawable.bg_tv_investor_gray);
                holder.btnApply.setClickable(false);
            } else {
                // 未过期
                // 是否已报名
                if (datas.get(position).isAttended()) {
                    holder.btnApply.setText("已报名");
                    holder.btnApply.setBackgroundResource(R.drawable.bg_tv_investor_gray);
                    holder.btnApply.setClickable(false);
                } else {
                    holder.btnApply.setText("立即报名");
                    holder.btnApply.setBackgroundResource(R.drawable.bg_tv_investor_orange);
                    holder.btnApply.setClickable(true);
                    holder.btnApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            applyDialog(datas.get(position).getActionId());
                            POSITION = position;
                        }
                    });
                }
            }
            return convertView;
        }

        public class ViewHolder {
            public TextView tvTitle;
            public TextView itemTime;
            public TextView itemNum;
            public TextView itemActivityFree;
            public TextView itemAddr;
            public TextView btnApply;
            public TextView itemDistance;
        }
    }

    // 获取活动列表
    private class GetActivityListTask extends AsyncTask<Void, Void, ActivityListBean> {
        private int page;

        public GetActivityListTask(int page) {
            this.page = page;
        }

        @Override
        protected ActivityListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETACTIVITYLIST)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETACTIVITYLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("活动列表", body);
                return FastJsonTools.getBean(body, ActivityListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityListBean activityListBean) {
            super.onPostExecute(activityListBean);
            clickable = true;
            if (activityListBean == null) {
//                pageError.setVisibility(View.VISIBLE);
//                refreshView.setVisibility(View.GONE);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (activityListBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = activityListBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.bg_main);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                            // 缓存数据
                            CacheUtils.saveObject(JSON.toJSONString(activityListBean), Constant.CACHE_ACTIVITYS_LIST);
                        }
                    } else {
                        for (ActivityListBean.DataBean dataBean : activityListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (activityListBean.getStatus() == 201) {
//                    pageError.setVisibility(View.GONE);
//                    refreshView.setVisibility(View.VISIBLE);
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
//                    pageError.setVisibility(View.VISIBLE);
//                    refreshView.setVisibility(View.GONE);
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                }
            }
        }
    }

    // 下拉刷新和上拉加载
    private class PullListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新
            isSearch = false;
            pages = 0;
            searchPages = 0;
            edSearch.setText("");
            GetActivityListTask getActivityListTask = new GetActivityListTask(0);
            getActivityListTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            if (isSearch) {
                searchPages++;
                ActivitySearch activitySearch = new ActivitySearch(searchPages);
                activitySearch.execute();
            } else {
                pages++;
                Log.i("页码", String.valueOf(pages));
                GetActivityListTask getActivityListTask = new GetActivityListTask(pages);
                getActivityListTask.execute();
            }
        }
    }

    // 报名弹框
    private void applyDialog(final int contentId) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setView(new EditText(getActivity()));
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_activity_apply);
        RelativeLayout bgEdt = (RelativeLayout) window.findViewById(R.id.dialog_activity_bg_edt);
        final EditText edt = (EditText) window.findViewById(R.id.dialog_activity_edt);
        Button btnCancel = (Button) window.findViewById(R.id.dialog_activity_btn_cancel);
        Button btnConfirm = (Button) window.findViewById(R.id.dialog_activity_btn_confirm);
        // dialog弹出后自动弹出键盘
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               imm.showSoftInput(edt, 0);
                           }
                       },
                100);
        bgEdt.setOnClickListener(new View.OnClickListener() {// 点击输入框背景弹出键盘
            @Override
            public void onClick(View v) {
                imm.showSoftInput(edt, 0);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {// 取消
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {// 确认报名
            @Override
            public void onClick(View v) {
                if (clickable) {
                    clickable = false;
                    ApplyTask applyTask = new ApplyTask(contentId, edt.getText().toString());
                    applyTask.execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                    }
                }, 100);
            }
        });
    }

    // 报名接口
    private class ApplyTask extends AsyncTask<Void, Void, ActivityApplyBean> {
        private int contentId;
        private String content;

        public ApplyTask(int contentId, String content) {
            this.contentId = contentId;
            this.content = content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected ActivityApplyBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.APPLYACTIVITY)),
                            "contentId", String.valueOf(contentId),
                            "content", content,
                            Constant.BASE_URL + Constant.APPLYACTIVITY,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("报名返回信息", body);
                return FastJsonTools.getBean(body, ActivityApplyBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityApplyBean activityApplyBean) {
            super.onPostExecute(activityApplyBean);
            clickable = true;
            dismissProgressDialog();
            if (activityApplyBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (activityApplyBean.getStatus() == 200) {
                    SuperToastUtils.showSuperToast(mContext, 2, activityApplyBean.getMessage());
                    datas.get(POSITION).setAttended(true);
                    myAdapter.notifyDataSetChanged();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityApplyBean.getMessage());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == ActivityDetailActivity.RESULT_CODE) {
                if (data.getBooleanExtra("ISATTENDED", false)) {// 在详情中报了名
                    datas.get(POSITION).setAttended(true);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    // 搜索
    private class ActivitySearch extends AsyncTask<Void, Void, ActivityListBean> {
        private int page;

        public ActivitySearch(int page) {
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 0) {
                showProgressDialog();
            }
        }

        @Override
        protected ActivityListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.ACTIVITYSEARCH)),
                            "page", String.valueOf(page),
                            "keyword", edSearch.getText().toString(),
                            Constant.BASE_URL + Constant.ACTIVITYSEARCH,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("搜索活动列表", body);
                return FastJsonTools.getBean(body, ActivityListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityListBean activityListBean) {
            super.onPostExecute(activityListBean);
            clickable = true;
            dismissProgressDialog();
            if (activityListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (activityListBean.getStatus() == 200) {
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = activityListBean.getData();
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (ActivityListBean.DataBean dataBean : activityListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (activityListBean.getStatus() == 201) {
                    searchPages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, activityListBean.getMessage());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
