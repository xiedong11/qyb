package com.zhuandian.qxe.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.binding.BaseBindingHolder;
import com.zhuandian.qxe.databinding.ItemSecondGoodsBinding;
import com.zhuandian.qxe.entity.GoodsEntity;

import java.util.List;

/**
 * Created by 谢栋 on 2016/8/13.
 */
public class GoodsAdapter extends RecyclerView.Adapter<BaseBindingHolder<ItemSecondGoodsBinding>> {
    private List<GoodsEntity> data;
    private Context context;

    public GoodsAdapter(List<GoodsEntity> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public BaseBindingHolder<ItemSecondGoodsBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSecondGoodsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_second_goods, parent, false);
        return new BaseBindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseBindingHolder<ItemSecondGoodsBinding> holder, int position) {
//holder.getBinding().setVariable()
//        final T item = mItems.get(position);
//        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
