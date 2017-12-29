package com.zhuandian.qxe.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 谢栋 on 2016/12/28.
 */
public class Comment extends BmobObject{
    private String content;
    private Myuser myuser;    //评论的用户，Pointer类型，一对一关系
    private HeartShare heartshare;    //所评论的动态，这里体现的是一对多的关系，一个评论只能属于一个动态


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Myuser getMyuser() {
        return myuser;
    }

    public void setMyuser(Myuser myuser) {
        this.myuser = myuser;
    }

    public HeartShare getHeartshare() {
        return heartshare;
    }

    public void setHeartshare(HeartShare heartshare) {
        this.heartshare = heartshare;
    }
}
