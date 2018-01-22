package com.zhuandian.qxe.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by 谢栋 on 2016/12/28.
 */
public class CommentEntity extends BmobObject{
    private String content;
    private UserEntity userEntity;    //评论的用户，Pointer类型，一对一关系
    private HeartShareEntity heartshare;    //所评论的动态，这里体现的是一对多的关系，一个评论只能属于一个动态


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public HeartShareEntity getHeartshare() {
        return heartshare;
    }

    public void setHeartshare(HeartShareEntity heartshare) {
        this.heartshare = heartshare;
    }
}
