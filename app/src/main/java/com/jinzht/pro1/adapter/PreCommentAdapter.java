package com.jinzht.pro1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jinzht.pro1.R;

/**
 * 预选项目中的评论列表数据适配器
 */
public class PreCommentAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    public PreCommentAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_pre_comment, null);
        return view;
    }
}
