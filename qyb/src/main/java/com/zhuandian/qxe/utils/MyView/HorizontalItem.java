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
    TvRightOnclick tvRightOnclick;//接口

//    public HorizontalItem(Context context) {
//        super(context);
//    }

    public HorizontalItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将自定义组合控件的布局渲染成View
        View view = View.inflate(context, R.layout.horizontal_item, this);
    //  LayoutInflater.from(context).inflate(R.layout.horizontal_item,this,true);
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
        tvLeft.setOnClickListener(this);


        //右边TextView
        String rightText = typedArray.getString(R.styleable.HorizontalItem_tv_right);
        tvRight.setText(rightText);
        tvRight.setOnClickListener(this);

        //右边图片ImageView
        int rightImageView = typedArray.getResourceId(R.styleable.HorizontalItem_iv_right, R.drawable.add);
        ivRight.setBackgroundResource(rightImageView);
        ivRight.setOnClickListener(this);

        typedArray.recycle();
    }






    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_left:
                Log.i("sbl","嘻嘻嘻嘻嘻嘻");
                break;
            case R.id.tv_right://以这个为例子
                Log.i("sbl","哈哈啊哈哈哈哈哈哈");
                new TvRightOnclick() {
                    @Override
                    public void setTvRightOnLongclickListener(String text) {
                        tvRight.setText(text);
                    }
                };
             //   tvRightOnclick.setTvRightOnLongclickListener();
                break;
            case R.id.iv_right:
                break;
        }

    }




}
