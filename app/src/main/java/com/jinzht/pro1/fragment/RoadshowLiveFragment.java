package com.jinzht.pro1.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jinzht.pro1.utils.SuperToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 路演项目详情页中的现场
 */
public class RoadshowLiveFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout roadshowBtnPlay;// 播放暂停按钮
    private ImageView roadshowIvPlay;// 播放暂停按钮图标
    private SeekBar roadshowSbVoice;// 播放进度条
    private TextView roadshowTvVoiceTime;// 播放时间
    private EditText roadshowEdChat;// 发送消息输入框
    private TextView roadshowBtnChatSend;// 发送按钮
    private ListView roadshowLvChat;// 聊天列表

    private boolean isPlaying = false;
    private ChatMsgAdapter msgAdapter;// 聊天内容适配器
    private List<ChatMsgBean> mDataArrays = new ArrayList<>();// 聊天内容

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadshow_live,container,false);
        roadshowBtnPlay = (LinearLayout) view.findViewById(R.id.roadshow_btn_play);// 播放暂停按钮
        roadshowBtnPlay.setOnClickListener(this);
        roadshowIvPlay = (ImageView) view.findViewById(R.id.roadshow_iv_play);// 播放暂停按钮图标
        roadshowSbVoice = (SeekBar) view.findViewById(R.id.roadshow_sb_voice);// 播放进度条
        roadshowTvVoiceTime = (TextView) view.findViewById(R.id.roadshow_tv_voice_time);// 播放时间
        roadshowEdChat = (EditText) view.findViewById(R.id.roadshow_ed_chat);// 发送消息输入框
        roadshowBtnChatSend = (TextView) view.findViewById(R.id.roadshow_btn_chat_send);// 发送按钮
        roadshowBtnChatSend.setOnClickListener(this);
        roadshowLvChat = (ListView) view.findViewById(R.id.roadshow_lv_chat);// 聊天界面
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 给聊天列表填充数据
        initChatData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roadshow_btn_play:
                if (isPlaying) {// 正在播放，点击暂停
                    roadshowIvPlay.setBackgroundResource(R.mipmap.icon_pause);
                    isPlaying = false;
                } else {// 暂停，点击播放
                    roadshowIvPlay.setBackgroundResource(R.mipmap.icon_play);
                    isPlaying = true;
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
                "周二 18:11","","","","",""};
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
        roadshowLvChat.setAdapter(msgAdapter);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
