package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.MessageAdapter;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

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
    private ListView lvMessage;// 站内信列表

    private MessageAdapter messageAdapter;// 站内信列表数据填充器
    private boolean checkable;// 是否开启编辑的标识
    private boolean checkedAll;// 是否全选的标识

    @Override
    protected int getResourcesId() {
        return R.layout.activity_message;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

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
        messageBtnDel = (TextView) findViewById(R.id.message_btn_del);// 删除按钮
        messageBtnDel.setOnClickListener(this);
        lvMessage = (ListView) findViewById(R.id.lv_message);// 站内信列表
        // 填充数据
        messageAdapter = new MessageAdapter(mContext, checkable);
        lvMessage.setAdapter(messageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.tv_title:// 筛选
                SuperToastUtils.showSuperToast(this, 2, "筛选");
                break;
            case R.id.btn_edit:// 点击批量删除
                if (checkable) {
                    messageRlBottom.setVisibility(View.GONE);
                    checkable = false;
                } else {
                    messageRlBottom.setVisibility(View.VISIBLE);
                    checkable = true;
                }
                messageAdapter.notifyDataSetInvalidated();
                break;
            case R.id.message_btn_del:// 删除
                SuperToastUtils.showSuperToast(this, 2, "删除");
                break;
            case R.id.message_cb_all:// 全选
                for (int i = 0; i < 10; i++) {
                    if (checkedAll) {
                        // 遍历整个列表，将选中状态全部置为false
                        messageAdapter.getIsSelected().put(i, false);
                        checkedAll = false;
                    } else {
                        // 遍历整个列表，将选中状态全部置为true
                        messageAdapter.getIsSelected().put(i, true);
                        checkedAll = true;
                    }
                }
                // 刷新
                messageAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
