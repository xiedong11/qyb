package com.zhuandian.qxe.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by 谢栋 on 2016/9/7.
 */
public class Myuser extends BmobUser{

    private String realName;
    private String phone;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
