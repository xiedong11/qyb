package com.zhuandian.qxe.service;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.zhuandian.qxe.chat.CustomUserProvider;

import cn.bmob.v3.Bmob;
import cn.leancloud.chatkit.LCChatKit;

/**
 * 软件全局配置类，
 * 1.用户初始化全局参数，
 * 2.初始化第三方框架
 * Created by 谢栋 on 2017/1/2.
 */
public class QYBApplication extends Application {

    // LeanCloud  appId、appKey
    private final String APP_ID = "XL0csHhKUXpGOtFspgAJa4RJ-gzGzoHsz";
    private final String APP_KEY = "7sckd1rcz1wcMkmzK1fq53v8";
    private static Context mContext;  //全局通用的context

    public static Context getAPPContext() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);

        //初始化Bmob的SDK
        Bmob.initialize(this, "df25a6c6a79479d11a60f2e89c68b467");
    }


    //获取在注册界面得到的sharedpreferences对象存放的内容
    public static String getUsername(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sp.getString("username", "匿名用户");
        return username;
    }
}
