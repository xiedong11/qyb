package com.zhuandian.qxe.base.binding;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * desc：data binding 基础ViewHolder
 * author：xiedong
 * date：2018/1/18.
 */
public class BaseBindingHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {
    protected V binding;

    public BaseBindingHolder(V binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public V getBinding() {
        return binding;
    }

    public void setBinding(V binding) {
        this.binding = binding;
    }
//    public void onBindViewHolder(BindingHolder holder, int position) {
//        final V item = mItems.get(position);
////        holder.getBinding().setVariable(BR.item, item);
//        holder.getBinding().executePendingBindings();
//    }
}
