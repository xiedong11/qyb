package com.zhuandian.qxe.MainFrame.schoolNews;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.adapter.NewsViewPagerAdapter;
import com.zhuandian.qxe.base.QYBFragment;
import com.zhuandian.qxe.utils.myUtils.MyL;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 新闻资讯各个模块的container
 * Created by 谢栋 on 2017/1/31.
 */
public class NewsMainActivity extends QYBFragment {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    private List<Fragment> fragmentList = new ArrayList<>();
    @Override
    protected void setupView() {
        ((TextView) getActivity().findViewById(R.id.navigation_text)).setText("曲园快讯");
        //添加新闻资讯页
        fragmentList.add(CommonNewsFragment.getInstance("XXYW", "http://www.qfnu.edu.cn/xxyw.htm"));
        fragmentList.add(CommonNewsFragment.getInstance("XYCZ", "http://www.qfnu.edu.cn/xxyw.htm"));
        fragmentList.add(CommonNewsFragment.getInstance("TZGG", "http://www.qfnu.edu.cn/tzgg.htm"));
        fragmentList.add(CommonNewsFragment.getInstance("MTGZ", "http://www.qfnu.edu.cn/xxyw.htm"));
        viewPager.setAdapter(new NewsViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList));
        tabLayout.post(new Runnable() {   //开线程解决TabLayout加载时的一小段延时，导致页面标题不同步
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);  //设置ViewPager与TabLayout联动
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_main_container;
    }

    @Override
    protected void setModle() {

    }

}
