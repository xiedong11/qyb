package com.zhuandian.qxe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.Comment;
import com.zhuandian.qxe.bean.HeartShare;
import com.zhuandian.qxe.bean.Myuser;
import com.zhuandian.qxe.utils.myUtils.MyL;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 谢栋 on 2016/12/28.
 */
public class HeartShareAdapter extends BaseAdapter{

    private List<HeartShare> mDatas;
    private LayoutInflater inflater;

    /**
     * 构造方法
     * @param context
     * @param mDatas
     */
    public HeartShareAdapter(Context context ,List<HeartShare> mDatas) {
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

        ViewHolder viewHolder=null;

        if(convertView==null){

            convertView = inflater.inflate(R.layout.heart_share_item,null);
            viewHolder = new ViewHolder();

            viewHolder.username = (TextView) convertView.findViewById(R.id.username);
            viewHolder.shareType = (TextView) convertView.findViewById(R.id.share_type);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.likes = (TextView) convertView.findViewById(R.id.likes);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HeartShare heartShare = mDatas.get(position);   //得到当前position对应的业务类




        //给控件装值
        viewHolder.username.setText(heartShare.getUsername());
        viewHolder.shareType.setText(heartShare.getContentType());
        setCommentCount(heartShare.getObjectId(),viewHolder.comment);   //评论个数
        viewHolder.content.setText(heartShare.getContent());
        setLikesCount(heartShare.getObjectId(),viewHolder.likes);   //设置点赞个数

        MyL.e("创建Time----"+heartShare.getCreatedAt()+"系统时间--"+ MyUtils.currentTime());
//        创建Time----2016-12-30 10:46:44系统时间--2016-12-30 10:54:03
        String createtTime [] = heartShare.getCreatedAt().split(" ");
        String currentTime [] = MyUtils.currentTime().split(" ");

        //判断创建时间跟当前时间是否同一天，是，只显示时间，不是，显示创建的日期，不显示时间
        if(createtTime[0].equals(currentTime[0])) {
            String createtTime1 [] = createtTime[1].split(":");
            viewHolder.time.setText("今天 "+createtTime1[0]+":"+createtTime1[1]);
        }else{
            String createtTime1 [] = createtTime[0].split("-");  //正则切割月份
            String createtTime2 [] = createtTime[1].split(":");  //正则切割时间
            viewHolder.time.setText(createtTime1[1]+"/" +createtTime1[2]+" "+createtTime2[0]+":"+createtTime2[1]);

        }

        return convertView;
    }

    /**
     * 设置点赞的个数
     * @param objectId
     * @param likes
     */
    private void setLikesCount(String objectId, final TextView likes) {

        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        BmobQuery<Myuser> query = new BmobQuery<Myuser>();
        HeartShare post = new HeartShare();
        post.setObjectId(objectId);
//likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("likes", new BmobPointer(post));
        query.findObjects(new FindListener<Myuser>() {

            @Override
            public void done(List<Myuser> object,BmobException e) {
                if(e==null){
                    MyL.e("查询个数：" + object.size());
                    likes.setText(object.size()+"");   //设置点赞个数
                }else{
                    MyL.e("失败：" + e.getMessage());
                }
            }

        });
    }


    /**
     * 得到动态相关的评论个数
     * @param objectId
     * @return
     */
    private void setCommentCount(String objectId, final TextView countView) {



        BmobQuery<Comment> query = new BmobQuery<Comment>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        HeartShare post = new HeartShare();
        post.setObjectId(objectId);   //得到当前动态的Id号，
        query.addWhereEqualTo("heartshare",new BmobPointer(post));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("myuser,heartshare.auther");
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {

                if(e==null) {
                    countView.setText(objects.size()+"");


                }else{
                    MyL.e("查询数据失败");
                }
            }
        });

    }


    class ViewHolder {
        private TextView username;
        private TextView shareType;
        private TextView time;
        private TextView content;
        private TextView likes;
        private TextView comment;

    }
}
