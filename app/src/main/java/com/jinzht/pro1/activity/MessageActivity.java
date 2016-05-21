package com.jinzht.pro1.activity;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * 站内信页面
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout btnEdit;// 编辑按钮
    private TextView tvEdit;// 编辑文字
    private RelativeLayout messageRlBottom;// 底部编辑栏
    private CheckBox messageCbAll;// 全选按钮
    private TextView messageBtnDel;// 删除按钮
    private RecyclerView lvMessage;// 站内信列表

    private boolean clickable;// 是否可编辑标识
    private boolean clickedAll;// 是否全选标识
    private List<Boolean> boolList = new ArrayList<>();// 选中item的列表
    private List<Integer> datas = new ArrayList<>();// 模拟数据
    private MyAdapter myAdapter;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_message;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setOnClickListener(this);
        btnEdit = (LinearLayout) findViewById(R.id.btn_edit);// 编辑按钮
        btnEdit.setOnClickListener(this);
        tvEdit = (TextView) findViewById(R.id.tv_edit);// 编辑文字
        messageRlBottom = (RelativeLayout) findViewById(R.id.message_rl_bottom);// 底部编辑栏
        messageCbAll = (CheckBox) findViewById(R.id.message_cb_all);// 全选按钮
        messageCbAll.setOnClickListener(this);
        messageCbAll.setChecked(false);
        messageBtnDel = (TextView) findViewById(R.id.message_btn_del);// 删除按钮
        messageBtnDel.setOnClickListener(this);
        lvMessage = (RecyclerView) findViewById(R.id.lv_message);// 站内信列表

        // 初始化站内信列表
        initList();
    }

    private void initList() {
        // 填充数据
        for (int i = 0; i < 10; i++) {
            datas.add(i);
            boolList.add(false);
        }
        myAdapter = new MyAdapter();
        RecyclerViewData.setVertical(lvMessage, mContext, myAdapter);
        myAdapter.setItemClickListener(new ItemClickListener() {
            // 点击条目选中
            @Override
            public void onItemClick(View view, int position) {
                if (clickable) {
                    MyAdapter.MViewHolder holder = (MyAdapter.MViewHolder) view.getTag();
                    holder.cb.setTag(position);
                    holder.cb.toggle();
                    if (holder.cb.isChecked()) {
                        boolList.set(position, true);
                    } else {
                        boolList.set(position, false);
                    }
                }
            }

            // 长按可编辑
            @Override
            public void onItemLongClick(View view, int position) {
                if (!clickable) {
                    tvEdit.setText("完成");
                    messageRlBottom.setVisibility(View.VISIBLE);
                    clickable = true;
                    // 将所有item都变为未选中状态
                    if (myAdapter.getItemCount() > 0) {
                        for (int i = 0; i < myAdapter.getItemCount(); i++) {
                            boolList.set(i, false);
                        }
                    }
                    messageCbAll.setChecked(false);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.tv_title:// 点击标题，进行筛选
                SuperToastUtils.showSuperToast(this, 2, "筛选");
                break;
            case R.id.btn_edit:// 点击编辑，可批量删除
                if (clickable) {
                    tvEdit.setText("编辑");
                    messageRlBottom.setVisibility(View.GONE);
                    clickable = false;
                } else {
                    tvEdit.setText("完成");
                    messageRlBottom.setVisibility(View.VISIBLE);
                    clickable = true;
                }
                // 将所有item都变为未选中状态
                if (myAdapter.getItemCount() > 0) {
                    for (int i = 0; i < myAdapter.getItemCount(); i++) {
                        boolList.set(i, false);
                    }
                }
                messageCbAll.setChecked(false);
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.message_cb_all:// 全选
                if (clickable && myAdapter.getItemCount() > 0) {
                    if (clickedAll) {
                        for (int i = 0; i < myAdapter.getItemCount(); i++) {
                            boolList.set(i, false);
                        }
                        messageCbAll.setChecked(false);
                        clickedAll = false;
                    } else {
                        for (int i = 0; i < myAdapter.getItemCount(); i++) {
                            boolList.set(i, true);
                            Log.i("全选", boolList.toString());
                        }
                        clickedAll = true;
                        messageCbAll.setChecked(true);
                    }
                    myAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.message_btn_del:// 删除选中的条目
                if (clickable && myAdapter.getItemCount() > 0) {
                    for (int i = 0; i < myAdapter.getItemCount(); ) {
                        if (boolList.get(i)) {
                            Log.i("删除", datas.get(i).toString());
                            datas.remove(i);
                            Log.i("数据", datas.toString());
                            boolList.remove(i);
                            myAdapter.notifyItemRemoved(i);
                            continue;
                        }
                        i++;
                    }
                }
                break;
        }
    }

    // 站内信列表数据填充器
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private ItemClickListener mItemClickListener;
        private MViewHolder holder;

        public void setItemClickListener(ItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public MyAdapter.MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
            holder = new MViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MViewHolder holder, int position) {
            holder.tv.setText("金指投平台" + position);
            if (clickable) {
                holder.cb.setVisibility(View.VISIBLE);
            } else {
                holder.cb.setVisibility(View.GONE);
            }
            // 给checkBox设置Tag，解决复用出现的bug
            holder.cb.setTag(position);
            if (boolList != null) {
                holder.cb.setChecked((boolList.get(position)));
            } else {
                holder.cb.setChecked(false);
            }
            // checkBox的选中事件
            onChecked(holder, position);
        }

        public class MViewHolder extends RecyclerView.ViewHolder {
            public TextView tv;
            public CheckBox cb;

            public MViewHolder(final View itemView) {
                super(itemView);
                itemView.setTag(this);
                tv = (TextView) itemView.findViewById(R.id.item_message_name);
                cb = (CheckBox) itemView.findViewById(R.id.item_message_cb_select);
                // 为item添加点击事件回调
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(itemView, getPosition());
                        }
                    }
                });
                // 为item添加长按事件回调
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemLongClick(itemView, getPosition());
                        }
                        return true;
                    }
                });
            }
        }

        public void onChecked(final MViewHolder holder, final int position) {
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        boolList.set((Integer) holder.cb.getTag(), true);
                        Log.i("选中", boolList.toString());
                    } else {
                        boolList.set((Integer) holder.cb.getTag(), false);
                        Log.i("不选中", boolList.toString());
                    }
                }
            });
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
