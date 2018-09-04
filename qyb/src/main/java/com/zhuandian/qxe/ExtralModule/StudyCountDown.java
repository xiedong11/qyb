package com.zhuandian.qxe.ExtralModule;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.utils.MyView.PickerView;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/10/21.
 */
public class StudyCountDown extends AppCompatActivity {

    private PickerView minute_pv;
    private PickerView second_pv;
    private TextView hour ,minute;
    private LinearLayout anim_loading,editContent;
    private EditText timeEditText;
    private long time;  //定义倒计时时长

    private boolean flag=false;   //倒计时完成的标志位
    private Vibrator vibrator;    //震动器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_countdown);

        ((TextView)findViewById(R.id.toolbar_title)).setText("学习监督");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initView();



    }

    private void initView() {

        hour = (TextView) findViewById(R.id.time_hour);
        minute =(TextView) findViewById(R.id.time_minute);
        anim_loading= (LinearLayout) findViewById(R.id.load_animation);
        editContent = (LinearLayout) findViewById(R.id.edit_content);
        timeEditText = (EditText) findViewById(R.id.ed_time);

        ((Button)findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("".equals(timeEditText.getText().toString())){

                    new SweetAlertDialog(StudyCountDown.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先设置自习时长！")
                            .setContentText("设定时长后，人家才能监督你学习嘛！！")
                            .show();

                }else if(Integer.parseInt(timeEditText.getText().toString())>120){
                    new SweetAlertDialog(StudyCountDown.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请设置120分钟之内！")
                            .setContentText("时间太长人家怕你会学习劳累嘛！！")
                            .show();
                } else{
                    time = Long.parseLong(timeEditText.getText().toString());
                    time = time * 1000 * 60;  //转换成微秒
                    Log.i("xiedong", time + "");
                    new MyCountDownTimer(time, 1000).start();

                    flag = true;
                    editContent.setVisibility(View.INVISIBLE);
                    anim_loading.setVisibility(View.VISIBLE);
                }

            }
        });
    }


//    CountDownTimer myCountTimer = new CountDownTimer(100000,1000) {
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            Log.i("xiedong",time+"哈哈哈，倒计时着呢");
//            hour.setText((millisUntilFinished/1000)/60+"");
//            minute.setText((millisUntilFinished/1000)%60+"");
//        }
//
//        @Override
//        public void onFinish() {
//
//        }
//    };

    class MyCountDownTimer extends CountDownTimer {

        //构造方法
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        //结束倒计时执行方法
        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            editContent.setVisibility(View.VISIBLE);
            anim_loading.setVisibility(View.INVISIBLE);

            flag=false;  //学习计划完成，标志位回到初始值

            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {100, 100, 100, 1000}; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, 2);
            //-1不重复，非-1为从pattern的指定下标开始重复

//            myVibrator.vibrate(new long[]{100, 200, 100, 200}, 0);  //短振动
//            myVibrator.vibrate(new long[]{100, 100, 100, 1000}, 0);  //长振动
//            myVibrator.vibrate(new long[]{500, 100, 500, 100, 500, 100}, 0); //节奏振动
//
            new SweetAlertDialog(StudyCountDown.this,SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("野生学霸养成中！")
                    .setContentText("休息一下吧，当前学习计划已完成！")
                    .show();

        }

       //倒计时开始执行方法
        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
//            Log.i("xiedong",time+"哈哈哈，倒计时着呢");
             hour.setText((millisUntilFinished/1000)/60+"");
             minute.setText((millisUntilFinished/1000)%60+"");
        }



}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(flag){
                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("请认真学习！！！")
                        .setContentText("学习计划还未完成，请放下手机！！")
                        .show();

            }else {
                if(vibrator!=null){
                    vibrator.cancel();
                }
                finish();

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

            //未完成学习计划的逻辑
            if(flag){
                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("请认真学习！！！")
                        .setContentText("学习计划还未完成，请放下手机！！")
                        .show();

            }else {
                finish();
                if(vibrator!=null){
                    vibrator.cancel();
                }
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }



}
