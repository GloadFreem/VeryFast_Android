package com.jinzht.pro1.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.InvestorFragmentAdapter;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.utils.UiUtils;

/**
 * 投资人界面
 */
public class InvestorFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup investorRgTab;// 选择投资人类型的RadioGroup
    private RadioButton investorRbtnInvestor;// 投资人
    private RadioButton investorRbtnInvestorg;// 投资机构
    private RadioButton investorRbtnBrain;// 智囊团
    private ViewPager investorVpType;// 投资人类型界面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor, container, false);
        investorRgTab = (RadioGroup) view.findViewById(R.id.investor_rg_tab);// 选择投资人类型的RadioGroup页签
        investorRbtnInvestor = (RadioButton) view.findViewById(R.id.investor_rbtn_investor);// 投资人
        investorRbtnInvestorg = (RadioButton) view.findViewById(R.id.investor_rbtn_investorg);// 投资机构
        investorRbtnBrain = (RadioButton) view.findViewById(R.id.investor_rbtn_brain);// 智囊团
        investorVpType = (ViewPager) view.findViewById(R.id.investor_vp_type);// 投资人类型界面
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置RadioGroup页签的单选事件
        investorRgTab.setOnCheckedChangeListener(this);
        // 给投资人类型填充数据
        investorVpType.setAdapter(new InvestorFragmentAdapter(getChildFragmentManager()));
        investorVpType.setCurrentItem(0);
        // 设置RadioGroup页签和项目ViewPager联动
        investorVpType.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        investorRgTab.check(R.id.investor_rbtn_investor);
                        break;
                    case 1:
                        investorRgTab.check(R.id.investor_rbtn_investorg);
                        break;
                    case 2:
                        investorRgTab.check(R.id.investor_rbtn_brain);
                        break;
                }
            }
        });
    }

    // 设置RadioGroup页签的单选事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.investor_rbtn_investor:// 选择了投资人
                investorVpType.setCurrentItem(0);
                investorRbtnInvestor.setTextColor(UiUtils.getColor(R.color.custom_orange));
                investorRbtnInvestorg.setTextColor(UiUtils.getColor(R.color.bg_text));
                investorRbtnBrain.setTextColor(UiUtils.getColor(R.color.bg_text));
                break;
            case R.id.investor_rbtn_investorg:// 选择了投资机构
                investorVpType.setCurrentItem(1);
                investorRbtnInvestor.setTextColor(UiUtils.getColor(R.color.bg_text));
                investorRbtnInvestorg.setTextColor(UiUtils.getColor(R.color.custom_orange));
                investorRbtnBrain.setTextColor(UiUtils.getColor(R.color.bg_text));
                break;
            case R.id.investor_rbtn_brain:// 选择了智囊团
                investorVpType.setCurrentItem(2);
                investorRbtnInvestor.setTextColor(UiUtils.getColor(R.color.bg_text));
                investorRbtnInvestorg.setTextColor(UiUtils.getColor(R.color.bg_text));
                investorRbtnBrain.setTextColor(UiUtils.getColor(R.color.custom_orange));
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
