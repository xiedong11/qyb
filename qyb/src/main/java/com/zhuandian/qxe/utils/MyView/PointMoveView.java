package com.zhuandian.qxe.utils.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.utils.myUtils.MyL;

/**
 * Created by 谢栋 on 2017/1/3.
 */
public class PointMoveView  extends View{

    public float currentX =120;  //定义当前坐标的位置
    public float currentY =120;

    public float currentX_right =80;  //定义当前坐标的位置
    public float currentY_right =140;

    public float currentX_left =200;  //定义当前坐标的位置
    public float currentY_left =140;

    public int currentSize = 10;    //定义圆点的初始大小

    Paint paint = new Paint();  //实例化当前画笔
    Paint paintRight = new Paint();  //右边边画笔
    Paint paintLeft = new Paint();  //左边画笔

    public PointMoveView(Context context) {
        super(context);
    }

    public PointMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    /**
     * 重写onDraw()方法，设置画笔颜色，绘制小圆点
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        MyL.e(currentSize+"---------");
        //当前圆点
        paint.setColor(getResources().getColor(R.color.appBar));   //定义画笔颜色
        canvas.drawCircle(currentX,currentY,20,paint);   //传入xy值，圆的大小，画笔对象，画出一个小圆点

        //右边圆点
        paintRight.setColor(Color.BLUE);   //定义右边画笔颜色
        canvas.drawCircle(currentX_right,currentY_right,currentSize,paintRight);   //传入xy值，圆的大小，画笔对象，画出一个小圆点

        //左边圆点
        paintLeft.setColor(Color.RED);   //定义左边画笔颜色
        canvas.drawCircle(currentX_left,currentY_left,currentSize,paintLeft);   //传入xy值，圆的大小，画笔对象，画出一个小圆点
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        if(currentX>event.getX()||currentY>event.getY()){  //向下或者向右值变大
            currentSize+=5;
        }else{
            //向上或者向左值变大
            currentSize-=5;
        }

        //得到当前组件的X,Y两个属性值
        this.currentX = event.getX();
        this.currentY = event.getY();

        //得到右边组件的X,Y两个属性值
        this.currentX_right = event.getX()+40;
        this.currentY_right = event.getY()+40;

        //得到左边组件的X,Y两个属性值
        this.currentX_left = event.getX()-40;
        this.currentY_left = event.getY()+40;


        this.invalidate();  //通知组件重新绘制

        return true;  //结束事件的传递
    }
}
