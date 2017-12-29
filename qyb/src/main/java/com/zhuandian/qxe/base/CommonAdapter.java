package com.zhuandian.qxe.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xiedong on 2017/7/17.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflater;
    protected Context context;
    protected List<T> datas;
    private int layoutId;


    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.datas = datas;
        this.layoutId = layoutId;

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(context, convertView, parent, layoutId, position);
        convert(holder, getItem(position));
        return null;
    }

    protected abstract void convert(ViewHolder holder, T t);
}
