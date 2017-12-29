package com.zhuandian.qxe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 学校新闻资讯adapter
 * Created by 谢栋 on 2017/1/30.
 */
public class NewsViewPagerAdapter extends FragmentPagerAdapter{


    private List<Fragment> fragmentList;
    private String titles [] = {"学校要闻","校园传真","通知共告","媒体关注"};

    public NewsViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * 得到当前页卡的导航条标题
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
