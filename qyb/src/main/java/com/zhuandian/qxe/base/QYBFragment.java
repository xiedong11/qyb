package com.zhuandian.qxe.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by 谢栋 on 2017/5/13.
 */

public abstract class QYBFragment extends Fragment {

    private View rootView;
    private LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, rootView);  //调整绑定顺序在setUpView之前
            setupView();
            setModle();
        }
        return rootView;
    }


    protected abstract void setupView();

    protected abstract int getLayoutId();

    protected abstract void setModle();
}


