package com.zhuandian.qxe.ExtralModule.heartshare;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.adapter.UserCommentAdapter;
import com.zhuandian.qxe.bean.Comment;
import com.zhuandian.qxe.bean.HeartShare;
import com.zhuandian.qxe.bean.Myuser;
import com.zhuandian.qxe.utils.GlobalVariable;
import com.zhuandian.qxe.utils.myUtils.MyL;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 心语互享详情界面
 * Created by 谢栋 on 2016/12/31.
 */
public class HeartShareItemFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {
    private static boolean LIKES_FLAG = true; //点赞与取消点赞的标志位
    private View view;
    private HeartShare mDatas;
    private TextView shareType, username, time, content, comment, likes;  //布局上的各个文字内容
    private ListView listView;
    private TextView submitComment;
    private EditText commentContent;
    private FloatingActionButton fabButton;
    private LinearLayout submitCommentLayout;
    private SweetAlertDialog pDialog;
    private LinearLayout setOrRemoveLikes;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.heart_share_list_item, null);
        initView();
//        view.setClickable(true);     //把View的click属性设为true，截断点击时间段扩散
        view.setOnTouchListener(this);

        //定义对话框
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("提交中...");
        pDialog.setCancelable(false);

        getAllUserComment();  //得到所有参与该动态评论的用户信息和内容
        return view;
    }


    private void getAllUserComment() {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        HeartShare post = new HeartShare();
        post.setObjectId(mDatas.getObjectId());   //得到当前动态的Id号，
        query.order("-updatedAt");
        query.addWhereEqualTo("heartshare",new BmobPointer(post));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("myuser,heartshare.auther");
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {

                if(e==null) {
                    //得到数据源后直接绑定到listview
                    listView.setAdapter(new UserCommentAdapter(getActivity(), objects));
                }else{
                    MyL.e("查询数据失败");
                }
            }
        });
    }

    /**
     * 初始化各个控件上的数据
     */
    private void initView() {

        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("动态详情");   //更改ToolBar上的标题
        listView = (ListView) view.findViewById(R.id.comment_listview);   //得到上层activity的listview控件
        username = (TextView) view.findViewById(R.id.username);
        shareType = (TextView) view.findViewById(R.id.share_type);
        time = (TextView) view.findViewById(R.id.time);
        content = (TextView) view.findViewById(R.id.content);
        likes = (TextView) view.findViewById(R.id.likes);
        comment = (TextView) view.findViewById(R.id.comment);
        commentContent = (EditText) view.findViewById(R.id.comment_content);
        submitComment = (TextView) view.findViewById(R.id.submit_comment);   //绑定提交评论的监听事件
        submitComment.setOnClickListener(this);
        fabButton = ((FloatingActionButton) view.findViewById(R.id.fab));
        fabButton.setOnClickListener(this);
        submitCommentLayout = ((LinearLayout) view.findViewById(R.id.submit_comment_layout));
        setOrRemoveLikes = ((LinearLayout) view.findViewById(R.id.set_remove_likes));
        setOrRemoveLikes.setOnClickListener(this);

        Bundle bundle = getArguments();
        mDatas = (HeartShare) bundle.getSerializable("item");
        MyL.e(mDatas.getContent());

        //给控件装值
        username.setText(mDatas.getUsername());
        shareType.setText(mDatas.getContentType());
        setCommentCount(mDatas.getObjectId(),comment);  //设置评论个数
        content.setText(mDatas.getContent());
        setLikesCount(mDatas.getObjectId(),likes);  //设置评论个数

//        创建Time----2016-12-30 10:46:44系统时间--2016-12-30 10:54:03
        String createtTime[] = mDatas.getCreatedAt().split(" ");
        String currentTime[] = MyUtils.currentTime().split(" ");

        //判断创建时间跟当前时间是否同一天，是，只显示时间，不是，显示创建的日期，不显示时间
        if (createtTime[0].equals(currentTime[0])) {
            String createtTime1[] = createtTime[1].split(":");
            time.setText("今天 " + createtTime1[0] + ":" + createtTime1[1]);
        } else {
            String createtTime1[] = createtTime[0].split("-");  //正则切割月份
            String createtTime2[] = createtTime[1].split(":");  //正则切割时间
            time.setText(createtTime1[1] + "/" + createtTime1[2] + " " + createtTime2[0] + ":" + createtTime2[1]);

        }

    }

    /**
     * 当前view有能力处理事件
     *
     * @param v
     * @param event
     * @return true  截断事件的传递
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    /**
     * 当前Fragment销毁时被回调
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

           //根据全局跳转flag设置回退时所到达的toolbar上的标题,false,代表全部动态，true代表当前动态
        if(GlobalVariable.SHARE_LIST_FLAG==true) {
            ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("我的动态");
        }else {
            ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("心语互享");
        }

    }


    /**
     * 重写点击事件的监听
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.submit_comment :  //提交评论
                submitUserComment();
                break;

            case R.id.fab :  //新建评论
                listView.setVisibility(View.INVISIBLE);
                submitCommentLayout.setVisibility(View.VISIBLE);
                fabButton.setVisibility(View.INVISIBLE);
                break;

            case R.id.set_remove_likes:
                setAndRemoveLikesCount();
                setLikesCount(mDatas.getObjectId(),likes);
                break;

        }


    }

    /**
     *添加或者擦出点赞状态
     */
    private void setAndRemoveLikesCount() {

        Myuser user = BmobUser.getCurrentUser(Myuser.class);
        HeartShare post = new HeartShare();
        post.setObjectId(mDatas.getObjectId());   //设置当前动态的id
       //将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
        BmobRelation relation = new BmobRelation();
        if(LIKES_FLAG==true) {
            //将当前用户添加到多对多关联中
            relation.add(user);
            LIKES_FLAG = false;
        }else{
            relation.remove(user);
            LIKES_FLAG = true;
        }
      //多对多关联指向`post`的`likes`字段
        post.setLikes(relation);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    MyL.e("点赞状态更改成功"+LIKES_FLAG);
                }else{
                    MyL.e("状态更新失败："+e.getMessage());
                }
            }

        });

    }


    /**
     * 提交当前用户的评论，并且关联到该动态
     */
    private void submitUserComment() {


        String userComment = commentContent.getText().toString();  //得到用户输入框的评论内容
        if (!"".equals(userComment)) {
            pDialog.show();  //打开用户等待对话框
            Myuser user = BmobUser.getCurrentUser(Myuser.class);  //得到当前用户
            HeartShare post = new HeartShare();   //当前动态内容
            post.setObjectId(mDatas.getObjectId());  //得到当前的动态的id，与评论建立关联
            final Comment comment = new Comment();
            comment.setContent(userComment);
            comment.setMyuser(user);
            comment.setHeartshare(post);
            comment.save(new SaveListener<String>() {

                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        pDialog.dismiss();  //评论成功
                        Snackbar.make(view,"评论成功",Snackbar.LENGTH_LONG).show();
                        commentContent.setText(""); //清空输入框，防止用户二次评论时影响用户体验
                        listView.setVisibility(View.VISIBLE);
                        submitCommentLayout.setVisibility(View.INVISIBLE);
                        fabButton.setVisibility(View.VISIBLE);
                        getAllUserComment();  //重新加载一遍数据

                    } else {
                        MyL.e("评论失败");
                    }
                }

            });
        }else{
            Snackbar.make(view,"评论内容不允许为空...",Snackbar.LENGTH_LONG).show();
        }

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
}
