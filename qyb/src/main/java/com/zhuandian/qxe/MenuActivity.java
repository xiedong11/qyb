package com.zhuandian.qxe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.umeng.analytics.MobclickAgent;
import com.zhuandian.qxe.MainFrame.AboutUsFragrant;
import com.zhuandian.qxe.MainFrame.DataRunFragrant;
import com.zhuandian.qxe.MainFrame.FeedBackFragment;
import com.zhuandian.qxe.MainFrame.HomeFragment;
import com.zhuandian.qxe.MainFrame.SettingFragment;
import com.zhuandian.qxe.MainFrame.WKZXSFragment;
import com.zhuandian.qxe.MainFrame.esGoods.EsGoodsFragment;
import com.zhuandian.qxe.MainFrame.esGoods.UploadGoodsFragment;
import com.zhuandian.qxe.MainFrame.schoolNews.NewsMainActivity;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;

public class MenuActivity extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private MenuActivity mContext;


    //左右主Fragrant选项卡菜单
    private ResideMenuItem itemHome;
    private ResideMenuItem itemESGoods;
    private ResideMenuItem itemWKZXS;
    private ResideMenuItem itemDataRun;
    private ResideMenuItem itemWKZX;
    private ResideMenuItem itemAboutUs;
    private ResideMenuItem itemNews;
    private ResideMenuItem itemFeedBack;
    private ResideMenuItem itemSetting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //初始化Bmob的SDK
        Bmob.initialize(this,"df25a6c6a79479d11a60f2e89c68b467");

        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);

        //在线更新初始化
//        BmobUpdateAgent.initAppVersion();
        BmobUpdateAgent.update(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                // TODO Auto-generated method stub
                //根据updateStatus来判断更新是否成功
                Log.i("xiedong",updateStatus+"状态码");
            }
        });


        mContext = this;
        setUpMenu();
        if( savedInstanceState == null ) {
            changeFragment(new HomeFragment());
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
//        resideMenu.setUse3D(true);

        //替换项目背景
        resideMenu.setBackground(R.drawable.background_5);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);


        //创建菜单item
        itemHome     = new ResideMenuItem(this,R.drawable.goods_trade,       "闲物处理");
        itemDataRun  = new ResideMenuItem(this,R.drawable.data_run,   "约跑步" );
        itemESGoods  = new ResideMenuItem(this,R.drawable.qy_taobao,   "曲园淘宝");
        itemWKZX = new ResideMenuItem(this,R.drawable.wuke_zixi ,   "无课自习");
        itemWKZXS    = new ResideMenuItem(this,R.drawable.wuke_zixi ,  "上自习");
//        itemAboutUs  = new ResideMenuItem(this,R.drawable.icon_settings ,   "关于我们");
        itemNews     = new ResideMenuItem(this,R.drawable.qy_news  ,   "曲园快讯");
//        itemFeedBack = new ResideMenuItem(this,R.drawable.icon_profile ,    "反馈建议");
        itemSetting       = new ResideMenuItem(this,R.drawable.setting  ,       "软件设置");


//        itemFeedBack.setOnClickListener(this);
        itemNews.setOnClickListener(this);
        itemHome.setOnClickListener(this);
        itemWKZX.setOnClickListener(this);
        itemESGoods.setOnClickListener(this);
        itemDataRun.setOnClickListener(this);
        itemWKZXS.setOnClickListener(this);
//        itemAboutUs.setOnClickListener(this);
        itemSetting.setOnClickListener(this);

        resideMenu.addMenuItem(itemESGoods,ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemNews ,ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(itemAboutUs,ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemDataRun,ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(itemFeedBack,ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemWKZX,ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSetting,ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(itemWKZXS,ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        //绑定导航条上的左右按钮的监听事件
        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                changeFragment(new HomeFragment());
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.Share_app_url));
                shareIntent.setType("text/plain");

                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
            }
        });




    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {



        if (view == itemHome){
            changeFragment(new UploadGoodsFragment());
        }else if (view == itemWKZXS){
            changeFragment(new WKZXSFragment());
        }else if (view == itemDataRun){
            changeFragment(new DataRunFragrant());
        }else if (view == itemESGoods){
            changeFragment(new EsGoodsFragment());
        }else if (view == itemAboutUs){
            changeFragment(new AboutUsFragrant());
        }else if (view == itemWKZX){
            changeFragment(new HomeFragment());
        }else if(view==itemNews){
            changeFragment(new NewsMainActivity());
        }else if(view== itemFeedBack){
            changeFragment(new FeedBackFragment());
        }else if(view == itemSetting){
            changeFragment(new SettingFragment());
        }

        resideMenu.closeMenu();
    }

    //监听左右菜单打开关闭
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    //开启Fragrant跳转
    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
//                .addToBackStack("fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }



    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }


   //跟设置fragment中的跳转有冲突
    //防止用户误操作退出软件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();//得到的后退栈数值为零
        Log.i("xiedong",fragmentCount+"count");
        Log.i("xiedong",getSupportFragmentManager().findFragmentByTag("homefragment")+"homefragment");
        //当fragment回退栈中没有fragment存在时，keyBack事件才生效，修复设置界面fragment跳转时，直接退出的Bug
        if(fragmentCount==0) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                MobclickAgent.onKillProcess(mContext);
                exit();

                return false;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    /**
     * 返回键被点击一次提示用户误操作，连续点击两次退出程序
     */
    public void exit() {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MenuActivity.this, "再按一次可就真的退出了咔", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }



    //    友盟统计相关
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
