//package com.zhuandian.qxe.utils;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Movie;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.zhuandian.qxe.R;
//
//public class MyGifView extends View {
//    private Movie mMovie;
//    private long mMovieStart;
//    public MyGifView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mMovie = Movie.decodeStream(getResources().openRawResource(
//                R.drawable.loding));
//    }
//    public MyGifView(Context context) {
//        super(context);
//        mMovie = Movie.decodeStream(getResources().openRawResource(
//                R.drawable.loding));
//    }
//    public void onDraw(Canvas canvas) {
//        long now = android.os.SystemClock.uptimeMillis();
//        if (mMovieStart == 0) { // first time
//            mMovieStart = now;
//        }
//        if (mMovie != null) {
//            int dur = mMovie.duration();
//            if (dur == 0) {
//                dur = 1000;
//            }
//            int relTime = (int) ((now - mMovieStart) % dur);
//            mMovie.setTime(relTime);
//            canvas.scale(0.7f,0.7f);
//            mMovie.draw(canvas, 0, 0);
//            invalidate();
//        }
//    }
//}