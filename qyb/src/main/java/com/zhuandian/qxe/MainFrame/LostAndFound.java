package com.zhuandian.qxe.MainFrame;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;
import com.zhuandian.qxe.entity.LostAndFoundEntity;
import com.zhuandian.qxe.service.QYBApplication;
import com.zhuandian.qxe.utils.myUtils.MyL;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 失物招领activity，记录用户需要广播的内容
 * Created by 谢栋 on 2017/1/14.
 */
public class LostAndFound extends QYBActivity {


    private EditText broadContent;
    private SweetAlertDialog pDialog;
    private String username;


    @Override
    public void setContent() {
        setContentView(R.layout.lost_and_found);
    }

    @Override
    public void setupView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("发布广播");
        broadContent = (EditText) findViewById(R.id.broadcast_content);
        //得到用户名
        QYBApplication application = (QYBApplication) this.getApplication();
        username = application.getUsername(this);

        //设置支持Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //定义对话框
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("提交中...");
        pDialog.setCancelable(false);


        //设置发布广播的监听事件
        ((TextView) findViewById(R.id.submit_broadcast)).setOnClickListener(this);

    }

    @Override
    public void setModle() {

    }





    @Override
    public void onClick(View v) {
        final String broadStr = broadContent.getText().toString();   //得到输入框中的内容
        if (!"".equals(broadStr)) {
            //执行后台存储操作
            pDialog.show();
            LostAndFoundEntity myBroadcast = new LostAndFoundEntity();
            myBroadcast.setBroadcastContent(broadStr);
            myBroadcast.setUsername(username);
            myBroadcast.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {

                        pDialog.dismiss();
                        Snackbar.make(broadContent, "曲园广播台已收到消息...", Snackbar.LENGTH_LONG).show();
                        MyL.e("存取成功" + broadStr);
                    } else {
                        MyL.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        } else {
            Snackbar.make(broadContent, "广播内容不允许为空...", Snackbar.LENGTH_LONG).show();
        }

    }
}
