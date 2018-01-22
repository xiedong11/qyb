package com.zhuandian.qxe.base.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * desc：图片控件绑定的适配器
 * author：xiedong
 * date：2018/1/22.
 */
public class ImageBindingAdapter {
    @BindingAdapter(value = {"url","placeHolder"} ,requireAll = false)
    public static void loadImage(ImageView view, String url , Drawable placeholderImageRes){

    }
}
