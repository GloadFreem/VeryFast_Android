package com.jinzht.pro1.fragment;

import android.os.Bundle;
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
import com.jinzht.pro1.activity.RoadshowDetailsActivity;
import com.jinzht.pro1.adapter.ChatMsgAdapter;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.ChatMsgBean;
import com.jinzht.pro1.utils.SuperToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 路演项目详情页中的现场
 */
public class RoadshowLiveFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout btnPlay;// 播放暂停按钮
    public static ImageView ivPlay;// 播放暂停按钮图标
    public static SeekBar sbVoice;// 播放进度条
    public static TextView tvVoiceTime;// 播放时间
    private EditText edChat;// 发送消息输入框
    private TextView btnChatSend;// 发送按钮
    private ListView listview;// 聊天列表

    private ChatMsgAdapter msgAdapter;// 聊天内容适配器
    private List<ChatMsgBean> mDataArrays = new ArrayList<>();// 聊天内容

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadshow_live, container, false);
        btnPlay = (LinearLayout) view.findViewById(R.id.roadshow_btn_play);// 播放暂停按钮
        btnPlay.setOnClickListener(this);
        ivPlay = (ImageView) view.findViewById(R.id.roadshow_iv_play);// 播放暂停按钮图标
        if (RoadshowDetailsActivity.isPlaying) {
            ivPlay.setBackgroundResource(R.mipmap.icon_pause);
        } else {
            ivPlay.setBackgroundResource(R.mipmap.icon_play);
        }
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

        sbVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress() * RoadshowDetailsActivity.player.getDuration() / seekBar.getMax();  //计算进度条需要前进的位置数据大小
                RoadshowDetailsActivity.player.seekTo(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });

        // 给聊天列表填充数据
        initChatData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roadshow_btn_play:
                if (RoadshowDetailsActivity.isPlaying) {// 正在播放，点击暂停
                    RoadshowDetailsActivity.isPlaying = false;
                    ivPlay.setBackgroundResource(R.mipmap.icon_play);
                    RoadshowDetailsActivity.player.pause();
                    RoadshowDetailsActivity.postSize = RoadshowDetailsActivity.player.getCurrentPosition();
                } else {// 暂停，点击播放
                    RoadshowDetailsActivity.isPlaying = true;
                    ivPlay.setBackgroundResource(R.mipmap.icon_pause);
                    RoadshowDetailsActivity.playMediaMethod();
                }
                break;
            case R.id.roadshow_btn_chat_send:// 发送消息
                SuperToastUtils.showSuperToast(mContext, 2, "走你");
                break;
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
        mDataArrays.clear();
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

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}