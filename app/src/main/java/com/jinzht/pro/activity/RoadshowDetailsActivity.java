package com.jinzht.pro.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseFragmentActivity;
import com.jinzht.pro.bean.CustomerServiceBean;
import com.jinzht.pro.bean.PPTBean;
import com.jinzht.pro.bean.ProjectCollectBean;
import com.jinzht.pro.bean.ProjectDetailBean;
import com.jinzht.pro.bean.RoadshowMemberBean;
import com.jinzht.pro.bean.RoadshowVoiceBean;
import com.jinzht.pro.bean.ShareBean;
import com.jinzht.pro.fragment.RoadshowDetailsFragment;
import com.jinzht.pro.fragment.RoadshowLiveFragment;
import com.jinzht.pro.fragment.RoadshowMemberFragment;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.ShareUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;
import com.jinzht.pro.view.ScrollableViewPager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 路演项目详情
 */
public class RoadshowDetailsActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout btnCollect;// 收藏
    private ImageView ivCollect;// title右侧第二个按钮图标
    private LinearLayout btnShare;// title最右侧按钮，分享
    public static ScrollableViewPager vpPPt;// 播放PPT区域
    private RadioGroup rgTab;// 详情、成员、现场总合tab
    private RadioButton rbtnDetail;// 详情按钮
    private RadioButton rbtnMember;// 成员按钮
    private RadioButton rbtnLive;// 现场按钮
    private LinearLayout llInvest;// 客服和投资按钮布局
    private ImageButton btnService;// 客服按钮
    private RelativeLayout btnInvest;// 认投按钮
    private FrameLayout flModule;// 加载Fragment的布局
    private ScrollView sv;// ScrollView

    private int FLAG = 0;// 关注或取消关注的标识
    private int needRefresh = 0;// 是否需要在项目列表中刷新
    public final static int RESULT_CODE = 0;
    private final static int REQUEST_CODE = 1;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ProjectDetailBean.DataBean.ProjectBean data;// 项目详情数据
    public static int userId;

    private static int pages = 0;// PPT数据页码
    private static List<PPTBean.DataBean> pptData;// PPT数据
    private static List<ImageView> imageViews;// PPT的图片View
    private static PPTAdapter pptAdapter;

    public static RoadshowVoiceBean.DataBean voiceData;// 现场音频数据
    public static MediaPlayer player;// 音频播放器
    private static String url = "";// 音频播放地址
    public static int postSize = 0;// 保存已播音频时间
    public static boolean isPlaying = false;// 是否正在播放的标识
    private static UpdateSeekBarR updateSeekBarR;// 更新进度条用的线程
    private static String progress = "";// 播放时间进度
    private final static int VIDEO_FILE_ERROR = 1;// 网络错误
    private final static int VIDEO_UPDATE_SEEKBAR = 2;// 更新播放进度条

    @Override
    protected int getResourcesId() {
        return R.layout.activity_details_roadshow;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        pages = 0;
        pptData = new ArrayList<>();
        imageViews = new ArrayList<>();
        voiceData = null;
        player = new MediaPlayer();
        url = "";
        postSize = 0;
        isPlaying = false;
        updateSeekBarR = new UpdateSeekBarR();
        progress = "";

        fragments.add(new RoadshowDetailsFragment());
        fragments.add(new RoadshowMemberFragment());
        fragments.add(new RoadshowLiveFragment());

        pptAdapter = new PPTAdapter();

        // 设置tab的单选事件
        rgTab.setOnCheckedChangeListener(this);
        setSelect(fragments.get(2));
        GetDetailTask getDetailTask = new GetDetailTask();
        getDetailTask.execute();
        GetVoidTask getVoidTask = new GetVoidTask();
        getVoidTask.execute();
        GetMemberTask getMemberTask = new GetMemberTask();
        getMemberTask.execute();

        vpPPt.setScrollable(true);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //音频播放完成
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;

                vpPPt.setScrollable(true);

                RoadshowLiveFragment.ivPlay.setBackgroundResource(R.mipmap.icon_play);
                postSize = 0;
            }
        });
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.title_btn_left);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题，此页不显示
        tvTitle.setVisibility(View.GONE);
        btnCollect = (LinearLayout) findViewById(R.id.title_btn_right2);// 收藏
        btnCollect.setOnClickListener(this);
        ivCollect = (ImageView) findViewById(R.id.title_iv_right2);// title右侧第二个按钮图标
        btnShare = (LinearLayout) findViewById(R.id.title_btn_right);// title最右侧按钮，分享
        btnShare.setOnClickListener(this);
        vpPPt = (ScrollableViewPager) findViewById(R.id.details_roadshow_ppt);// 播放PPT区域
        rgTab = (RadioGroup) findViewById(R.id.details_rg_tab);// 详情、成员、现场总合tab
        rbtnDetail = (RadioButton) findViewById(R.id.details_rbtn_detail);// 详情按钮
        rbtnMember = (RadioButton) findViewById(R.id.details_rbtn_member);// 成员按钮
        rbtnLive = (RadioButton) findViewById(R.id.details_rbtn_live);// 现场按钮
        llInvest = (LinearLayout) findViewById(R.id.details_ll_invest);// 客服和投资按钮布局
        btnService = (ImageButton) findViewById(R.id.details_btn_service);// 客服按钮
        btnService.setOnClickListener(this);
        btnInvest = (RelativeLayout) findViewById(R.id.details_btn_invest);// 认投按钮
        btnInvest.setOnClickListener(this);
        flModule = (FrameLayout) findViewById(R.id.fl_module);// 加载Fragment的布局
        sv = (ScrollView) findViewById(R.id.details_sl);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_btn_left:// 返回上一页
                onBackPressed();
                break;
            case R.id.title_btn_right2:// 收藏
                if (clickable) {
                    clickable = false;
                    if (data.isCollected()) {
                        CollectTask collectTask = new CollectTask(2);
                        collectTask.execute();
                    } else {
                        CollectTask collectTask = new CollectTask(1);
                        collectTask.execute();
                    }
                }
                break;
            case R.id.title_btn_right:// 分享
                if (clickable) {
                    clickable = false;
                    ShareTask shareTask = new ShareTask();
                    shareTask.execute();
                }
                break;
            case R.id.details_btn_service:// 给客服打电话
                if (clickable) {
                    clickable = false;
                    CustomerServiceTask customerServiceTask = new CustomerServiceTask();
                    customerServiceTask.execute();
                }
                break;
            case R.id.details_btn_invest:// 认投
                if ("已认证".equals(SharedPreferencesUtils.getIsAuthentic(mContext))) {
                    Intent intent1 = new Intent(mContext, InvestActivity.class);
                    intent1.putExtra("projectId", String.valueOf(data.getProjectId()));
                    intent1.putExtra("limitAmount", data.getRoadshows().get(0).getRoadshowplan().getLimitAmount());
                    intent1.putExtra("profit", data.getRoadshows().get(0).getRoadshowplan().getProfit());
                    intent1.putExtra("borrower_user_no", data.getBorrowerUserNumber());
                    intent1.putExtra("abbrevName", data.getAbbrevName());
                    intent1.putExtra("fullName", data.getFullName());
                    intent1.putExtra("img", data.getStartPageImage());
                    startActivity(intent1);
                } else if ("认证中".equals(SharedPreferencesUtils.getIsAuthentic(mContext))) {
                    DialogUtils.confirmDialog(this, "您的信息正在认证中，通过后方可查看！", "确定");
                } else {
                    DialogUtils.goAuthentic(this);
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

    // 设置tab的单选事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.details_rbtn_detail:// 选择了详情
                setSelect(fragments.get(0));
                rbtnDetail.setTextColor(UiUtils.getColor(R.color.custom_orange));
                rbtnMember.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnLive.setTextColor(UiUtils.getColor(R.color.bg_text));
                llInvest.setVisibility(View.VISIBLE);
                break;
            case R.id.details_rbtn_member:// 选择了成员
                setSelect(fragments.get(1));
                rbtnDetail.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnMember.setTextColor(UiUtils.getColor(R.color.custom_orange));
                rbtnLive.setTextColor(UiUtils.getColor(R.color.bg_text));
                llInvest.setVisibility(View.GONE);
                break;
            case R.id.details_rbtn_live:// 选择了现场
                setSelect(fragments.get(2));
                rbtnDetail.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnMember.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbtnLive.setTextColor(UiUtils.getColor(R.color.custom_orange));
                llInvest.setVisibility(View.GONE);
                break;
        }
    }

    // 选择Fragment
    private void setSelect(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_module, fragment);
        transaction.commit();
    }

    // 关注处理
    private void initCollect() {
        if (data.isCollected()) {
            ivCollect.setBackgroundResource(R.mipmap.icon_collected);
        } else {
            ivCollect.setBackgroundResource(R.mipmap.icon_collect);
        }
    }

    // 处理PPT
    private static void initPPT() {
        imageViews.clear();
        for (PPTBean.DataBean bean : pptData) {
            ImageView imageView = new ImageView(UiUtils.getContext());
            // 设置图片缩放类型
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(UiUtils.getContext()).load(bean.getImageUrl()).into(imageView);
            imageViews.add(imageView);
        }
        if (pages == 0) {
            vpPPt.setAdapter(pptAdapter);
            vpPPt.setOffscreenPageLimit(5);
            vpPPt.setCurrentItem(0);
        } else {
            pptAdapter.notifyDataSetChanged();
        }
    }

    // PPT的填充器
    private class PPTAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    // 获取项目详情
    private class GetDetailTask extends AsyncTask<Void, Void, ProjectDetailBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected ProjectDetailBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTDETAIL)),
                            "projectId", getIntent().getStringExtra("id"),
                            Constant.BASE_URL + Constant.GETPROJECTDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("路演项目详情", body);
                return FastJsonTools.getBean(body, ProjectDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectDetailBean projectDetailBean) {
            super.onPostExecute(projectDetailBean);
            if (projectDetailBean == null) {
                dismissProgressDialog();
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (projectDetailBean.getStatus() == 200) {
                    EventBus.getDefault().postSticky(projectDetailBean.getData());
                    data = projectDetailBean.getData().getProject();
                    if (data != null) {
                        initCollect();
                        userId = data.getUserId();
                        Log.i("项目的userId", userId + "");
                    }
                } else {
                    dismissProgressDialog();
                    SuperToastUtils.showSuperToast(mContext, 2, projectDetailBean.getMessage());
                }
            }
        }
    }

    // 获取成员
    private class GetMemberTask extends AsyncTask<Void, Void, RoadshowMemberBean> {
        @Override
        protected RoadshowMemberBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETMEMBER)),
                            "projectId", getIntent().getStringExtra("id"),
                            Constant.BASE_URL + Constant.GETMEMBER,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("路演项目成员", body);
                return FastJsonTools.getBean(body, RoadshowMemberBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RoadshowMemberBean roadshowMemberBean) {
            super.onPostExecute(roadshowMemberBean);
            dismissProgressDialog();
            if (roadshowMemberBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (roadshowMemberBean.getStatus() == 200) {
                    EventBus.getDefault().postSticky(roadshowMemberBean.getData().get(0));
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, roadshowMemberBean.getMessage());
                }
            }
        }
    }

    // 获取音频链接
    private class GetVoidTask extends AsyncTask<Void, Void, RoadshowVoiceBean> {
        @Override
        protected RoadshowVoiceBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETROADSHOWVOICE)),
                            "projectId", getIntent().getStringExtra("id"),
                            Constant.BASE_URL + Constant.GETROADSHOWVOICE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("获取的路演音频", body);
                return FastJsonTools.getBean(body, RoadshowVoiceBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RoadshowVoiceBean roadshowVoiceBean) {
            super.onPostExecute(roadshowVoiceBean);
            if (roadshowVoiceBean == null) {
                dismissProgressDialog();
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (roadshowVoiceBean.getStatus() == 200) {
                    if (roadshowVoiceBean.getData() != null && roadshowVoiceBean.getData().size() != 0) {
                        EventBus.getDefault().postSticky(roadshowVoiceBean.getData().get(0));
                        voiceData = roadshowVoiceBean.getData().get(0);
                        url = roadshowVoiceBean.getData().get(0).getAudioPath();
                        GetPPTTask getPPTTask = new GetPPTTask(roadshowVoiceBean.getData().get(0).getSceneId(), pages);
                        getPPTTask.execute();
                        if (voiceData != null && RoadshowLiveFragment.tvVoiceTime != null) {
                            long i = voiceData.getTotlalTime() / 1000;
                            long hour = i / (60 * 60);
                            long minute = i / 60 % 60;
                            long second = i % 60;
                            progress = String.format("%02d:%02d:%02d", hour, minute, second);
                            RoadshowLiveFragment.tvVoiceTime.setText(progress);
                        }
                    }
                } else {
                    dismissProgressDialog();
                    SuperToastUtils.showSuperToast(mContext, 2, roadshowVoiceBean.getMessage());
                }
            }
        }
    }

    // 获取PPT数据
    private static class GetPPTTask extends AsyncTask<Void, Void, PPTBean> {
        private int sceneId;
        private int page;

        public GetPPTTask(int sceneId, int page) {
            this.sceneId = sceneId;
            this.page = page;
        }

        @Override
        protected PPTBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(UiUtils.getContext()))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPPTDATA)),
                            "sceneId", String.valueOf(sceneId),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETPPTDATA,
                            UiUtils.getContext()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("PPT数据" + page, body);
                return FastJsonTools.getBean(body, PPTBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(PPTBean pptBean) {
            super.onPostExecute(pptBean);
            if (pptBean != null && pptBean.getStatus() == 200) {
                if (page == 0) {
                    pptData = pptBean.getData();
                    if (pptData.size() != 0) {
                        initPPT();
                    }
                } else {
                    for (PPTBean.DataBean bean : pptBean.getData()) {
                        pptData.add(bean);
                    }
                    initPPT();
                }
                Log.i("PPT第" + pages + "页", String.valueOf(pptData.size()));
            }
        }
    }

    // 关注
    private class CollectTask extends AsyncTask<Void, Void, ProjectCollectBean> {
        int flag;

        public CollectTask(int flag) {
            this.flag = flag;
        }

        @Override
        protected ProjectCollectBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COLLECTPROJECT)),
                            "projectId", String.valueOf(data.getProjectId()),
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.COLLECTPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("关注项目返回信息", body);
                return FastJsonTools.getBean(body, ProjectCollectBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectCollectBean projectCollectBean) {
            super.onPostExecute(projectCollectBean);
            clickable = true;
            if (projectCollectBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (projectCollectBean.getStatus() == 200) {
                    if (flag == 1) {
                        data.setCollected(true);
                        data.setCollectionCount(data.getCollectionCount() + 1);
                        FLAG = 1;
                    } else if (flag == 2) {
                        data.setCollected(false);
                        data.setCollectionCount(data.getCollectionCount() - 1);
                        FLAG = 2;
                    }
                    needRefresh++;
                    initCollect();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, projectCollectBean.getMessage());
                }
            }
        }
    }

    // 分享项目
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREPROJECT)),
                            "type", "1",
                            "projectId", String.valueOf(data.getProjectId()),
                            Constant.BASE_URL + Constant.SHAREPROJECT,
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
                    ShareUtils shareUtils = new ShareUtils(RoadshowDetailsActivity.this);
                    DialogUtils.newShareDialog(RoadshowDetailsActivity.this, shareUtils, data.getAbbrevName(), data.getDescription(), data.getStartPageImage(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
                }
            }
        }
    }

    // 客服接口
    private class CustomerServiceTask extends AsyncTask<Void, Void, CustomerServiceBean> {
        @Override
        protected CustomerServiceBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CUSTOMERSERVICE)),
                            Constant.BASE_URL + Constant.CUSTOMERSERVICE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("客服信息", body);
                return FastJsonTools.getBean(body, CustomerServiceBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CustomerServiceBean customerServiceBean) {
            super.onPostExecute(customerServiceBean);
            clickable = true;
            if (customerServiceBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (customerServiceBean.getStatus() == 200) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + customerServiceBean.getData().getTel());
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, customerServiceBean.getMessage());
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

    //播放视频的方法
    public static void playMediaMethod() {
        Log.i("postSize大小", String.valueOf(postSize));
        if (postSize > 0 && url != null) {//说明，停止过 activity调用过pause方法，跳到停止位置播放
            int sMax = RoadshowLiveFragment.sbVoice.getMax();
            int mMax = player.getDuration();
            if (mMax != 0) {
                RoadshowLiveFragment.sbVoice.setProgress(postSize * sMax / mMax);
            }
            new PlayThread(postSize).start();//从postSize位置开始放
        } else {
            new PlayThread(0).start();//表明是第一次开始播放
        }
    }

    //播放音频的线程
    public static class PlayThread extends Thread {
        int post = 0;

        public PlayThread(int post) {
            this.post = post;
        }

        @Override
        public void run() {
            if (postSize == 0) {
                player = new MediaPlayer();
            }
            try {
                player.reset();    //恢复播放器默认
                player.setDataSource(url);   //设置播放路径
                player.prepare();  //准备播放
                player.setOnPreparedListener(new VoicePreparedL(post));  //设置监听事件
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(VIDEO_FILE_ERROR);
            }
            super.run();
        }
    }

    //播放准备事件监听器
    public static class VoicePreparedL implements MediaPlayer.OnPreparedListener {
        int postSize;

        public VoicePreparedL(int postSize) {
            this.postSize = postSize;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {//准备完成
            if (player != null) {
                if (postSize > 0) {  //说明中途停止过（activity调用过pause方法，不是用户点击停止按钮），跳到停止时候位置开始播放
                    player.seekTo(postSize);   //跳到postSize大小位置处进行播放
                }
                player.start();  //开始播放
            } else {
                return;
            }
            new Thread(updateSeekBarR).start();   //启动线程，更新进度条
        }
    }

    //每隔1秒更新一下进度条线程
    private static class UpdateSeekBarR implements Runnable {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(VIDEO_UPDATE_SEEKBAR);
            if (isPlaying) {
                mHandler.postDelayed(updateSeekBarR, 1000);
            }
        }
    }

    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_FILE_ERROR:// 错误信息
                    SuperToastUtils.showSuperToast(UiUtils.getContext(), 2, "暂无数据");
                    isPlaying = false;

                    vpPPt.setScrollable(true);

                    RoadshowLiveFragment.ivPlay.setBackgroundResource(R.mipmap.icon_play);
                    break;
                case VIDEO_UPDATE_SEEKBAR:// 更新播放进度条
                    if (isPlaying) {
                        int position = player.getCurrentPosition();
                        int mMax = player.getDuration();
                        int sMax = RoadshowLiveFragment.sbVoice.getMax();
                        if (mMax != 0) {
                            RoadshowLiveFragment.sbVoice.setProgress(position * sMax / mMax);
                        }

                        int i = position / 1000;
                        int hour = i / (60 * 60);
                        int minute = i / 60 % 60;
                        int second = i % 60;
                        progress = String.format("%02d:%02d:%02d", hour, minute, second);

                        // 更新PPT
                        for (PPTBean.DataBean bean : pptData) {
                            if (i >= bean.getStartTime() && i <= bean.getEndTime()) {
                                vpPPt.setCurrentItem(bean.getSortIndex());
                            }
                        }

                        // 当播放到倒数第二页时加载最新PPT
                        if (vpPPt.getCurrentItem() >= pptData.size() - 3 && vpPPt.getCurrentItem() != 0) {
                            GetPPTTask getPPTTask = new GetPPTTask(voiceData.getSceneId(), ++pages);
                            getPPTTask.execute();
                        }
                    }
                    RoadshowLiveFragment.tvVoiceTime.setText(progress);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        if (!RoadshowLiveFragment.flag) {
            if (player != null && isPlaying) {
                isPlaying = false;
                RoadshowLiveFragment.ivPlay.setBackgroundResource(R.mipmap.icon_play);

                vpPPt.setScrollable(true);

                player.pause();
                postSize = player.getCurrentPosition();
            }
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (!RoadshowLiveFragment.flag) {
            if (player != null && isPlaying) {
                isPlaying = false;
                RoadshowLiveFragment.ivPlay.setBackgroundResource(R.mipmap.icon_play);

                vpPPt.setScrollable(true);

                player.pause();
                postSize = player.getCurrentPosition();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            if (isPlaying) {
                isPlaying = false;
                RoadshowLiveFragment.ivPlay.setBackgroundResource(R.mipmap.icon_play);

                vpPPt.setScrollable(true);

                player.stop();
            }
            player.release();
            player = null;
        }
        super.onDestroy();
    }
}
