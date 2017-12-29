package com.zhuandian.qxe.GuideView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhuandian.qxe.MenuActivity;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.service.LoginActivity;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * 软件第一次启动时启动该类
 * Created by 谢栋 on 2016/8/30.
 */
public class StartFirst extends Activity {
    private static int DELAY = 2000;
    private BmobUser bmobUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //初始化Bmob的SDK
        Bmob.initialize(this, getString(R.string.bmobkey));

        //初始化后台统计功能
        cn.bmob.statistics.AppStat.i(getString(R.string.bmobkey), null);

        //设置版本号
        ((TextView) findViewById(R.id.version)).setText(MyUtils.getVersionName(this));
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                //把当前安装包的VersionCode写入用户配置文件
                SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);

                if (sp.getInt("versionCode", 0) < MyUtils.getVersionCode(StartFirst.this)) {

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("versionCode", MyUtils.getVersionCode(StartFirst.this));
                    editor.commit();

                    startActivity(new Intent(StartFirst.this, GuideView.class));
                    StartFirst.this.finish();

                } else {

                    //判断是否存在当前用户
                    bmobUser = BmobUser.getCurrentUser();
                    if (bmobUser != null) {
                        Log.i("xie", "有用户缓存记录");
                        intent = new Intent(StartFirst.this, MenuActivity.class);
                    } else {
                        Log.i("xie", "没有用户登录缓存记录");
                        intent = new Intent(StartFirst.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    StartFirst.this.finish();
                }
            }
        }, DELAY);


    }
}
