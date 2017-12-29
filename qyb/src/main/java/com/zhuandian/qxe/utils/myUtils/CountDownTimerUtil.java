package com.zhuandian.qxe.utils.myUtils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;


/* The calls to {@link #onTick(long)} are synchronized to this object so that
        * one call to {@link #onTick(long)} won't ever occur before the previous
        * callback is complete.  This is only relevant when the implementation of
        * {@link #onTick(long)} takes an amount of time to execute that is significant
        * compared to the countdown interval.
        */

public abstract class CountDownTimerUtil {

    /**
     * Millis since epoch when alarm should stop.
     */
    private long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private long mCountdownInterval;

    private long mStopTimeInFuture;

    private boolean mCancelled = false;

    /**
     * @param millisInFuture The number of millis in the future from the call
     *   to {@link #start()} until the countdown is done and {@link #onFinish()}
     *   is called.
     * @param countDownInterval The interval along the way to receive
     *   {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtil(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }


    public final void setCountdownInterval(long countdownInterval) {
        mCountdownInterval = countdownInterval;
    }

    public final void setMillisInFuture(long millisInFuture) {
        mMillisInFuture = millisInFuture;       
    }

    /**
     * Cancel the countdown.
     *
     * Do not call it from inside CountDownTimer threads
     */
    public final void cancel() {
        mHandler.removeMessages(MSG);
        mCancelled = true;
    }

    /**
     * Start the countdown.
     */
    public synchronized final CountDownTimerUtil start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mCancelled = false;
        return this;
    }

    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();

    private static final int MSG = 1;

    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownTimerUtil.this) {
                final long millisLeft = mStopTimeInFuture - 

SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;

                    if (!mCancelled) {
                        sendMessageDelayed(obtainMessage(MSG), delay);
                    }
                }
            }
        }
    };
}