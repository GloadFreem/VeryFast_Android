package com.jinzht.pro1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.view.CircleImageView;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_email:
                break;
            case R.id.btn_tel:
                break;
        }
    }

    // 获取成员

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

}
