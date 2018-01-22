package com.zhuandian.qxe.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 用户存放用户心语共享的业务类
 * 实现Serializable接口，用于动态详情跳转时传递对象
 *
 * Created by 谢栋 on 2016/12/28.
 */
public class HeartShareEntity extends BmobObject  implements Serializable {

    private String username;
    private String content;
    private String contentType;
    private UserEntity author;       //发布动态的作者，一对一的思想
    private BmobRelation likes;    //存放喜欢该动态的用户（点赞人数）
    private int commentCount;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
