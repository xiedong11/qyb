package com.zhuandian.qxe.ExtralModule.wkzxs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 谢栋 on 2016/10/15.
 */
public class ZHLActivity extends QYBActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    public int getLayoutId() {
        return R.layout.zhl_wklist;
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
