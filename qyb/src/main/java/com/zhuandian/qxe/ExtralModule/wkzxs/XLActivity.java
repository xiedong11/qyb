package com.zhuandian.qxe.ExtralModule.wkzxs;

import android.os.Bundle;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 谢栋 on 2016/10/15.
 */
public class XLActivity extends QYBActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    public void setContent() {
        setContentView(R.layout.xl_wklist);
    }

    @Override
    public void setupView() {
        toolbarTitle.setText("西联");
    }

    @Override
    public void setModle() {

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
