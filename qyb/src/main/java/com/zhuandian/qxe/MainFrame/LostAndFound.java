package com.zhuandian.qxe.MainFrame;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;
import com.zhuandian.qxe.entity.LostAndFoundEntity;
import com.zhuandian.qxe.service.QYBApplication;
import com.zhuandian.qxe.utils.myUtils.MyL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 失物招领activity，记录用户需要广播的内容
 * Created by 谢栋 on 2017/1/14.
 */
public class LostAndFound extends QYBActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private EditText broadContent;
    private SweetAlertDialog pDialog;
    private String username;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lost_and_found;
    }

    @Override
    public void setupView() {
        toolbarTitle.setText("发布广播");
        broadContent = (EditText) findViewById(R.id.broadcast_content);
        //得到用户名
        QYBApplication application = (QYBApplication) this.getApplication();
        username = application.getUsername(this);
        //定义对话框
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("提交中...");
        pDialog.setCancelable(false);
    }

    @Override
    public void setModle() {

    }


    @OnClick({R.id.iv_back, R.id.submit_broadcast})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.submit_broadcast:
                submitBroadcast();
                break;
        }
    }

    /**
     * 提交广播事物
     */
    public void submitBroadcast() {
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
