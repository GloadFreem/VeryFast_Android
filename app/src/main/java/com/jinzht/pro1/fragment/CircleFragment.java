package com.jinzht.pro1.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro1.R;
import com.jinzht.pro1.activity.CircleDetailActivity;
import com.jinzht.pro1.activity.ImagePagerActivity;
import com.jinzht.pro1.activity.ReleaseCircleActivity;
import com.jinzht.pro1.adapter.CirclePhotosAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.CircleListBean;
import com.jinzht.pro1.bean.CirclePriseBean;
import com.jinzht.pro1.bean.ShareBean;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.DialogUtils;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.ShareUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.CircleImageView;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子界面
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout titleBtnRight;// title右侧按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private int pages = 0;
    private List<CircleListBean.DataBean> datas = new ArrayList<>();// 数据集合
    private MyAdapter myAdapter;

    private final static int REQUEST_CODE = 1;
    private int POSITION = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        titleBtnRight = (LinearLayout) view.findViewById(R.id.title_btn_right);// title右侧按钮
        titleBtnRight.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.listview);// 列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        myAdapter = new MyAdapter();
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null));

        GetCircleListTask getCircleList = new GetCircleListTask(0);
        getCircleList.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_right:// 点击发表内容
                Intent intent = new Intent(mContext, ReleaseCircleActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        private boolean isOpen = false;// 查看全文的开关状态

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_circle, null);
                holder.ivFavicon = (CircleImageView) convertView.findViewById(R.id.iv_favicon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvAddr = (TextView) convertView.findViewById(R.id.tv_addr);
                holder.tvCompPosition = (TextView) convertView.findViewById(R.id.tv_comp_position);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.btnMore = (TextView) convertView.findViewById(R.id.btn_more);
                holder.recyclerview = (RecyclerView) convertView.findViewById(R.id.recyclerview);
                holder.btnTranspond = (RelativeLayout) convertView.findViewById(R.id.btn_transpond);
                holder.tvTranspond = (TextView) convertView.findViewById(R.id.tv_transpond);
                holder.btnComment = (RelativeLayout) convertView.findViewById(R.id.btn_comment);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                holder.btnGood = (RelativeLayout) convertView.findViewById(R.id.btn_good);
                holder.tvGood = (TextView) convertView.findViewById(R.id.tv_good);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUsers().getHeadSculpture()).into(holder.ivFavicon);
            holder.tvName.setText(datas.get(position).getUsers().getName());
            holder.tvAddr.setText(datas.get(position).getUsers().getAuthentics().get(0).getCity().getName());
            String companyName = datas.get(position).getUsers().getAuthentics().get(0).getCompanyName();
            String pos = datas.get(position).getUsers().getAuthentics().get(0).getPosition();
            holder.tvCompPosition.setText(companyName + " | " + pos);
            holder.tvTime.setText(com.jinzht.pro1.utils.DateUtils.timeLogic(datas.get(position).getPublicDate()));

            // 内容
            holder.tvContent.setText(datas.get(position).getContent());
            if (!StringUtils.isBlank(datas.get(position).getContent())) {
                int contentLength = datas.get(position).getContent().length();
                if (contentLength > 66) {
                    holder.tvContent.setEllipsize(TextUtils.TruncateAt.END);
                    holder.tvContent.setMaxLines(3);
                    holder.btnMore.setVisibility(View.VISIBLE);
                } else {
                    holder.tvContent.setEllipsize(null);
                    holder.tvContent.setMaxLines(Integer.MAX_VALUE);
                    holder.btnMore.setVisibility(View.GONE);
                }
            } else {
                holder.tvContent.setEllipsize(null);
                holder.tvContent.setMaxLines(Integer.MAX_VALUE);
                holder.btnMore.setVisibility(View.GONE);
            }
            final ViewHolder finalHolder = holder;
            holder.btnMore.setOnClickListener(new View.OnClickListener() {// 点击查看全文
                @Override
                public void onClick(View v) {
                    if (isOpen) {// 打开状态，点击关闭
                        finalHolder.tvContent.setEllipsize(TextUtils.TruncateAt.END);
                        finalHolder.tvContent.setMaxLines(3);
                        finalHolder.btnMore.setText("全文");
                        isOpen = false;
                    } else {// 关闭状态，点击打开
                        finalHolder.tvContent.setEllipsize(null);
                        finalHolder.tvContent.setMaxLines(Integer.MAX_VALUE);
                        finalHolder.btnMore.setText("闭合");
                        isOpen = true;
                    }
                }
            });

            // 图片
            final ArrayList<String> urls = new ArrayList<>();
            for (CircleListBean.DataBean.ContentimagesesBean bean : datas.get(position).getContentimageses()) {
                urls.add(bean.getUrl());
            }
            CirclePhotosAdapter photosAdapter = new CirclePhotosAdapter(mContext, urls);
            RecyclerViewData.setGrid(holder.recyclerview, mContext, photosAdapter, 3);
            photosAdapter.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.i("获取的图片地址", urls.toString());
                    imageBrower(position, urls);
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }

                @Override
                public void onItemSubViewClick(View view, int position) {

                }
            });

            // 转发
            holder.btnTranspond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    POSITION = position;
                    ShareTask shareTask = new ShareTask();
                    shareTask.execute();
                }
            });
            holder.tvTranspond.setText(String.valueOf(datas.get(position).getShareCount()));

            // 评论
            holder.btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    POSITION = position;
                    Intent intent = new Intent(mContext, CircleDetailActivity.class);
                    intent.putExtra("id", datas.get(position).getPublicContentId());
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });

            // 详情
            convertView.setOnClickListener(new View.OnClickListener() {// 点击条目
                @Override
                public void onClick(View v) {
                    POSITION = position;
                    Intent intent = new Intent(mContext, CircleDetailActivity.class);
                    intent.putExtra("id", String.valueOf(datas.get(position).getPublicContentId()));
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
            holder.tvComment.setText(String.valueOf(datas.get(position).getCommentCount()));

            // 点赞
            holder.btnGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (datas.get(position).isFlag()) {
                        finalHolder.tvGood.setCompoundDrawablesWithIntrinsicBounds(UiUtils.getDrawable(R.mipmap.icon_good), null, null, null);
                        datas.get(position).setFlag(false);
                        datas.get(position).setPriseCount(datas.get(position).getPriseCount() - 1);
                        finalHolder.tvGood.setText(String.valueOf(datas.get(position).getPriseCount()));
                        CieclePriseTask cieclePriseTask = new CieclePriseTask(datas.get(position).getPublicContentId(), 2);
                        cieclePriseTask.execute();
                    } else {
                        finalHolder.tvGood.setCompoundDrawablesWithIntrinsicBounds(UiUtils.getDrawable(R.mipmap.icon_good_checked), null, null, null);
                        datas.get(position).setFlag(true);
                        datas.get(position).setPriseCount(datas.get(position).getPriseCount() + 1);
                        finalHolder.tvGood.setText(String.valueOf(datas.get(position).getPriseCount()));
                        CieclePriseTask cieclePriseTask = new CieclePriseTask(datas.get(position).getPublicContentId(), 1);
                        cieclePriseTask.execute();
                    }
                }
            });
            if (datas.get(position).isFlag()) {
                holder.tvGood.setCompoundDrawablesWithIntrinsicBounds(UiUtils.getDrawable(R.mipmap.icon_good_checked), null, null, null);
            } else {
                holder.tvGood.setCompoundDrawablesWithIntrinsicBounds(UiUtils.getDrawable(R.mipmap.icon_good), null, null, null);
            }
            holder.tvGood.setText(String.valueOf(datas.get(position).getPriseCount()));
            return convertView;
        }

        private void imageBrower(int position, ArrayList<String> urls) {
            Intent intent = new Intent(mContext, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

        class ViewHolder {
            public CircleImageView ivFavicon;
            public TextView tvName;
            public TextView tvAddr;
            public TextView tvCompPosition;// 公司和职位
            public TextView tvTime;// 发布时间
            public TextView tvContent;
            public TextView btnMore;// 全文按钮
            public RecyclerView recyclerview;
            public RelativeLayout btnTranspond;// 转发按钮
            public TextView tvTranspond;// 转发数量
            public RelativeLayout btnComment;// 评论按钮
            public TextView tvComment;// 评论数量
            public RelativeLayout btnGood;// 点赞按钮
            public TextView tvGood;// 点赞数量
        }
    }

    // 获取圈子列表
    private class GetCircleListTask extends AsyncTask<Void, Void, CircleListBean> {
        private int page;

        public GetCircleListTask(int page) {
            this.page = page;
        }

        @Override
        protected CircleListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETCIRCLELIST)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETCIRCLELIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("圈子列表信息", body);
                return FastJsonTools.getBean(body, CircleListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CircleListBean circleListBean) {
            super.onPostExecute(circleListBean);
            if (circleListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                return;
            } else {
                if (circleListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = circleListBean.getData();
//                        for (CircleListBean.DataBean dataBean : datas) {
//                            Log.i("内容", dataBean.toString());
//                        }
                        listview.setAdapter(myAdapter);
                    } else {
                        for (CircleListBean.DataBean dataBean : circleListBean.getData()) {
//                            Log.i("内容", dataBean.toString());
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (circleListBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, circleListBean.getMessage());
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
            GetCircleListTask getCircleList = new GetCircleListTask(0);
            getCircleList.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetCircleListTask getCircleList = new GetCircleListTask(pages);
            getCircleList.execute();
        }
    }

    // 点赞
    private class CieclePriseTask extends AsyncTask<Void, Void, CirclePriseBean> {
        private int id;
        private int flag;

        public CieclePriseTask(int id, int flag) {
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
    }

    // 分享
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHARECIRCLE)),
                            "type", "2",
                            "contentId", String.valueOf(datas.get(POSITION).getPublicContentId()),
                            Constant.BASE_URL + Constant.SHARECIRCLE,
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
            if (shareBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(getActivity());
                    DialogUtils.shareDialog(getActivity(), refreshView, shareUtils, "金指投圈子", datas.get(POSITION).getContent(), datas.get(POSITION).getUsers().getHeadSculpture(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == CircleDetailActivity.RESULT_CODE) {
                if (data.getIntExtra("FLAG", 0) == 1) {// 在详情中点了赞
                    datas.get(POSITION).setFlag(true);
                    datas.get(POSITION).setPriseCount(datas.get(POSITION).getPriseCount() + 1);
                } else if (data.getIntExtra("FLAG", 0) == 2) {// 在详情中取消了点赞
                    datas.get(POSITION).setFlag(false);
                    datas.get(POSITION).setPriseCount(datas.get(POSITION).getPriseCount() - 1);
                }
                myAdapter.notifyDataSetChanged();
            }
            if (resultCode == ReleaseCircleActivity.RESULT_CODE) {// 发表了状态
                GetCircleListTask getCircleList = new GetCircleListTask(0);
                getCircleList.execute();
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
