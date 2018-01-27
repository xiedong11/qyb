package com.zhuandian.qxe.ExtralModule.wkzxs;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 谢栋 on 2016/10/15
 */
public class WSLActivity extends QYBActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;


    @Override
    public void setContent() {
        setContentView(R.layout.wsl_wklist);
    }

    @Override
    public void setupView() {
        toolbarTitle.setText("文史楼");
    }

    @Override
    public void setModle() {

    }



    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
