package com.jinzht.pro1.callback;

import android.view.View;

/**
 * RecyclerView的点击事件回调接口
 */
public interface ItemClickListener {
    public void onItemClick(View view, int position);

    public void onItemLongClick(View view, int position);

    public void onItemSubViewClick(View view, int position);
}
