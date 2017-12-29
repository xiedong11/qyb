package com.zhuandian.qxe.GuideView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zhuandian.qxe.MenuActivity;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.service.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import github.chenupt.springindicator.SpringIndicator;

/**
 * 用户引导页
 * 用于初次登录或者版本信息更新后指导用户使用
 * Created by 谢栋 on 2017/2/10.
 */
public class GuideView extends Activity{
    private ViewPager viewPager;
    private List<View> views = new ArrayList<>();
    private SpringIndicator springIndicator;
    private boolean isLastPage = false;
    private boolean isDragPage = false;
    private boolean canJumpPage = true;
    private BmobUser bmobUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.guideview);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        springIndicator=(SpringIndicator) findViewById(R.id.indicator);


        views.add(LayoutInflater.from(this).inflate(R.layout.guideview_01,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.guideview_02,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.guideview_03,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.guideview_04,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.guideview_05,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.guideview_06,null));

        viewPager.setPageTransformer(true ,new ZoomOutPageTransformer());

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }
        });

        //同步ViewPager的页面切换事件到springIndicator
        springIndicator.setViewPager(viewPager);
        springIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v("AAA",isLastPage+"   "+isDragPage+"   "+positionOffsetPixels);
                if(isLastPage && isDragPage && positionOffsetPixels==0){

                    if(canJumpPage){
                        canJumpPage = false;
                        jumpToNext();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                isLastPage = position== views.size()-1;   //是否为最后一页

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                isDragPage = state==1;   //当前页面是否被继续滑动

            }
        });
    }

    /**
     * 当page为最后一页并且是滑动状态时触发
     */
    private void jumpToNext() {

        Intent intent;
        //判断是否存在当前用户
        bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            Log.i("xie", "有用户缓存记录");
            intent = new Intent(GuideView.this, MenuActivity.class);
        } else {
            Log.i("xie", "没有用户登录缓存记录");
            intent = new Intent(GuideView.this, LoginActivity.class);
        }
        startActivity(intent);
        GuideView.this.finish();

    }
}
