package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.MessageListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 站内信页面
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private LinearLayout btnEdit;// 编辑按钮
    private TextView tvEdit;// 编辑文字
    private RelativeLayout messageRlBottom;// 底部编辑栏
    private CheckBox messageCbAll;// 全选按钮
    private TextView messageBtnDel;// 删除按钮
    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private boolean clickable;// 是否可编辑标识
    private boolean clickedAll;// 是否全选标识
    private List<MessageListBean.DataBean> datas = new ArrayList<>();
    private List<Boolean> boolList = new ArrayList<>();// 选中item的列表
    private List<String> deleteList = new ArrayList<>();// 要删除的messageId集合
    private int pages = 0;
    private MyAdapter myAdapter;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_message;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnEdit = (LinearLayout) findViewById(R.id.btn_edit);// 编辑按钮
        btnEdit.setOnClickListener(this);
        tvEdit = (TextView) findViewById(R.id.tv_edit);// 编辑文字
        messageRlBottom = (RelativeLayout) findViewById(R.id.message_rl_bottom);// 底部编辑栏
        messageCbAll = (CheckBox) findViewById(R.id.message_cb_all);// 全选按钮
        messageCbAll.setOnClickListener(this);
        messageCbAll.setChecked(false);
        messageBtnDel = (TextView) findViewById(R.id.message_btn_del);// 删除按钮
        messageBtnDel.setOnClickListener(this);
        pageError = (LinearLayout) findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        listview = (PullableListView) findViewById(R.id.listview);// 列表

        myAdapter = new MyAdapter();

        GetMessageListTask getMessageListTask = new GetMessageListTask(0);
        getMessageListTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_edit:// 点击编辑，可批量删除
                if (clickable) {
                    tvEdit.setText("编辑");
                    messageRlBottom.setVisibility(View.GONE);
                    clickable = false;
                } else {
                    tvEdit.setText("完成");
                    messageRlBottom.setVisibility(View.VISIBLE);
                    clickable = true;
                }
                // 将所有item都变为未选中状态
                if (myAdapter.getCount() > 0) {
                    for (int i = 0; i < myAdapter.getCount(); i++) {
                        boolList.set(i, false);
                    }
                }
                messageCbAll.setChecked(false);
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.message_cb_all:// 全选
                if (clickable && myAdapter.getCount() > 0) {
                    if (clickedAll) {
                        for (int i = 0; i < myAdapter.getCount(); i++) {
                            boolList.set(i, false);
                        }
                        messageCbAll.setChecked(false);
                        clickedAll = false;
                    } else {
                        for (int i = 0; i < myAdapter.getCount(); i++) {
                            boolList.set(i, true);
                            Log.i("全选", boolList.toString());
                        }
                        clickedAll = true;
                        messageCbAll.setChecked(true);
                    }
                    myAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.message_btn_del:// 删除选中的条目
                if (clickable && myAdapter.getCount() > 0) {
                    DeleteChecked deleteChecked = new DeleteChecked();
                    deleteChecked.execute();
                    for (int i = 0; i < myAdapter.getCount(); ) {
                        if (boolList.get(i)) {
                            Log.i("删除", datas.get(i).getTitle());
                            datas.remove(i);
                            Log.i("数据", datas.toString());
                            boolList.remove(i);
//                            recyclerAdapter.notifyItemRemoved(i);
                            continue;
                        }
                        i++;
                    }
                    myAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetMessageListTask getMessageListTask = new GetMessageListTask(0);
                    getMessageListTask.execute();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (clickable) {// 编辑状态时退出编辑
            tvEdit.setText("编辑");
            messageRlBottom.setVisibility(View.GONE);
            clickable = false;
            // 将所有item都变为未选中状态
            if (myAdapter.getCount() > 0) {
                for (int i = 0; i < myAdapter.getCount(); i++) {
                    boolList.set(i, false);
                }
            }
            messageCbAll.setChecked(false);
            myAdapter.notifyDataSetChanged();
        } else {// 非编辑状态退出站内信
            finish();
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
                convertView = UiUtils.inflateView(R.layout.item_message);
                holder.ivIsread = (ImageView) convertView.findViewById(R.id.iv_isread);
                holder.cb = (CheckBox) convertView.findViewById(R.id.item_message_cb_select);
                holder.tv_name = (TextView) convertView.findViewById(R.id.item_message_name);
                holder.tv_title = (TextView) convertView.findViewById(R.id.item_message_title);
                holder.tv_content = (TextView) convertView.findViewById(R.id.item_message_content);
                holder.tv_time = (TextView) convertView.findViewById(R.id.item_message_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(datas.get(position).getTitle());
            holder.tv_title.setText(datas.get(position).getMessagetype().getName());
            holder.tv_content.setText(datas.get(position).getContent());
            holder.tv_time.setText(DateUtils.timeLogic(datas.get(position).getMessageDate()));
            if (datas.get(position).isIsRead()) {// 已读
                holder.ivIsread.setVisibility(View.INVISIBLE);
            } else {// 未读
                holder.ivIsread.setVisibility(View.VISIBLE);
            }
            if (clickable) {
                holder.cb.setVisibility(View.VISIBLE);
            } else {
                holder.cb.setVisibility(View.GONE);
            }
            // 给checkBox设置Tag，解决复用出现的bug
            holder.cb.setTag(position);
            if (boolList != null) {
                holder.cb.setChecked((boolList.get(position)));
            } else {
                holder.cb.setChecked(false);
            }
            // checkBox的选中事件
            onChecked(holder);
            final ViewHolder finalHolder = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickable) {// 编辑状态下选中
                        finalHolder.cb.setTag(position);
                        finalHolder.cb.toggle();
                        if (finalHolder.cb.isChecked()) {
                            boolList.set(position, true);
                        } else {
                            boolList.set(position, false);
                        }
                    } else {// 常规状态下跳转至详情
                        if (datas.get(position).getMessagetype().getMessageTypeId() == 1) {
                            if (!StringUtils.isBlank(datas.get(position).getUrl())) {
                                Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                                intent.putExtra("title", datas.get(position).getMessagetype().getName());
                                intent.putExtra("url", datas.get(position).getUrl());
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(mContext, MessageDetail.class);
                            intent.putExtra("title", datas.get(position).getMessagetype().getName());
                            intent.putExtra("name", datas.get(position).getTitle());
                            intent.putExtra("time", datas.get(position).getMessageDate());
                            intent.putExtra("content", datas.get(position).getContent());
                            startActivity(intent);
                        }

                        datas.get(position).setIsRead(true);
                        myAdapter.notifyDataSetChanged();
                        SetIsRead setIsRead = new SetIsRead(datas.get(position).getMessageId());
                        setIsRead.execute();
                    }
                }
            });
            // 长按可编辑，批量删除
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!clickable) {
                        tvEdit.setText("完成");
                        messageRlBottom.setVisibility(View.VISIBLE);
                        clickable = true;
                        // 将所有item都变为未选中状态
                        if (myAdapter.getCount() > 0) {
                            for (int i = 0; i < myAdapter.getCount(); i++) {
                                boolList.set(i, false);
                            }
                        }
                        messageCbAll.setChecked(false);
                        myAdapter.notifyDataSetChanged();
                    }
                    return true;
                }
            });
            return convertView;
        }

        public void onChecked(final ViewHolder holder) {
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        boolList.set((Integer) holder.cb.getTag(), true);
                        Log.i("选中", boolList.toString());
                    } else {
                        boolList.set((Integer) holder.cb.getTag(), false);
                        Log.i("不选中", boolList.toString());
                    }
                }
            });
        }

        public class ViewHolder {
            private CheckBox cb;
            private ImageView ivIsread;
            private TextView tv_name;
            private TextView tv_title;
            private TextView tv_content;
            private TextView tv_time;
        }
    }

    // 获取站内信列表
    private class GetMessageListTask extends AsyncTask<Void, Void, MessageListBean> {
        private int page;

        public GetMessageListTask(int page) {
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
        protected MessageListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETMESSAGELIST)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETMESSAGELIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("站内信列表", body);
                return FastJsonTools.getBean(body, MessageListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(MessageListBean messageListBean) {
            super.onPostExecute(messageListBean);
            clickable = true;
            dismissProgressDialog();
            if (messageListBean == null) {
                pageError.setVisibility(View.VISIBLE);
                refreshView.setVisibility(View.GONE);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (messageListBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = messageListBean.getData();
                        boolList.clear();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.white);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            for (int i = 0; i < datas.size(); i++) {
                                boolList.add(false);
                            }
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (MessageListBean.DataBean dataBean : messageListBean.getData()) {
                            datas.add(dataBean);
                        }
                        for (int i = 0; i < messageListBean.getData().size(); i++) {
                            boolList.add(false);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (messageListBean.getStatus() == 201) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    pageError.setVisibility(View.VISIBLE);
                    refreshView.setVisibility(View.GONE);
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                }
            }
        }
    }

    // 下拉刷新与上拉加载
    private class PullListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新
            pages = 0;
            GetMessageListTask getMessageListTask = new GetMessageListTask(0);
            getMessageListTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetMessageListTask getMessageListTask = new GetMessageListTask(pages);
            getMessageListTask.execute();
        }
    }

    // 删除已选
    private class DeleteChecked extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            for (int i = 0; i < boolList.size(); i++) {
                if (boolList.get(i)) {
                    deleteList.add(String.valueOf(datas.get(i).getMessageId()));
                }
            }
            Log.i("要删除的id", deleteList.toString());
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    String messageId = deleteList.toString();
                    messageId = messageId.substring(1, messageId.length() - 1);
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.DELETECHECKED)),
                            "messageId", messageId,
                            Constant.BASE_URL + Constant.DELETECHECKED,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("删除已选", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            deleteList.clear();
        }
    }

    // 标记为已读
    private class SetIsRead extends AsyncTask<Void, Void, CommonBean> {
        private int messageId;

        public SetIsRead(int messageId) {
            this.messageId = messageId;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SETISREAD)),
                            "messageId", String.valueOf(messageId),
                            Constant.BASE_URL + Constant.SETISREAD,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("标记为已读", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
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
