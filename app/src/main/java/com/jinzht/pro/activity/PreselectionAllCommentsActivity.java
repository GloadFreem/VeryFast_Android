package com.jinzht.pro.activity;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.ProjectCommentBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

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
    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
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
        pageError = (LinearLayout) findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datas.get(position).getUsers().getUserId() == SharedPreferencesUtils.getUserId(mContext)) {
                    // 弹框提示删除
                    showDeleteWindow(view, position);
                }
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
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetCommentsTask getCommentsTask = new GetCommentsTask(0);
                    getCommentsTask.execute();
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
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 0) {
                showProgressDialog();
            }
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
            clickable = true;
            dismissProgressDialog();
            if (projectCommentBean == null) {
                pageError.setVisibility(View.VISIBLE);
                refreshView.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (projectCommentBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    btnComment.setVisibility(View.VISIBLE);
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = projectCommentBean.getData();// 评论数据
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.white);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (ProjectCommentBean.DataBean bean : projectCommentBean.getData()) {
                            datas.add(bean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (projectCommentBean.getStatus() == 201) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    btnComment.setVisibility(View.VISIBLE);
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    pageError.setVisibility(View.VISIBLE);
                    refreshView.setVisibility(View.GONE);
                    btnComment.setVisibility(View.GONE);
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
            clickable = true;
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    needRefresh = true;
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
                    if (clickable) {
                        clickable = false;
                        CommentTask commentTask = new CommentTask(edComment.getText().toString());
                        commentTask.execute();
                    }
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
                imm.hideSoftInputFromWindow(edComment.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    // 删除评论弹窗
    private void showDeleteWindow(View view, final int position) {
        ImageButton button = new ImageButton(mContext);
        button.setBackgroundResource(R.mipmap.icon_delete);
        final PopupWindow popupWindow = new PopupWindow(button, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        view.getLocationInWindow(location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickable) {
                    clickable = false;
                    DeleteCommentTask deleteCommentTask = new DeleteCommentTask(datas.get(position).getCommentId());
                    deleteCommentTask.execute();
                    datas.remove(position);
                    myAdapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - UiUtils.dip2px(34), location[1] - UiUtils.dip2px(22));
    }

    // 删除项目评论
    private class DeleteCommentTask extends AsyncTask<Void, Void, CommonBean> {
        private int commentId;

        public DeleteCommentTask(int commentId) {
            this.commentId = commentId;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.DELETEPROJECTCOMMENT)),
                            "commentId", String.valueOf(commentId),
                            Constant.BASE_URL + Constant.DELETEPROJECTCOMMENT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("删除评论", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            clickable = true;
            if (commonBean != null) {
                Log.i("删除评论完成", commonBean.getMessage());
                if (commonBean.getStatus() == 200) {
                    needRefresh = true;
                }
            }
        }
    }
}
