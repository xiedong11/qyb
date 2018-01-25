package com.zhuandian.qxe.utils;

import java.lang.reflect.ParameterizedType;

/**
 * desc :泛型对象获取类
 * author：xiedong
 * data：2018/1/25
 */

public class TUtil {
    public static boolean hasParameterizedTye(Object o) {
        return o.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0;
    }

    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
