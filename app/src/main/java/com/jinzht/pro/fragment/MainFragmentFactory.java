package com.jinzht.pro.fragment;

import com.jinzht.pro.base.BaseFragment;

import java.util.HashMap;

/**
 * 主页4个模块的Fragment工厂类
 */
public class MainFragmentFactory {
    // 通过一个HashMap，将已经创建出来的fragment进行保存，以便下次使用
    private static HashMap<Integer, BaseFragment> savedFragment = new HashMap<Integer, BaseFragment>();

    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = savedFragment.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new ProjectFragment();
                    break;
                case 1:
                    fragment = new InvestorFragment();
                    break;
                case 2:
                    fragment = new CircleFragment();
                    break;
                case 3:
                    fragment = new ActivityFragment();
                    break;
            }
            savedFragment.put(position, fragment);
        }
        return fragment;
    }

}
