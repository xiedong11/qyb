package com.zhuandian.qxe.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by 谢栋 on 2016/9/2.
 */
public class FeedBackEntity extends BmobObject{

    private String phone;
    private String feedback;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
