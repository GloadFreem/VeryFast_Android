package com.jinzht.pro.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.RoadshowMemberBean;
import com.jinzht.pro.view.CircleImageView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 路演项目详情页中的成员
 */
public class RoadshowMemberFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView ivFavicon;// 头像
    private TextView tvName;// 姓名
    private TextView tvCompNamePosition;// 公司和职位
    private TextView tvAddr;// 地址
    private TextView btnEmail;// 邮箱按钮
    private TextView btnTel;// 电话

    private RoadshowMemberBean.DataBean data;// 成员数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadshow_member, container, false);
        ivFavicon = (CircleImageView) view.findViewById(R.id.iv_favicon);// 头像
        tvName = (TextView) view.findViewById(R.id.tv_name);// 姓名
        tvCompNamePosition = (TextView) view.findViewById(R.id.tv_compName_position);// 公司和职位
        tvAddr = (TextView) view.findViewById(R.id.tv_addr);// 地址
        btnEmail = (TextView) view.findViewById(R.id.btn_email);// 邮箱按钮
        btnEmail.setOnClickListener(this);
        btnTel = (TextView) view.findViewById(R.id.btn_tel);// 电话
        btnTel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getMemberData(RoadshowMemberBean.DataBean bean) {
        data = bean;
        if (data != null) {
            initData();
        }
    }

    private void initData() {
        Glide.with(mContext)
                .load(data.getIcon())
//                .placeholder(R.mipmap.ic_default_favicon)
                .error(R.mipmap.ic_default_favicon)
                .into(ivFavicon);
        tvName.setText(data.getName());
        tvCompNamePosition.setText(data.getCompany() + data.getPosition());
        tvAddr.setText(data.getAddress());
        btnEmail.setText(data.getEmial());
        btnTel.setText(data.getTelephone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_email:
                break;
            case R.id.btn_tel:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + this.data.getTelephone());
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
