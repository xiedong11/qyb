package com.zhuandian.qxe.entity;

import cn.bmob.v3.BmobObject;

/**
 * 失物招领内容的业务对象
 * Created by 谢栋 on 2017/1/14.
 */
public class LostAndFoundEntity extends BmobObject{

    private String broadcastContent;   //用户需要广播的内容
    private String username;        //上传广播信息的用户信息

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBroadcastContent() {
        return broadcastContent;
    }

    public void setBroadcastContent(String broadcastContent) {
        this.broadcastContent = broadcastContent;
    }
}
