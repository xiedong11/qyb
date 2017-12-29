package com.zhuandian.qxe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.Comment;
import com.zhuandian.qxe.utils.myUtils.MyL;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import java.util.List;

/**
 * 动态相关评论的adapter
 * Created by 谢栋 on 2017/1/12.
 */
public class UserCommentAdapter extends BaseAdapter{

    private List<Comment> mDatas;
    private LayoutInflater inflater;

    public UserCommentAdapter(Context context, List<Comment> mDatas) {
        this.mDatas = mDatas;
        this.inflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){

            convertView = inflater.inflate(R.layout.comment_listview_item,null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content = (TextView) convertView.findViewById(R.id.content);

           convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyL.e("创建Time----"+mDatas.get(position).getCreatedAt()+"系统时间--"+ MyUtils.currentTime());
//        创建Time----2016-12-30 10:46:44系统时间--2016-12-30 10:54:03
        String createtTime [] = mDatas.get(position).getCreatedAt().split(" ");
        String currentTime [] = MyUtils.currentTime().split(" ");

        //判断创建时间跟当前时间是否同一天，是，只显示时间，不是，显示创建的日期，不显示时间
        if(createtTime[0].equals(currentTime[0])) {
            String createtTime1 [] = createtTime[1].split(":");
            holder.time.setText("今天 "+createtTime1[0]+":"+createtTime1[1]);
        }else{
            String createtTime1 [] = createtTime[0].split("-");  //正则切割月份
            String createtTime2 [] = createtTime[1].split(":");  //正则切割时间
            holder.time.setText(createtTime1[1]+"/" +createtTime1[2]+" "+createtTime2[0]+":"+createtTime2[1]);

        }
        holder.content.setText(mDatas.get(position).getContent());   //设置评论内容
        holder.name.setText(mDatas.get(position).getMyuser().getUsername());   //设置评论者信息

        return convertView;
    }


    class ViewHolder{
        TextView time;
        TextView name;
        TextView content;
    }
}
