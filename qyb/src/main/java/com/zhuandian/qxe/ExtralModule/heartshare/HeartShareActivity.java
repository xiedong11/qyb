package com.zhuandian.qxe.ExtralModule.heartshare;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

/**
 * Created by 谢栋 on 2016/10/26.
 */
public class HeartShareActivity extends Activity  {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_share);
        initView();

        //把心语互享的listFragment添加进布局
        getFragmentManager()
                .beginTransaction()
                .add(R.id.heart_content,new HeartShareListFragment(),"AllShareFragment")
//                .addToBackStack("fragment")     //不添加进回退栈，点击返回直接退出当前Activity
                .commit();



    }

    /**
     * 初始化activity上的控件
     */
    private void initView() {
        ((TextView)findViewById(R.id.toolbar_title)).setText("心语互享");
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        android.app.ActionBar actionBar =getActionBar();

        toolbar.setNavigationIcon(R.drawable.md_nav_icon);

        //设置左上角导航键的点击监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        toolbar.inflateMenu(R.menu.heart_share_nemu);   //设置溢出菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {



                switch (item.getItemId())
                {


                    case R.id.my_share :

                        //动态跳转到当前用户下的所有动态列表
                        if(getFragmentManager().findFragmentByTag("currentUserShare")==null) {
                            getFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.heart_content, new CurrentUserShareList(), "currentUserShare")
                                    .addToBackStack("currentUserShare")
                                    .commit();
                        }
                        break;
                    case R.id.my_comment:
                        Snackbar snackbar=Snackbar.make(toolbar,"开发者正在移植，请稍后...",Snackbar.LENGTH_SHORT);
                        MyUtils.setSnackbarMessageTextColor(snackbar, Color.parseColor("#ffffff"));
                        snackbar.show();
                        break;
                    case R.id.my_likes :
                        Snackbar snackbar1=Snackbar.make(toolbar,"开发者正在移植，请稍后...",Snackbar.LENGTH_SHORT);
                        MyUtils.setSnackbarMessageTextColor(snackbar1, Color.parseColor("#ffffff"));
                        snackbar1.show();
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //监听左上角的返回箭头
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
