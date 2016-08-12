package com.jinzht.pro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommentsListBean;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 路演项目中全屏交流
 */
public class RoadshowChatActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edChat;// 输入框
    private TextView btnSend;// 发送按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 聊天列表

    private int pages = 0;
    private List<CommentsListBean.DataBean> datas = new ArrayList<>();// 聊天内容数据
    private ChatMsgAdapter msgAdapter;// 聊天内容适配器
    private Handler handler;// 用于循环获取聊天内容

    private InputMethodManager imm;// 控制键盘

    public final static int RESULT_CODE = 0;
    public boolean needRefresh = false;// 是否进行了交互，返回时是否刷新

    @Override
    protected int getResourcesId() {
        return R.layout.activity_roadshow_chat;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("互动交流");
        edChat = (EditText) findViewById(R.id.ed_chat);// 输入框
        btnSend = (TextView) findViewById(R.id.btn_send);// 发送按钮
        btnSend.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) findViewById(R.id.listview);// 聊天列表

        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口

        msgAdapter = new ChatMsgAdapter();
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);

        handler = new Handler();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        GetCommentsListTask getCommentsListTask = new GetCommentsListTask(0);
        getCommentsListTask.execute();

        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回
                onBackPressed();
                break;
            case R.id.btn_send:// 发送
                if (!StringUtils.isBlank(edChat.getText().toString())) {
                    if (clickable) {
                        clickable = false;
                        CommentTask commentTask = new CommentTask(edChat.getText().toString());
                        commentTask.execute();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入聊天内容");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (needRefresh) {
            Intent intent = new Intent();
            intent.putExtra("needRefresh", needRefresh);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }

    private class ChatMsgAdapter extends BaseAdapter {

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
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (datas.get(position).isFlag()) {
                return 0;// flag = true，自己说的
            } else {
                return 1;// flag = false，别人说的
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                if (getItemViewType(position) == 0) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_roadshow_chat_right, null);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_roadshow_chat_left, null);
                }
                holder.itemChatFavicon = (CircleImageView) convertView.findViewById(R.id.item_chat_favicon);
                holder.itemChatName = (TextView) convertView.findViewById(R.id.item_chat_name);
                holder.itemChatMsg = (TextView) convertView.findViewById(R.id.item_chat_msg);
                holder.itemChatTime = (TextView) convertView.findViewById(R.id.item_chat_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUsers().getHeadSculpture()).into(holder.itemChatFavicon);
            holder.itemChatName.setText(datas.get(position).getUsers().getName());
            holder.itemChatMsg.setText(datas.get(position).getContent());
            if (DateUtils.timeDiff4Mins(datas.get(position).getCommentDate()) < 2) {
                holder.itemChatTime.setVisibility(View.GONE);
            } else {
                holder.itemChatTime.setVisibility(View.VISIBLE);
                holder.itemChatTime.setText(DateUtils.timeLogic(datas.get(position).getCommentDate()));
            }
            return convertView;
        }

        class ViewHolder {
            public CircleImageView itemChatFavicon;
            public TextView itemChatName;
            public TextView itemChatMsg;
            public TextView itemChatTime;
        }
    }

    // 5秒获取一次聊天内容
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GetCommentsListTask getCommentsListTask = new GetCommentsListTask(0);
            getCommentsListTask.execute();
            handler.postDelayed(this, 5000);
        }
    };

    // 获取聊天内容
    private class GetCommentsListTask extends AsyncTask<Void, Void, CommentsListBean> {
        private int page;

        public GetCommentsListTask(int page) {
            this.page = page;
        }

        @Override
        protected CommentsListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETCOMMENTSLIST)),
                            "sceneId", String.valueOf(RoadshowDetailsActivity.voiceData.getSceneId()),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETCOMMENTSLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("交流列表" + page, body);
                return FastJsonTools.getBean(body, CommentsListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommentsListBean commentsListBean) {
            super.onPostExecute(commentsListBean);
            if (commentsListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (commentsListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        List<CommentsListBean.DataBean> tempList = commentsListBean.getData();
                        Collections.reverse(tempList);
                        datas = tempList;
                        listview.setAdapter(msgAdapter);
                        listview.setSelection(msgAdapter.getCount());
                    } else {
                        for (CommentsListBean.DataBean dataBean : commentsListBean.getData()) {
                            datas.add(0, dataBean);
                        }
                        msgAdapter.notifyDataSetChanged();
                        listview.setSelection(commentsListBean.getData().size() + 1);
                    }
                } else if (commentsListBean.getStatus() == 201) {
                    pages--;
                    refreshView.refreshFinish(PullToRefreshLayout.FIRST);// 告诉控件刷新到最新一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, commentsListBean.getMessage());
                }
            }
        }
    }

    // 下拉刷新与上拉加载
    private class PullListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetCommentsListTask getCommentsListTask = new GetCommentsListTask(pages);
            getCommentsListTask.execute();

        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉刷新
            pages = 0;
            GetCommentsListTask getCommentsListTask = new GetCommentsListTask(0);
            getCommentsListTask.execute();
        }
    }

    // 交流评论
    private class CommentTask extends AsyncTask<Void, Void, CommonBean> {
        private String content;

        public CommentTask(String content) {
            this.content = content;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SCENECOMMENT)),
                            "sceneId", String.valueOf(RoadshowDetailsActivity.voiceData.getSceneId()),
                            "content", content,
                            Constant.BASE_URL + Constant.SCENECOMMENT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("发表评论", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            clickable = true;
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    needRefresh = true;
                    edChat.setText("");
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(edChat.getWindowToken(), 0);
                    }

                    GetCommentsListTask getCommentsListTask = new GetCommentsListTask(0);
                    getCommentsListTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}
