package com.zhuandian.qxe.GuideView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhuandian.qxe.MenuActivity;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;
import com.zhuandian.qxe.service.LoginActivity;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * 软件第一次启动时启动该类
 * Created by 谢栋 on 2016/8/30.
 */
public class SpalshActivity extends QYBActivity {
    private static int DELAY = 2000;
    @BindView(R.id.version)
    TextView version;
    private BmobUser bmobUser;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void setupView() {
        version.setText(MyUtils.getVersionName(this));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void setModle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                //把当前安装包的VersionCode写入用户配置文件
                SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
                if (sp.getInt("versionCode", 0) < MyUtils.getVersionCode(SpalshActivity.this)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("versionCode", MyUtils.getVersionCode(SpalshActivity.this));
                    editor.commit();
                    startActivity(new Intent(SpalshActivity.this, GuideView.class));
                    SpalshActivity.this.finish();
                } else {
                    //判断是否存在当前用户
                    bmobUser = BmobUser.getCurrentUser();
                    if (bmobUser != null) {
                        Log.i("xie", "有用户缓存记录");
                        intent = new Intent(SpalshActivity.this, MenuActivity.class);
                    } else {
                        Log.i("xie", "没有用户登录缓存记录");
                        intent = new Intent(SpalshActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    SpalshActivity.this.finish();
                }
            }
        }, DELAY);

    }


}
