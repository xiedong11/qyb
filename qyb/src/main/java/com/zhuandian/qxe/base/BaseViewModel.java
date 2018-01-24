package com.zhuandian.qxe.base;

/**
 * desc：控制类基类
 * author：xiedong
 * date：2018/1/24.
 */
public abstract class BaseViewModel<B> {
    public QYBActivity activity;
    public B binding;

    public void setContext(QYBActivity activity, B binding) {
        this.activity = activity;
        this.binding = binding;
    }

    protected abstract void initView();
}
