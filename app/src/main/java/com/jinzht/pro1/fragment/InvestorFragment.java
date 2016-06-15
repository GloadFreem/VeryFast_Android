package com.jinzht.pro1.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.InvestorFragmentAdapter;
import com.jinzht.pro1.base.BaseFragment;

/**
 * 投资人界面
 */
public class InvestorFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout btnInvestor;// 投资人按钮
    private View lineInvestor;// 投资人底线
    private RelativeLayout btnInvestorg;// 投资机构按钮
    private View lineInvestorg;// 投资机构底线
    private RelativeLayout btnBrain;// 智囊团按钮
    private View lineBrain;// 智囊团底线
    private ViewPager investorVpType;// 投资人类型界面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor, container, false);
        btnInvestor = (RelativeLayout) view.findViewById(R.id.btn_investor);// 投资人按钮
        btnInvestor.setOnClickListener(this);
        lineInvestor = view.findViewById(R.id.line_investor);// 投资人底线
        btnInvestorg = (RelativeLayout) view.findViewById(R.id.btn_investorg);// 投资机构按钮
        btnInvestorg.setOnClickListener(this);
        lineInvestorg = view.findViewById(R.id.line_investorg);// 投资机构底线
        btnBrain = (RelativeLayout) view.findViewById(R.id.btn_brain);// 智囊团按钮
        btnBrain.setOnClickListener(this);
        lineBrain = view.findViewById(R.id.line_brain);// 智囊团底线
        investorVpType = (ViewPager) view.findViewById(R.id.investor_vp_type);// 投资人类型界面
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 给投资人类型填充数据
        investorVpType.setAdapter(new InvestorFragmentAdapter(getChildFragmentManager()));
        investorVpType.setOffscreenPageLimit(3);
        investorVpType.setCurrentItem(0);
        // 设置RadioGroup页签和项目ViewPager联动
        investorVpType.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        lineInvestor.setVisibility(View.VISIBLE);
                        lineInvestorg.setVisibility(View.INVISIBLE);
                        lineBrain.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        lineInvestor.setVisibility(View.INVISIBLE);
                        lineInvestorg.setVisibility(View.VISIBLE);
                        lineBrain.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        lineInvestor.setVisibility(View.INVISIBLE);
                        lineInvestorg.setVisibility(View.INVISIBLE);
                        lineBrain.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_investor:// 点击了投资人
                investorVpType.setCurrentItem(0);
                lineInvestor.setVisibility(View.VISIBLE);
                lineInvestorg.setVisibility(View.INVISIBLE);
                lineBrain.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_investorg:// 点击了投资机构
                investorVpType.setCurrentItem(1);
                lineInvestor.setVisibility(View.INVISIBLE);
                lineInvestorg.setVisibility(View.VISIBLE);
                lineBrain.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_brain:// 点击了智囊团
                investorVpType.setCurrentItem(2);
                lineInvestor.setVisibility(View.INVISIBLE);
                lineInvestorg.setVisibility(View.INVISIBLE);
                lineBrain.setVisibility(View.VISIBLE);
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
