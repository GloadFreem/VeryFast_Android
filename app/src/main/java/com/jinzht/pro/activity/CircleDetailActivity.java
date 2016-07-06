package com.jinzht.pro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jinzht.pro.adapter.CirclePhotosAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CircleCommentBean;
import com.jinzht.pro.bean.CircleDetailBean;
import com.jinzht.pro.bean.CircleMoreCommentsBean;
import com.jinzht.pro.bean.CirclePriseBean;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.callback.ItemClickListener;
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

/**
 * 圈子详情页
 */
public class CircleDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout llComment;// 评论输入框布局
    private EditText edComment;// 评论输入框
    private TextView btnComment;// 评论按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private ListAdapter listAdapter;// 列表数据填充器
    private CircleDetailBean.DataBean datas;// 获取到的数据
    private List<CircleDetailBean.DataBean.CommentsBean> comments = new ArrayList<>();// 评论列表
    private int pages = 0;

    private int FLAG = 0;// 点赞或取消点赞的标识
    private int needRefresh = 0;// 是否需要在圈子列表中刷新
    public final static int RESULT_CODE = 0;
    private InputMethodManager imm;// 键盘控制

    @Override

    protected int getResourcesId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        refreshView.setOnRefreshListener(new PullListener());
        initListView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getIntent().getIntExtra("TAG", 0) == 0) {// 详情
                    imm.hideSoftInputFromWindow(edComment.getWindowToken(), 0);
                } else {
                    imm.showSoftInput(edComment, 0);
                }
            }
        }, 80);
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("详情");
        llComment = (LinearLayout) findViewById(R.id.ll_comment);// 评论输入框布局
        edComment = (EditText) findViewById(R.id.ed_comment);// 评论输入框
        btnComment = (TextView) findViewById(R.id.btn_comment);// 评论按钮
        btnComment.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) findViewById(R.id.listview);// 列表
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);// 键盘控制
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                onBackPressed();
                break;
            case R.id.btn_comment:// 发表评论
                if ("已认证".equals(SharedPreferencesUtils.getIsAuthentic(mContext))) {
                    if (StringUtils.isBlank(edComment.getText().toString())) {
                        SuperToastUtils.showSuperToast(this, 2, "请输入评论内容");
                    } else {
                        if ("评论本条状态".equals(edComment.getHint())) {
                            CircleCommentTask circleCommentTask = new CircleCommentTask("");
                            circleCommentTask.execute();
                        } else {
                            CircleCommentTask circleCommentTask = new CircleCommentTask(String.valueOf(comments.get((Integer) edComment.getTag() - 1).getUsersByUserId().getUserId()));
                            circleCommentTask.execute();
                        }
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, "您还没有进行实名认证，请先实名认证");
                    Intent intent = new Intent();
                    if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
                        intent.setClass(mContext, WechatVerifyActivity.class);
                    } else {
                        intent.setClass(mContext, CertificationIDCardActivity.class);
                    }
                    intent.putExtra("usertype", SharedPreferencesUtils.getUserType(mContext));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (needRefresh % 2 != 0 && FLAG != 0) {
            Intent intent = new Intent();
            intent.putExtra("FLAG", FLAG);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }

    private void initListView() {
        listAdapter = new ListAdapter();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (comments.get(position - 1).getUsersByUserId().getUserId() == SharedPreferencesUtils.getUserId(mContext)) {
                    // 弹框提示删除
                    showDeleteWindow(view, position - 1);
                } else {
                    imm.showSoftInput(edComment, 0);
                    edComment.setHint("回复 " + comments.get(position - 1).getUsersByUserId().getName());
                    edComment.setTag(position);
                }
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                imm.hideSoftInputFromWindow(edComment.getWindowToken(), 0);
                edComment.setHint("评论本条状态");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        GetCircleDetailTask getCircleDetailTask = new GetCircleDetailTask();
        getCircleDetailTask.execute();
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    private class ListAdapter extends BaseAdapter {

        private boolean isOpen = false;// 查看全文的开关状态

        @Override
        public int getCount() {
            if (comments != null) {
                return comments.size() + 1;
            }
            return 1;
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
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_circle_detail, null);
                    holder.ivDetailFavicon = (CircleImageView) convertView.findViewById(R.id.iv_favicon);
                    holder.tvDetailName = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tvDetailAddr = (TextView) convertView.findViewById(R.id.tv_addr);
                    holder.tvDetailCompPosition = (TextView) convertView.findViewById(R.id.tv_comp_position);
                    holder.tvDetailTime = (TextView) convertView.findViewById(R.id.tv_time);
                    holder.tvDetailContent = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.recyclerview = (RecyclerView) convertView.findViewById(R.id.recyclerview);
                    holder.btnGood = (ImageView) convertView.findViewById(R.id.btn_good);
                    holder.tvGood = (TextView) convertView.findViewById(R.id.tv_good);
                    holder.btnAll = (TextView) convertView.findViewById(R.id.btn_all);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_circle_comment, null);
                    holder.ivFavicon = (CircleImageView) convertView.findViewById(R.id.iv_favicon);
                    holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 处理详情
            if (getItemViewType(position) == 0) {
                Glide.with(mContext).load(datas.getUsers().getHeadSculpture()).into(holder.ivDetailFavicon);
                holder.tvDetailName.setText(datas.getUsers().getName());
                holder.tvDetailAddr.setText(datas.getUsers().getAuthentics().get(0).getCity().getName());
                if (!StringUtils.isBlank(datas.getUsers().getAuthentics().get(0).getCompanyName()) && !StringUtils.isBlank(datas.getUsers().getAuthentics().get(0).getPosition())) {
                    holder.tvDetailCompPosition.setText(datas.getUsers().getAuthentics().get(0).getCompanyName() + " | " + datas.getUsers().getAuthentics().get(0).getPosition());
                }
                holder.tvDetailTime.setText(DateUtils.timeLogic(datas.getPublicDate()));
                holder.tvDetailContent.setText(datas.getContent());

                // 图片
                final ArrayList<String> urls = new ArrayList<>();
                for (CircleDetailBean.DataBean.ContentimagesesBean bean : datas.getContentimageses()) {
                    urls.add(bean.getUrl());
                }
                CirclePhotosAdapter photosAdapter = new CirclePhotosAdapter(mContext, urls);
                RecyclerViewData.setGrid(holder.recyclerview, mContext, photosAdapter, 3);
                photosAdapter.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        imageBrower(position, urls);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                    @Override
                    public void onItemSubViewClick(View view, int position) {

                    }
                });

                // 点赞
                final ViewHolder finalHolder = holder;
                holder.btnGood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("已认证".equals(SharedPreferencesUtils.getIsAuthentic(mContext))) {
                            if (datas.isFlag()) {
                                finalHolder.btnGood.setImageResource(R.mipmap.icon_good);
                                datas.setFlag(false);
                                CirclePriseTask circlePriseTask = new CirclePriseTask(datas.getPublicContentId(), 2);
                                circlePriseTask.execute();
                            } else {
                                finalHolder.btnGood.setImageResource(R.mipmap.icon_good_checked);
                                datas.setFlag(true);
                                CirclePriseTask circlePriseTask = new CirclePriseTask(datas.getPublicContentId(), 1);
                                circlePriseTask.execute();
                            }
                        } else {
                            SuperToastUtils.showSuperToast(mContext, 2, "您还没有进行实名认证，请先实名认证");
                            Intent intent = new Intent();
                            if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
                                intent.setClass(mContext, WechatVerifyActivity.class);
                            } else {
                                intent.setClass(mContext, CertificationIDCardActivity.class);
                            }
                            intent.putExtra("usertype", SharedPreferencesUtils.getUserType(mContext));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                if (datas.isFlag()) {
                    holder.btnGood.setImageResource(R.mipmap.icon_good_checked);
                } else {
                    holder.btnGood.setImageResource(R.mipmap.icon_good);
                }
                ArrayList<String> names = new ArrayList<>();
                for (CircleDetailBean.DataBean.ContentprisesBean bean : datas.getContentprises()) {
                    names.add(bean.getUsers().getName());
                }
                holder.tvGood.setText(names.toString().substring(1, names.toString().length() - 1));

                // 查看全部按钮
                if (holder.tvGood.getLineCount() > 3) {
                    holder.tvGood.setEllipsize(TextUtils.TruncateAt.END);
                    holder.tvGood.setMaxLines(3);
                    holder.btnAll.setVisibility(View.VISIBLE);
                } else {
                    holder.tvGood.setEllipsize(null);
                    holder.tvGood.setMaxLines(Integer.MAX_VALUE);
                    holder.btnAll.setVisibility(View.GONE);
                }
                holder.btnAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOpen) {// 打开状态，点击关闭
                            finalHolder.tvGood.setEllipsize(TextUtils.TruncateAt.END);
                            finalHolder.tvGood.setMaxLines(3);
                            finalHolder.btnAll.setText("全部");
                            isOpen = false;
                        } else {// 关闭状态，点击打开
                            finalHolder.tvGood.setEllipsize(null);
                            finalHolder.tvGood.setMaxLines(Integer.MAX_VALUE);
                            finalHolder.btnAll.setText("闭合");
                            isOpen = true;
                        }
                    }
                });

                // 处理评论
            } else {
                if (comments.get(position - 1).getUsersByAtUserId() != null) {// 有回复
                    Glide.with(mContext).load(comments.get(position - 1).getUsersByUserId().getHeadSculpture()).into(holder.ivFavicon);
                    holder.tvName.setText(comments.get(position - 1).getUsersByUserId().getName() + " 回复 " + comments.get(position - 1).getUsersByAtUserId().getName());
                } else {
                    Glide.with(mContext).load(comments.get(position - 1).getUsersByUserId().getHeadSculpture()).into(holder.ivFavicon);
                    holder.tvName.setText(comments.get(position - 1).getUsersByUserId().getName());
                }
                holder.tvTime.setText(DateUtils.timeLogic(comments.get(position - 1).getPublicDate()));
                holder.tvContent.setText(comments.get(position - 1).getContent());
            }

            return convertView;
        }

        public class ViewHolder {
            public CircleImageView ivDetailFavicon;
            public TextView tvDetailName;
            public TextView tvDetailAddr;
            public TextView tvDetailCompPosition;
            public TextView tvDetailTime;
            public TextView tvDetailContent;
            public RecyclerView recyclerview;
            public ImageView btnGood;
            public TextView tvGood;
            public TextView btnAll;

            public CircleImageView ivFavicon;
            public TextView tvName;
            public TextView tvTime;
            public TextView tvContent;
        }

        private void imageBrower(int position, ArrayList<String> urls) {
            Intent intent = new Intent(mContext, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    // 首次获取详情信息和刷新
    private class GetCircleDetailTask extends AsyncTask<Void, Void, CircleDetailBean> {

        @Override
        protected CircleDetailBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETCIRCLEDETAIL)),
                            "feelingId", getIntent().getStringExtra("id"),
                            "page", String.valueOf(0),
                            Constant.BASE_URL + Constant.GETCIRCLEDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("圈子详情", body);
                return FastJsonTools.getBean(body, CircleDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CircleDetailBean circleDetailBean) {
            super.onPostExecute(circleDetailBean);
            if (circleDetailBean == null) {
                listview.setBackgroundResource(R.mipmap.bg_empty);
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (circleDetailBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    datas = circleDetailBean.getData();
                    comments = datas.getComments();
                    if (datas != null) {
                        listview.setBackgroundResource(R.color.bg_main);
                    } else {
                        listview.setBackgroundResource(R.mipmap.bg_empty);
                    }
                    listview.setAdapter(listAdapter);
                } else {
                    listview.setBackgroundResource(R.mipmap.bg_empty);
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, circleDetailBean.getMessage());
                }
            }
        }
    }

    // 加载更多评论
    private class GetMoreCommentsTask extends AsyncTask<Void, Void, CircleMoreCommentsBean> {
        private int page;

        public GetMoreCommentsTask(int page) {
            this.page = page;
        }

        @Override
        protected CircleMoreCommentsBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETCIRCLEDETAIL)),
                            "feelingId", getIntent().getStringExtra("id"),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETCIRCLEDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("更多评论", body);
                return FastJsonTools.getBean(body, CircleMoreCommentsBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CircleMoreCommentsBean circleMoreCommentsBean) {
            super.onPostExecute(circleMoreCommentsBean);
            if (circleMoreCommentsBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (circleMoreCommentsBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功

                    for (CircleMoreCommentsBean.DataBean bean : circleMoreCommentsBean.getData()) {

                        CircleDetailBean.DataBean.CommentsBean commentsBean = new CircleDetailBean.DataBean.CommentsBean();
                        CircleDetailBean.DataBean.CommentsBean.UsersByAtUserIdBean usersByAtUserId = new CircleDetailBean.DataBean.CommentsBean.UsersByAtUserIdBean();
                        CircleDetailBean.DataBean.CommentsBean.UsersByUserIdBean usersByUserIdBean = new CircleDetailBean.DataBean.CommentsBean.UsersByUserIdBean();

                        if (bean.getUsersByUserId() != null) {
                            usersByUserIdBean.setUserId(bean.getUsersByUserId().getUserId());
                            usersByUserIdBean.setName(bean.getUsersByUserId().getName());
                            usersByUserIdBean.setHeadSculpture(bean.getUsersByUserId().getHeadSculpture());
                        } else {
                            usersByUserIdBean = null;
                        }

                        if (bean.getUsersByAtUserId() != null) {
                            usersByAtUserId.setUserId(bean.getUsersByAtUserId().getUserId());
                            usersByAtUserId.setName(bean.getUsersByAtUserId().getName());
                            usersByAtUserId.setHeadSculpture(bean.getUsersByAtUserId().getHeadSculpture());
                        } else {
                            usersByAtUserId = null;
                        }

                        commentsBean.setCommentId(bean.getCommentId());
                        commentsBean.setContent(bean.getContent());
                        commentsBean.setPublicDate(bean.getPublicDate());
                        commentsBean.setUsersByUserId(usersByUserIdBean);
                        commentsBean.setUsersByAtUserId(usersByAtUserId);

                        comments.add(commentsBean);
                    }
                    Log.i("评论", comments.toString());
                    listAdapter.notifyDataSetChanged();
                } else if (circleMoreCommentsBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, circleMoreCommentsBean.getMessage());
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
            GetCircleDetailTask getCircleDetailTask = new GetCircleDetailTask();
            getCircleDetailTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetMoreCommentsTask getMoreCommentsTask = new GetMoreCommentsTask(pages);
            getMoreCommentsTask.execute();
        }
    }

    // 点赞
    private class CirclePriseTask extends AsyncTask<Void, Void, CirclePriseBean> {
        private int id;
        private int flag;

        public CirclePriseTask(int id, int flag) {
            this.id = id;
            this.flag = flag;
        }

        @Override
        protected CirclePriseBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.PRISECIRCLE)),
                            "contentId", String.valueOf(id),
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.PRISECIRCLE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("点赞返回值", body);
                return FastJsonTools.getBean(body, CirclePriseBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CirclePriseBean circlePriseBean) {
            super.onPostExecute(circlePriseBean);
            if (circlePriseBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (circlePriseBean.getStatus() == 200) {
                    List<CircleDetailBean.DataBean.ContentprisesBean> contentprises = datas.getContentprises();
                    if (flag == 1) {
                        CircleDetailBean.DataBean.ContentprisesBean bean = new CircleDetailBean.DataBean.ContentprisesBean();
                        CircleDetailBean.DataBean.ContentprisesBean.UsersBean users = new CircleDetailBean.DataBean.ContentprisesBean.UsersBean();
                        users.setName(circlePriseBean.getData().getName());
                        bean.setUsers(users);
                        contentprises.add(0, bean);
                        FLAG = 1;
                    } else {
                        for (CircleDetailBean.DataBean.ContentprisesBean contentprisesBean : contentprises) {
                            if (circlePriseBean.getData().getName().equals(contentprisesBean.getUsers().getName())) {
                                contentprises.remove(contentprisesBean);
                                break;
                            }
                        }
                        FLAG = 2;
                    }
                    needRefresh++;
                    listAdapter.notifyDataSetChanged();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, circlePriseBean.getMessage());
                }
            }
        }
    }

    // 评论
    private class CircleCommentTask extends AsyncTask<Void, Void, CircleCommentBean> {
        private String atUserId;

        public CircleCommentTask(String atUserId) {
            this.atUserId = atUserId;
        }

        @Override
        protected CircleCommentBean doInBackground(Void... params) {
            String body = "";
            int flag;
            if (StringUtils.isBlank(atUserId)) {
                flag = 1;// 评论
            } else {
                flag = 2;// 回复
            }
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COMMENTCIRCLE)),
                            "contentId", String.valueOf(datas.getPublicContentId()),
                            "content", edComment.getText().toString(),
                            "atUserId", atUserId,
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.COMMENTCIRCLE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("评论返回信息", body);
                return FastJsonTools.getBean(body, CircleCommentBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CircleCommentBean circleCommentBean) {
            super.onPostExecute(circleCommentBean);
            if (circleCommentBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (circleCommentBean.getStatus() == 200) {
                    imm.hideSoftInputFromWindow(edComment.getWindowToken(), 0);
                    edComment.setText("");
                    GetCircleDetailTask getCircleDetailTask = new GetCircleDetailTask();
                    getCircleDetailTask.execute();
                    listview.smoothScrollToPosition(1);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, circleCommentBean.getMessage());
                }
            }
        }
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
                DeleteCommentTask deleteCommentTask = new DeleteCommentTask(comments.get(position).getCommentId());
                deleteCommentTask.execute();
                comments.remove(position);
                listAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - UiUtils.dip2px(34), location[1] - UiUtils.dip2px(22));
    }

    // 删除圈子评论
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
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.DELETECIRCLECOMMENT)),
                            "commentId", String.valueOf(commentId),
                            Constant.BASE_URL + Constant.DELETECIRCLECOMMENT,
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
            if (commonBean != null) {
                Log.i("删除评论完成", commonBean.getMessage());
            }
        }
    }
}
