package com.jinzht.pro1.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView填充数据
 */
public class RecyclerViewData {

    // 水平线性的RecyclerView数据填充
    public static void setHorizontal(RecyclerView recyclerView, Context context, RecyclerView.Adapter adapter) {
        // 使RecyclerView保持固定的大小，该信息被用于自身的优化。
        recyclerView.setHasFixedSize(true);
        // 水平线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        // 填充数据
        recyclerView.setAdapter(adapter);
    }

    // 网格状RecyclerView数据填充
    public static void setGrid(RecyclerView recyclerView, Context context, RecyclerView.Adapter adapter) {
        // 使RecyclerView保持固定的大小，该信息被用于自身的优化。
        recyclerView.setHasFixedSize(true);
        // 列表布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        // 填充数据
        recyclerView.setAdapter(adapter);
    }
}
