package com.zhuandian.qxe.utils.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhuandian.qxe.R;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/2/2.
 */

public class HorizontalItem extends RelativeLayout implements View.OnClickListener{

    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivRight;

//    public HorizontalItem(Context context) {
//        super(context);
//    }

    public HorizontalItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将自定义组合控件的布局渲染成View
        View view = View.inflate(context, R.layout.horizontal_item, this);
    //  LayoutInflater.from(context).inflate(R.layout.horizontal_item,this,true);
        Log.i("sbl","======");
        tvLeft = (TextView) view.findViewById(R.id.tv_left);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        ivRight = (ImageView) findViewById(R.id.iv_right);

//        if (flag){//为True，右边文字显示
//            tvRight.setVisibility(VISIBLE);
//        }else {
//            ivRight.setVisibility(VISIBLE);
//        }


        //左边TextView
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.HorizontalItem);
        String leftText = typedArray.getString(R.styleable.HorizontalItem_tv_left);
        tvLeft.setText(leftText);


        //右边TextView
        String rightText = typedArray.getString(R.styleable.HorizontalItem_tv_right);
        tvRight.setText(rightText);

        //右边图片ImageView
        int rightImageView = typedArray.getResourceId(R.styleable.HorizontalItem_iv_right, -1);
        ivRight.setBackgroundResource(rightImageView);


        typedArray.recycle();
    }






    @Override
    public void onClick(View view) {

        Log.i("sbl","======");
    }




}
