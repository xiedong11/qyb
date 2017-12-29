package com.zhuandian.qxe.ExtralModule.wkzxs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.zhuandian.qxe.R;

/**
 * Created by 谢栋 on 2016/10/15.
 */
public class ZHLActivity extends ActionBarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhl_wklist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView)findViewById(R.id.toolbar_title)).setText("综合楼");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    /**
     * 菜单监听事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
//        左上角返回菜单的键值为android.R.id.home
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
