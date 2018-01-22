package com.zhuandian.qxe.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by 谢栋 on 2016/8/10.
 */
public class DataRunEntity extends BmobObject {

    private  int xccSum=0;

    public int getXccSum() {
        return xccSum;
    }

    public void setXccSum(int xccSum) {
        this.xccSum = xccSum;
    }
}
