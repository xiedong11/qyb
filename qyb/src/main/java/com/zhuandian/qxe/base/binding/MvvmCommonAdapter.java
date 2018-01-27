package com.zhuandian.qxe.base.binding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MvvmCommonAdapter extends RecyclerView.Adapter<MvvmCommonAdapter.CommonHolder> {

    protected Context mContext;
    //所有 item 的数据集合
    protected List mDatas;
    //item 布局文件 id
    protected int mLayoutId;
    protected LayoutInflater mInflater;
    // mvvm绑定的viewModel引用
    private int mVariableId;
    private OnItemClickListener clickListener;

    //构造方法
    public MvvmCommonAdapter(List datas, int variableId, Context context, int layoutId) {
        mContext = context;
        mDatas = datas;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);
        mVariableId = variableId;
    }


    public List getDatas() {
        return mDatas;
    }

    public void setDatas(List mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);
        CommonHolder myHolder = new CommonHolder(binding.getRoot());
        myHolder.setBinding(binding);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, final int position) {
        holder.binding.setVariable(mVariableId, mDatas.get(position));
        holder.binding.executePendingBindings();
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(view, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class CommonHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public CommonHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }
    }
}