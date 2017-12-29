package com.zhuandian.qxe.ExtralModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandian.qxe.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/10/20.
 */
public class CommunityActivity extends ActionBarActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_activity);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView)findViewById(R.id.toolbar_title)).setText("校园活动");

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        ((LinearLayout)findViewById(R.id.update_activity)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(CommunityActivity.this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("无权提交！")
                        .setContentText("请联系客服升级为VIP用户！！")
                        .show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //监听android中的按键返回事件
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.i("xeeer","keycode---"+event);
        if(keyCode==KeyEvent.KEYCODE_APP_SWITCH){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
