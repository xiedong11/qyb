package com.zhuandian.qxe.utils.myUtils;

import android.util.Log;

/**
 * 自定义的Log打印类，软件发布时把debug设置false
 * Created by 谢栋 on 2016/12/24.
 */
public class MyL {
    private static final String TAG="qyb";
    private static boolean debug= true;   //全局调试开关，默认为true

    /**
     * debug模式下，打印所有调试信息(声明为static表示不要实例化就可以使用)
     * @param msg  要打印的信息
     */
    public static void e(String msg){
        if(debug){
            Log.e(TAG,msg);
        }
    }
}
