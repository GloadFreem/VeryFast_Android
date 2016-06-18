package com.jinzht.pro1.fragment;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ChatMsgAdapter;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.ChatMsgBean;
import com.jinzht.pro1.bean.RoadshowVoiceBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SuperToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 路演项目详情页中的现场
 */
public class RoadshowLiveFragment extends BaseFragment implements View.OnClickListener {

    private final static int VIDEO_FILE_ERROR = 1;// 网络错误
    private final static int VIDEO_UPDATE_SEEKBAR = 2;// 更新播放进度条

    private String projectId;

    private LinearLayout btnPlay;// 播放暂停按钮
    private ImageView ivPlay;// 播放暂停按钮图标
    private SeekBar sbVoice;// 播放进度条
    private TextView tvVoiceTime;// 播放时间
    private EditText edChat;// 发送消息输入框
    private TextView btnChatSend;// 发送按钮
    private ListView listview;// 聊天列表

    private RoadshowVoiceBean.DataBean data;// 音频数据
    private ChatMsgAdapter msgAdapter;// 聊天内容适配器
    private List<ChatMsgBean> mDataArrays = new ArrayList<>();// 聊天内容

    private String url;// 音频播放地址
    private MediaPlayer player;// 音频播放器
    private int postSize = 0;// 保存已播音频大小
    private boolean isPlaying = false;// 是否正在播放的标识
    private UpdateSeekBarR updateSeekBarR;   //更新进度条用

    public RoadshowLiveFragment() {
    }

    public RoadshowLiveFragment(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadshow_live, container, false);
        btnPlay = (LinearLayout) view.findViewById(R.id.roadshow_btn_play);// 播放暂停按钮
        btnPlay.setOnClickListener(this);
        ivPlay = (ImageView) view.findViewById(R.id.roadshow_iv_play);// 播放暂停按钮图标
        sbVoice = (SeekBar) view.findViewById(R.id.roadshow_sb_voice);// 播放进度条
        tvVoiceTime = (TextView) view.findViewById(R.id.roadshow_tv_voice_time);// 播放时间
        edChat = (EditText) view.findViewById(R.id.roadshow_ed_chat);// 发送消息输入框
        btnChatSend = (TextView) view.findViewById(R.id.roadshow_btn_chat_send);// 发送按钮
        btnChatSend.setOnClickListener(this);
        listview = (ListView) view.findViewById(R.id.roadshow_lv_chat);// 聊天界面
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        player = new MediaPlayer();
        updateSeekBarR = new UpdateSeekBarR();// 进度更新
        setPalyerListener();// 设置各种监听

        GetVoidTask getVoidTask = new GetVoidTask();
        getVoidTask.execute();

