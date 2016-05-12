package com.jinzht.pro1.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 给水平线性RecyclerView填充数据
 */
public class HorizontalRecyclerViewData {

    // 给团队成员填充数据
    public static void setData(RecyclerView recyclerView, Context context, RecyclerView.Adapter adapter) {
        // 使RecyclerView保持固定的大小，该信息被用于自身的优化。
        recyclerView.setHasFixedSize(true);
        // 水平线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        // 填充数据
        recyclerView.setAdapter(adapter);
    }
}
