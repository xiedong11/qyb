package com.zhuandian.qxe.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuandian.qxe.utils.TUtil;

import butterknife.ButterKnife;

/**
 * Created by 谢栋 on 2017/5/13.
 */

public abstract class QYBFragment<VM extends BaseViewModel, B extends ViewDataBinding> extends Fragment {
    public QYBActivity activity;
    public QYBFragment fragment;
    private View rootView;
    private LayoutInflater mInflater;
    public VM viewModel;
    public B binding;
    private boolean isFirstRun;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (QYBActivity) getActivity();
        fragment = this;
//        viewModel.setContext(activity, binding);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        if (TUtil.hasParameterizedTye(this)) {
            return getRootView(inflater, container);
        } else {
            if (rootView == null) {
                rootView = inflater.inflate(getLayoutId(), container, false);
                ButterKnife.bind(this, rootView);  //调整绑定顺序在setUpView之前
                initView();
            }
            return rootView;
        }

    }

    /**
     * 通过数据绑定获取rootview
     *
     * @param inflater  inflater
     * @param container container
     * @return rootView
     */
    private View getRootView(LayoutInflater inflater, @Nullable ViewGroup container) {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            viewModel = TUtil.getT(this, 0);
            viewModel.setContext(activity, binding);
            rootView = binding.getRoot();
            isFirstRun = true;
        } else {
            isFirstRun = false;
        }
        initView();
        return rootView;
    }

    /**
     * 装载数据
     */
    private void initView() {
        setupView();
        setModle();
    }

    protected abstract void setupView();

    protected abstract int getLayoutId();

    protected  void setModle(){};
}



