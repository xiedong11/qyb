package com.zhuandian.qxe.utils.MyView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义view利用缓冲区实现写字板
 * Created by 谢栋 on 2017/1/6.
 */
public class WriteTableView extends View{

    float previousX;    //前一个事件发生的坐标位置
    float previousY;

    private Path path;
    public Paint paint=null;

    Bitmap cacheBitmap =null;  //位于内存中的一个bitmap，作为缓冲区
    Canvas cacheCanvas = null;  //内存缓冲区上的canvas

    Canvas clearCanvas=null;   //定义清除内容用的canvas

    public WriteTableView(Context context , int width , int hight) {
        super(context);

         // 创建一个与传入view宽高相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(width,hight, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();

        //把当前的bitmap对象绘制到内存中的canvas上
        cacheCanvas.setBitmap(cacheBitmap);

        //设置画笔的颜色
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.BLACK);

        //设置画笔风格
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        //反锯齿
        paint.setAntiAlias(true);
        paint.setDither(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //得到当前拖动时间发生的坐标值
        float currentX= event.getX();
        float currentY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :

                //从前一个点绘制到当前点之后，把当前点定义成下一次绘制的前一个点
                path.moveTo(currentX,currentY);
                previousX = currentX;
                previousY= currentY;
                break;

            case MotionEvent.ACTION_MOVE :

                //从前一个点绘制到当前点之后，把当前点定义成下一次绘制的前一个点
                path.quadTo(previousX,previousY,currentX,currentY);
                previousX = currentX;
                previousY = currentY;
                break;

            case MotionEvent.ACTION_UP :
                cacheCanvas.drawPath(path,paint);
                path.reset();
                break;
        }

        this.invalidate();  //通知重新绘制组件

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.clearCanvas= canvas;

        Paint myPaint = new Paint();

        //将缓冲区的bitmap对象绘制到view组件上
        clearCanvas.drawBitmap(cacheBitmap,0,0,myPaint);
        clearCanvas.drawPath(path,paint);    //沿着path路径绘制bitmap
    }

    /**
     * 清除掉当前view上所绘制的内容
     */
    public void clearCurrentBitmap(){

        Canvas canvas = new Canvas();
        canvas.setBitmap(cacheBitmap);
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
    }
}
