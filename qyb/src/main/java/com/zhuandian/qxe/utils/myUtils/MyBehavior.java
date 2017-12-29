package com.zhuandian.qxe.utils.myUtils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 谢栋 on 2016/12/20.
 */
public class MyBehavior extends CoordinatorLayout.Behavior{
    //在xml文件中使用Behavior必须完成两个参数的构造方法，之后在xml文件中要使用Behavior的控件中加上app:layout_behavior="包名.MyBehavior"即可
    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //空参构造方法
    public MyBehavior() {
    }

    /**
     *
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @return  是否关心
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;  //需要关心整个滚动事件,直接返回true
    }

    /**
     * 处理滚动事件
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        //垂直方向的滚动，只需关心dy值即可
        if (dy<0) {
            //dy<0为向下滚动,让控件出现
            ViewCompat.animate(child).scaleX(1).scaleY(1).start();
        }else{
            //dy>0为向上滚动，让控件消失
            ViewCompat.animate(child).scaleX(0).scaleY(0).start();
        }
    }
}
