package com.zhuandian.qxe.base;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.zhuandian.qxe.utils.AppManager;
import com.zhuandian.qxe.utils.TUtil;

import butterknife.ButterKnife;

/**
 * Created by 谢栋 on 2017/5/13.
 */

public abstract class QYBActivity<VM extends BaseViewModel, B extends ViewDataBinding> extends FragmentActivity implements ActivityPageSetting, View.OnClickListener {
    public VM viewModel;
    public B binding;
    public QYBActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        AppManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initData();
        setupView();
        setModle();
    }


    public void initData() {
        activity = this;
        initMvvm();
    }

    private void initMvvm() {
        if (TUtil.hasParameterizedTye(this)) {
            binding = DataBindingUtil.setContentView(this, getLayoutId());
            viewModel = TUtil.getT(this, 0);
            viewModel.setContext(activity, binding);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        AppManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    //    友盟统计相关
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * umeng分享回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
