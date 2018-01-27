package com.zhuandian.qxe.base.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * desc :RecycerView数据绑定
 * author：xiedong
 * data：2018/1/27
 */

public class BindRecyclerView {
    @BindingAdapter(value = {"adapter", "data", "listener"}, requireAll = false)
    public static <T> void setRecyclerViewData(RecyclerView recyclerView, MvvmCommonAdapter adapter, List<T> mDatas, MvvmCommonAdapter.OnItemClickListener listener) {
        setAdapter(recyclerView, adapter, mDatas);
        setOnItemClickListener(adapter, listener);
    }

    private static <T> void setAdapter(RecyclerView recyclerView, MvvmCommonAdapter adapter, List<T> mDatas) {
//        adapter.setDatas(mDatas);
        recyclerView.setAdapter(adapter);
    }

    private static void setOnItemClickListener(MvvmCommonAdapter adapter, MvvmCommonAdapter.OnItemClickListener listener) {
        if (listener != null) {
            adapter.setClickListener(listener);
        }
    }
}