        // 给聊天列表填充数据
        initChatData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roadshow_btn_play:
                if (isPlaying) {// 正在播放，点击暂停
                    ivPlay.setBackgroundResource(R.mipmap.icon_play);
                    player.pause();
                    postSize = player.getCurrentPosition();
                    isPlaying = false;
                } else {// 暂停，点击播放
                    ivPlay.setBackgroundResource(R.mipmap.icon_pause);
                    playMediaMethod();
                    isPlaying = true;
                }
                break;
            case R.id.roadshow_btn_chat_send:// 发送消息
                SuperToastUtils.showSuperToast(mContext, 2, "走你");
                break;
        }
    }

    //播放视频的方法
    private void playMediaMethod() {
        if (postSize > 0 && url != null) {    //说明，停止过 activity调用过pause方法，跳到停止位置播放
            int sMax = sbVoice.getMax();
            int mMax = player.getDuration();
            sbVoice.setProgress(postSize * sMax / mMax);
            new PlayThread(postSize).start();//从postSize位置开始放
        } else {
            new PlayThread(0).start();//表明是第一次开始播放
        }
    }

    //播放音频的线程
    private class PlayThread extends Thread {
        int post = 0;

        public PlayThread(int post) {
            this.post = post;
        }

        @Override
        public void run() {
            try {
                player.reset();    //恢复播放器默认
                player.setDataSource(url);   //设置播放路径
                player.prepare();  //准备播放
                player.setOnPreparedListener(new VoicePreparedL(post));  //设置监听事件
            } catch (Exception e) {
                mHandler.sendEmptyMessage(VIDEO_FILE_ERROR);
            }
            super.run();
        }
    }

    //播放视图准备事件监听器
    private class VoicePreparedL implements MediaPlayer.OnPreparedListener {
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
                player.start();  //开始播放视频
            } else {
                return;
            }
            new Thread(updateSeekBarR).start();   //启动线程，更新进度条
        }
    }

    //设置操作监听器
    private void setPalyerListener() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //视频播放完成
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                postSize = 0;
                ivPlay.setBackgroundResource(R.mipmap.icon_play);
            }
        });

        sbVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress() * player.getDuration() / seekBar.getMax();  //计算进度条需要前进的位置数据大小
                player.seekTo(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_FILE_ERROR:// 错误信息
                    SuperToastUtils.showSuperToast(mContext, 2, "网络连接异常");
                    ivPlay.setBackgroundResource(R.mipmap.icon_play);
                    isPlaying = false;
                    break;
                case VIDEO_UPDATE_SEEKBAR:// 更新播放进度条
                    if (player == null) {
                        isPlaying = false;
                    } else {
                        String s = "";
//                        if (isPlaying) {
                        isPlaying = true;
                        int position = player.getCurrentPosition();
                        int mMax = player.getDuration();
                        int sMax = sbVoice.getMax();
                        sbVoice.setProgress(position * sMax / mMax);

//                            mMax = 0 == mMax ? 1 : mMax;
//                            double playpercent = position * 100.00 / mMax * 1.0;

                        int i = position / 1000;
                        int hour = i / (60 * 60);
                        int minute = i / 60 % 60;
                        int second = i % 60;

                        s = String.format("%02d:%02d:%02d", hour, minute, second);
//                        } else {
//                            s += " 视频当前未播放!";
//                            s = String.valueOf(data.getTotlalTime());
//                        }
                        tvVoiceTime.setText(s);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //每隔1秒更新一下进度条线程
    private class UpdateSeekBarR implements Runnable {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(VIDEO_UPDATE_SEEKBAR);
            if (isPlaying) {
                mHandler.postDelayed(updateSeekBarR, 1000);
            }
        }
    }

    // 给聊天列表填充数据
    private void initChatData() {
        String[] msgArray = new String[]{"  孩子们，要好好学习，天天向上！要好好听课，不要翘课！不要挂科，多拿奖学金！三等奖学金的争取拿二等，二等的争取拿一等，一等的争取拿励志！",
                "老师还有什么吩咐...",
                "还有，明天早上记得跑操啊，不来的就扣德育分！",
                "德育分是什么？扣了会怎么样？",
                "德育分会影响奖学金评比，严重的话，会影响毕业",
                "哇！学院那么不人道？",
                "你要是你不听话，我当场让你不能毕业！",
                "老师，我知错了(- -我错在哪了...)"};
        String[] dataArray = new String[]{"周一 18:00", "周二 18:10",
                "周二 18:11", "", "", "", "", ""};
        int count = 8;
        for (int i = 0; i < count; i++) {
            ChatMsgBean msgBean = new ChatMsgBean();
            msgBean.setDate(dataArray[i]);
            if (i % 2 == 0) {
                msgBean.setName("老师");
                msgBean.setMsgType(true);
            } else {
                msgBean.setName("小明");
                msgBean.setMsgType(false);
            }
            msgBean.setText(msgArray[i]);
            mDataArrays.add(msgBean);
        }
        msgAdapter = new ChatMsgAdapter(mContext, mDataArrays);
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        listview.setAdapter(msgAdapter);
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
                            "projectId", projectId,
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
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (roadshowVoiceBean.getStatus() == 200) {
                    if (roadshowVoiceBean.getData() != null && roadshowVoiceBean.getData().size() != 0) {
                        data = roadshowVoiceBean.getData().get(0);
                        url = data.getAudioPath();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, roadshowVoiceBean.getMessage());
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
    public void onPause() {
        if (player != null && isPlaying) {
            postSize = player.getCurrentPosition();
            player.stop();
            isPlaying = false;
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (player != null && isPlaying) {
            postSize = player.getCurrentPosition();
            player.stop();
            isPlaying = false;
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (player != null) {
            if (isPlaying) {
                player.stop();
            }
            player.release();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            if (isPlaying) {
                player.stop();
            }
            player.release();
        }
        super.onDestroy();
    }
}