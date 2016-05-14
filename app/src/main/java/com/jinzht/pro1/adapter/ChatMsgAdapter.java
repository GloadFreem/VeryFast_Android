package com.jinzht.pro1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.bean.ChatMsgBean;
import com.jinzht.pro1.utils.StringUtils;

import java.util.List;

/**
 * 聊天列表数据填充期
 */
public class ChatMsgAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<ChatMsgBean> data;

    public ChatMsgAdapter(Context context, List<ChatMsgBean> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public int getItemViewType(int position) {
        ChatMsgBean chatMsg = data.get(position);
        if (chatMsg.getMsgType()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgBean chatMsg = data.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (getItemViewType(position)) {
                case 0:
                    convertView = mInflater.inflate(R.layout.item_roadshow_chat_left, null);
                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.item_roadshow_chat_right, null);
                    break;
            }
            viewHolder.itemChatName = (TextView) convertView.findViewById(R.id.item_chat_name);
            viewHolder.itemChatMsg = (TextView) convertView.findViewById(R.id.item_chat_msg);
            viewHolder.itemChatTime = (TextView) convertView.findViewById(R.id.item_chat_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemChatName.setText(chatMsg.getName());
        viewHolder.itemChatMsg.setText(chatMsg.getText());
        if (StringUtils.isEmpty(chatMsg.getDate())) {
            viewHolder.itemChatTime.setVisibility(View.GONE);
        } else {
            viewHolder.itemChatTime.setVisibility(View.VISIBLE);
            viewHolder.itemChatTime.setText(chatMsg.getDate());
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView itemChatName;
        public TextView itemChatMsg;
        public TextView itemChatTime;
    }
}
