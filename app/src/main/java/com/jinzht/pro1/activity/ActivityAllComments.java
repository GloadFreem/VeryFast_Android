package com.jinzht.pro1.activity;

import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.ActivityAllCommentsBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动全部评论点赞人
 */
public class ActivityAllComments extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表
    private LinearLayout llBtn;// 按钮布局
    private ImageButton btnPraise;// 点赞按钮
    private ImageButton btnComment;// 评论按钮

    private List<String> prises = new ArrayList<>();//  全部点赞人
    private List<ActivityAllCommentsBean.DataBean.CommentsBean> comments = new ArrayList<>();// 全部评论人
    private int pages = 0;
    private MyAdapter myAdapter;

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体

    @Override
    protected int getResourcesId() {
        return R.layout.activity_activity_all_comments;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("点赞评论");
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        listview = (PullableListView) findViewById(R.id.listview);// 列表
        llBtn = (LinearLayout) findViewById(R.id.ll_btn);// 按钮布局
        btnPraise = (ImageButton) findViewById(R.id.btn_praise);// 点赞按钮
        btnPraise.setOnClickListener(this);
        btnComment = (ImageButton) findViewById(R.id.btn_comment);// 评论按钮
        btnComment.setOnClickListener(this);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        llBtn.setVisibility(View.GONE);
                    } else {
                        llBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        myAdapter = new MyAdapter();
        GetAllCommentsTask getAllCommentsTask = new GetAllCommentsTask(0);
        getAllCommentsTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回
                finish();
                break;
            case R.id.btn_praise:// 点赞

                break;
            case R.id.btn_comment:// 评论

                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return comments.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
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
            if (position == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                if (getItemViewType(position) == 0) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activity_prise, null);
                    holder.tvPraise = (TextView) convertView.findViewById(R.id.tv_praise);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activity_comment, null);
                    holder.ivComment = (ImageView) convertView.findViewById(R.id.iv_comment);
                    holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 处理点赞人
            if (getItemViewType(position) == 0) {
                holder.tvPraise.setText(prises.toString().substring(1, prises.toString().length() - 1));
            } else {
                if (position == 1) {
                    holder.ivComment.setVisibility(View.VISIBLE);
                } else {
                    holder.ivComment.setVisibility(View.INVISIBLE);
                }
                if (StringUtils.isBlank(comments.get(position - 1).getAtUserName())) {
                    String name = comments.get(position - 1).getUserName();
                    str = name + ": " + comments.get(position - 1).getContent();
                    span = new SpannableString(str);
                    span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tvComment.setText(span);
                } else {
                    String name = comments.get(position - 1).getUserName();
                    String atName = comments.get(position - 1).getAtUserName();
                    str = name + " 回复 " + atName + ": " + comments.get(position - 1).getContent();
                    span = new SpannableString(str);
                    span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tvComment.setText(span);
                }
            }
            return convertView;
        }

        class ViewHolder {
            private TextView tvPraise;// 点赞人
            private ImageView ivComment;// 评论图标
            private TextView tvComment;// 评论内容
        }
    }

    // 获取全部评论人
    private class GetAllCommentsTask extends AsyncTask<Void, Void, ActivityAllCommentsBean> {
        int page;

        public GetAllCommentsTask(int page) {
            this.page = page;
        }

        @Override
        protected ActivityAllCommentsBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETALLACTIVITYCOMMENTS)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETALLACTIVITYCOMMENTS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("全部评论人", body);
                return FastJsonTools.getBean(body, ActivityAllCommentsBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityAllCommentsBean activityAllCommentsBean) {
            super.onPostExecute(activityAllCommentsBean);
            if (activityAllCommentsBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (activityAllCommentsBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        prises = activityAllCommentsBean.getData().getPrises();// 点赞数据
                        comments = activityAllCommentsBean.getData().getComments();// 评论数据
                        listview.setAdapter(myAdapter);
                    } else {
                        for (ActivityAllCommentsBean.DataBean.CommentsBean bean : activityAllCommentsBean.getData().getComments()) {
                            comments.add(bean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (activityAllCommentsBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, activityAllCommentsBean.getMessage());
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
            GetAllCommentsTask getAllCommentsTask = new GetAllCommentsTask(0);
            getAllCommentsTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetAllCommentsTask getAllCommentsTask = new GetAllCommentsTask(pages);
            getAllCommentsTask.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

}
