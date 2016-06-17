package com.jinzht.pro1.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.CommonBean;
import com.jinzht.pro1.bean.ProjectCommentBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.DateUtils;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 预选项目全部评论
 */
public class PreselectionAllCommentsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 评论列表
    private ImageButton btnComment;// 评论按钮

    private List<ProjectCommentBean.DataBean> datas = new ArrayList<>();// 评论列表
    private int pages = 0;
    private MyAdapter myAdapter;

    private String comment = "";// 输入的评论内容
    private PopupWindow popupWindow;// 评论输入弹框

    public final static int RESULT_CODE = 0;
    public boolean needRefresh = false;// 是否进行了交互，返回时是否刷新

    @Override
    protected int getResourcesId() {
        return R.layout.activity_preselection_all_comments;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("全部评论");
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        listview = (PullableListView) findViewById(R.id.listview);// 评论列表
        btnComment = (ImageButton) findViewById(R.id.btn_comment);// 评论按钮
        btnComment.setOnClickListener(this);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        btnComment.setVisibility(View.GONE);
                    } else {
                        btnComment.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        myAdapter = new MyAdapter();
        GetCommentsTask getCommentsTask = new GetCommentsTask(0);
        getCommentsTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回
                onBackPressed();
                break;
            case R.id.btn_comment:// 评论
                CommentDialog();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_preselection_comment, null);
                holder.ivFavicon = (CircleImageView) convertView.findViewById(R.id.iv_favicon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUsers().getHeadSculpture()).into(holder.ivFavicon);
            holder.tvName.setText(datas.get(position).getUsers().getName());
            holder.tvTime.setText(DateUtils.timeLogic(datas.get(position).getCommentDate()));
            holder.tvContent.setText(datas.get(position).getContent());
            return convertView;
        }

        class ViewHolder {
            private CircleImageView ivFavicon;
            private TextView tvName;
            private TextView tvTime;
            private TextView tvContent;
        }
    }

    // 获取评论列表
    private class GetCommentsTask extends AsyncTask<Void, Void, ProjectCommentBean> {
        int page;

        public GetCommentsTask(int page) {
            this.page = page;
        }

        @Override
        protected ProjectCommentBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTCOMMENTS)),
                            "projectId", getIntent().getStringExtra("id"),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETPROJECTCOMMENTS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("评论列表返回信息", body);
                return FastJsonTools.getBean(body, ProjectCommentBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectCommentBean projectCommentBean) {
            super.onPostExecute(projectCommentBean);
            if (projectCommentBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (projectCommentBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = projectCommentBean.getData();// 评论数据
                        listview.setAdapter(myAdapter);
                    } else {
                        for (ProjectCommentBean.DataBean bean : projectCommentBean.getData()) {
                            datas.add(bean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (projectCommentBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, projectCommentBean.getMessage());
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
            GetCommentsTask getCommentsTask = new GetCommentsTask(0);
            getCommentsTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetCommentsTask getCommentsTask = new GetCommentsTask(pages);
            getCommentsTask.execute();
        }
    }

    // 评论
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
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COMMENTPROJECT)),
                            "projectId", getIntent().getStringExtra("id"),
                            "content", content,
                            Constant.BASE_URL + Constant.COMMENTPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("评论返回信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (commonBean.getStatus() == 200) {
                    popupWindow.dismiss();
                    comment = "";
                    GetCommentsTask getCommentsTask = new GetCommentsTask(0);
                    getCommentsTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    // 弹出评论输入框
    private void CommentDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        final EditText edComment = (EditText) view.findViewById(R.id.ed_comment);
        edComment.setText(comment);
        edComment.setSelection(comment.length());
        TextView btn = (TextView) view.findViewById(R.id.btn_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isBlank(edComment.getText().toString())) {
                    CommentTask commentTask = new CommentTask(edComment.getText().toString());
                    commentTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入评论内容");
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(btnComment, Gravity.BOTTOM, 0, 0);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               imm.showSoftInput(edComment, 0);
                           }
                       },
                100);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!StringUtils.isBlank(edComment.getText().toString())) {
                    comment = edComment.getText().toString();
                } else {
                    comment = "";
                }
                imm.hideSoftInputFromInputMethod(edComment.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
