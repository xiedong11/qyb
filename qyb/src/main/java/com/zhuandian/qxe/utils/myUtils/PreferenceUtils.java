package com.zhuandian.qxe.utils.myUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhuandian.qxe.base.QYBActivity;
import com.zhuandian.qxe.service.QYBApplication;

/**
 * SharePreference 封装
 * Created by xiedong on 2017/8/22.
 */

public class PreferenceUtils {
    public static final String PREF_NAME = "qyb_config";

    public static boolean getBoolean(String key,boolean defaultvalue){
        SharedPreferences sp = QYBApplication.getAPPContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key,defaultvalue);
    }

    public static void setBoolean(String key,boolean value){
        SharedPreferences sp = QYBApplication.getAPPContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value);
    }

    public static String getString(String key, String defaultvalue){
        SharedPreferences sp = QYBApplication.getAPPContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultvalue);
    }

    public static void setString(String key, String value){
        SharedPreferences sp = QYBApplication.getAPPContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value);
    }

    public static boolean keyExist(String key){
        SharedPreferences sp = QYBApplication.getAPPContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
