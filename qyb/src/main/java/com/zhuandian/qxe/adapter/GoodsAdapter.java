package com.zhuandian.qxe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.qxe.bean.GoodsBean;

import java.util.List;

/**
 * Created by 谢栋 on 2016/8/13.
 */
public class GoodsAdapter extends BaseAdapter {

    private List<GoodsBean> mDatas;
    private LayoutInflater   mInflater;

    /**
     * 构造方法
     * @param datas
     */
    public GoodsAdapter(Context context , List<GoodsBean> datas ) {
       mInflater =LayoutInflater.from(context);
        mDatas   =datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 返回listview的item
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){

        }

        return null;
    }


    //ViewHolder加载布局控件
    private class ViewHolder{
        private TextView titleTextView;
        private TextView contentTextView;
        private ImageView mImageView;
        private TextView priceTextView;

    }
}
