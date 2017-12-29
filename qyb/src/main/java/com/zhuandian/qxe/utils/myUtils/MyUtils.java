package com.zhuandian.qxe.utils.myUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.qxe.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类
 * Created by 谢栋 on 2016/9/5.
 */
public class MyUtils {
    private static Toast toast;


    public static String currentTime()
    {
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return time;
    }


    /**
     *
     * @param data  传入在当前时间基础上减少的数值
     * @return   变换之后的数值
     */
    public static String decreaseTime(int data)
    {
        long curren=System.currentTimeMillis();
        curren-=data * 60 * 1000;
        Date da=new Date(curren);
        SimpleDateFormat dataFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time=dataFormat.format(da);
        return time;
    }


    /**
     * 获取当前软件的版本号
     * @param context  传入上下文参数
     * @return  版本号
     */
    public static String getVersionName(Context context){
        String versionName ="";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(),0);

            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }


    /**
     * 获取当前软件的VersionCode
     * @param context  传入上下文参数
     * @return  VersionCode
     */
    public static int getVersionCode(Context context){
        int versionCode =0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(),0);

            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }


    /**
     * 设置Snackbar上的字体颜色
     * @param snackbar
     * @param color
     */
    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    /**
     * 封装Toast，防止用户点击时，Toast重复弹起，严重影响用户体验
     * @param context
     * @param content
     */
    public static void showToast(Context context,String content){
        if (toast==null){   //保证Toast只弹出一次
            toast=Toast.makeText(context,content,Toast.LENGTH_LONG);
        }else {
            toast.setText(content);
        }
        toast.show();
    }
}
