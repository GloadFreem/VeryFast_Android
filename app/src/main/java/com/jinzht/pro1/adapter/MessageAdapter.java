package com.jinzht.pro1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.jinzht.pro1.R;

import java.util.HashMap;

/**
 * 站内信列表
 */
public class MessageAdapter extends BaseAdapter {

    //    private ArrayList<String> list;// 填充数据的list
    private static HashMap<Integer, Boolean> isSelected;// 用来控制CheckBox的选中状况
    private Context context;
    private LayoutInflater mInflater = null;
    private boolean checkable;

    public MessageAdapter(Context context, boolean checkable) {
        this.context = context;
//        this.list = list;
        mInflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
//        for (int i = 0; i < list.size(); i++) {
//            getIsSelected().put(i, false);
//        }
        for (int i = 0; i < 10; i++) {
            getIsSelected().put(i, false);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MessageAdapter.isSelected = isSelected;
    }

    @Override
    public int getCount() {
//        return list.size();
        return 10;
    }

    @Override
    public Object getItem(int position) {
//        return list.get(position);
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = mInflater.inflate(R.layout.item_message, null);
//            holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_message_cb_select);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示
//        holder.tv.setText(list.get(position));
        // 根据isSelected来设置checkbox的选中状况
        holder.cb.setChecked(getIsSelected().get(position));
        if (checkable) {
            holder.cb.setVisibility(View.GONE);
        } else {
            holder.cb.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        //        TextView tv;
        CheckBox cb;
    }
}