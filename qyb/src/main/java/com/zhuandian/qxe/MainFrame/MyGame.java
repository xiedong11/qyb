package com.zhuandian.qxe.MainFrame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.zhuandian.qxe.utils.MyView.PointMoveView;

/**
 * Created by 谢栋 on 2017/1/3.
 */
public class MyGame extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化控件
        LinearLayout linearLayout = new LinearLayout(this);
        PointMoveView pointMoveView = new PointMoveView(this);


        linearLayout.addView(pointMoveView);

        setContentView(linearLayout);



    }
}
