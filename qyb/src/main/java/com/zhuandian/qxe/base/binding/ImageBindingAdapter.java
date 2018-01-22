package com.zhuandian.qxe.base.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * desc：图片控件绑定的适配器
 * author：xiedong
 * date：2018/1/22.
 */
public class ImageBindingAdapter {
    @BindingAdapter(value = {"url", "placeHolder"}, requireAll = false)
    public static void loadImage(ImageView view, String url, Drawable placeholderImageRes) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(view.getContext()).load(url).dontAnimate().placeholder(placeholderImageRes).into(view);
    }
}
