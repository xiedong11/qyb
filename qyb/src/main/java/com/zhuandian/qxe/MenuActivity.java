package com.zhuandian.qxe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zhuandian.qxe.MainFrame.AboutUsFragrant;
import com.zhuandian.qxe.MainFrame.DataRunFragrant;
import com.zhuandian.qxe.MainFrame.FeedBackFragment;
import com.zhuandian.qxe.MainFrame.HomeFragment;
import com.zhuandian.qxe.MainFrame.SettingFragment;
import com.zhuandian.qxe.MainFrame.WKZXSFragment;
import com.zhuandian.qxe.MainFrame.esGoods.SecondHandGoodsFragment;
import com.zhuandian.qxe.MainFrame.esGoods.old.UploadGoodsFragment;
import com.zhuandian.qxe.MainFrame.schoolNews.NewsMainActivity;
import com.zhuandian.qxe.base.QYBActivity;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;

public class MenuActivity extends QYBActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_main);

        mContext = this;
        setUpMenu();
        if (savedInstanceState == null) {
            changeFragment(new HomeFragment());
        }
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);

        //替换项目背景
        resideMenu.setBackground(R.drawable.background_5);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.6f);

        //创建菜单item
        itemHome = new ResideMenuItem(this, R.drawable.goods_trade, "闲物处理");
        itemDataRun = new ResideMenuItem(this, R.drawable.data_run, "约跑步");
        itemESGoods = new ResideMenuItem(this, R.drawable.qy_taobao, "曲园淘宝");
        itemWKZX = new ResideMenuItem(this, R.drawable.wuke_zixi, "无课自习");
        itemWKZXS = new ResideMenuItem(this, R.drawable.wuke_zixi, "上自习");
        itemNews = new ResideMenuItem(this, R.drawable.qy_news, "曲园快讯");
        itemSetting = new ResideMenuItem(this, R.drawable.setting, "软件设置");

        itemNews.setOnClickListener(this);
        itemHome.setOnClickListener(this);
        itemWKZX.setOnClickListener(this);
        itemESGoods.setOnClickListener(this);
        itemDataRun.setOnClickListener(this);
        itemWKZXS.setOnClickListener(this);
        itemSetting.setOnClickListener(this);
        resideMenu.addMenuItem(itemESGoods, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemNews, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemDataRun, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemWKZX, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSetting, ResideMenu.DIRECTION_RIGHT);
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
                initSocialShare();
            }
        });


    }

    /**
     * 初始化分享功能
     */
    private void initSocialShare() {
        new ShareAction(MenuActivity.this)
                .withText(getString(R.string.Share_app_url))
                .withMedia(new UMImage(MenuActivity.this, R.drawable.ic_launcher))
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.ALIPAY)
                .setCallback(shareListener)
                .open();

        //启用系统分享
//                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.Share_app_url));
//                shareIntent.setType("text/plain");
//
//                //设置分享列表的标题，并且每次都显示分享列表
//                startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(MenuActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MenuActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MenuActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome) {
            changeFragment(new UploadGoodsFragment());
        } else if (view == itemWKZXS) {
            changeFragment(new WKZXSFragment());
        } else if (view == itemDataRun) {
            changeFragment(new DataRunFragrant());
        } else if (view == itemESGoods) {
            changeFragment(new SecondHandGoodsFragment());
        } else if (view == itemAboutUs) {
            changeFragment(new AboutUsFragrant());
        } else if (view == itemWKZX) {
            changeFragment(new HomeFragment());
        } else if (view == itemNews) {
            changeFragment(new NewsMainActivity());
        } else if (view == itemFeedBack) {
            changeFragment(new FeedBackFragment());
        } else if (view == itemSetting) {
            changeFragment(new SettingFragment());
        }

        resideMenu.closeMenu();
    }

    //监听左右菜单打开关闭
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
        }
    };

    //开启Fragrant跳转
    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
//                .addToBackStack("fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    public ResideMenu getResideMenu() {
        return resideMenu;
    }


    //跟设置fragment中的跳转有冲突
    //防止用户误操作退出软件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();//得到的后退栈数值为零
        Log.i("xiedong", fragmentCount + "count");
        Log.i("xiedong", getSupportFragmentManager().findFragmentByTag("homefragment") + "homefragment");
        //当fragment回退栈中没有fragment存在时，keyBack事件才生效，修复设置界面fragment跳转时，直接退出的Bug
        if (fragmentCount == 0) {
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setupView() {

    }

    @Override
    public void setModle() {

    }
}
