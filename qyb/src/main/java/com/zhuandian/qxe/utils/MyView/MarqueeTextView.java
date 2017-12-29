package com.zhuandian.qxe.utils.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author 谢栋
 * 实现textview跑马灯效果
 */
public class MarqueeTextView extends TextView {

    /** 是否停止滚动 */
    private boolean mStopMarquee;
    private String mText;//文本内容
    private float mCoordinateX = 1280;//当前滚动位置
    private float mTextWidth;//文本宽度
    private int mScrollWidth = 1280;//滚动区域宽度
    private int speed = 1;//滚动速度
    public float getCurrentPosition() {
        return mCoordinateX;
    }

    public void setCurrentPosition(float mCoordinateX) {
        this.mCoordinateX = mCoordinateX;
    }

    public int getScrollWidth() {
        return mScrollWidth;
    }

    public void setScrollWidth(int mScrollWidth) {
        this.mScrollWidth = mScrollWidth;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text) {
        this.mText = text;
        mTextWidth = getPaint().measureText(mText);
        //mTextWidth = 1280;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 10);
    }


    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        if (!isEmpty(mText))
            mHandler.sendEmptyMessageDelayed(0, 2000);
        super.onAttachedToWindow();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isEmpty(mText))
            canvas.drawText(mText, mCoordinateX, 150, getPaint());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:                    
                if (mCoordinateX < (-mTextWidth)) {//文字滚动完了，从滚动区域的右边出来
                    mCoordinateX = mScrollWidth;
                    invalidate();
                    if (!mStopMarquee) {
                    sendEmptyMessageDelayed(0, 500);
                    }
                } else {
                    mCoordinateX -= speed;
                    invalidate();
                    if (!mStopMarquee) {
                        sendEmptyMessageDelayed(0, 30);
                    }
                }

                break;
            }
            super.handleMessage(msg);
        }
    };

}