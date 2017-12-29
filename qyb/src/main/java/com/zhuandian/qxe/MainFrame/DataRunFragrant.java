package com.zhuandian.qxe.MainFrame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.DataRunBean;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/8/10.
 */
public class DataRunFragrant extends Fragment implements View.OnClickListener {
    private View mView;
    private TextView xccTextView;
    private TextView xccSumTextView;
    private TextView xccHzTextView;
    private TextView xccMmzTextView;
    private int xccSum;

    private TextView timeHour;
    private TextView timeSecond;

    private EditText timeEditText;
    private LinearLayout timeContentLinearLayout;
    private Button start;
    private TextView runTextView;
    private long time;  //定义时长

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.run,null);
        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("相约跑步");

       initView();

        //初始化Bmob的SDK
        Bmob.initialize(getActivity(),"df25a6c6a79479d11a60f2e89c68b467");
       xccSum = getNowSum_2();


        timeHour = (TextView) mView.findViewById(R.id.time_hour);
        timeSecond = (TextView) mView.findViewById(R.id.time_second);

     initView2();
        return mView;


    }

    private void initView2() {
        timeContentLinearLayout = (LinearLayout) mView.findViewById(R.id.ed_time_content);
        timeEditText = (EditText) mView.findViewById(R.id.time);
        start = (Button) mView.findViewById(R.id.start);
        runTextView = (TextView) mView.findViewById(R.id.run);

        runTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeContentLinearLayout.setVisibility(View.VISIBLE);    //设置时间输入框，跟开始按钮可见
                runTextView.setVisibility(View.INVISIBLE);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("".equals(timeEditText.getText().toString())){   //防止用户不输入字符空指针退出
                   new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                           .setTitleText("请输入时长")
                           .show();
                }else{
                    time = Long.parseLong(timeEditText.getText().toString());
                    time = time*1000*60;  //转换成微秒

                    new MyCountDownTimer(time,1000).start();
                    timeContentLinearLayout.setVisibility(View.INVISIBLE);
                }

            }
        });

    }


    /**
     * CountDownTimer timer = new CountDownTimer(10000, 1000)中，
     * 第一个参数表示总时间，第二个参数表示间隔时间。意思就是每隔一秒会回调一次方法onTick，
     * 然后10秒之后会回调onFinish方法。
     */

    CountDownTimer countDownTimer = new CountDownTimer(100000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {   //millisUntilFinished   倒计时剩余时间。
            timeHour.setText(""+(millisUntilFinished/1000)/60);  //计算分钟
            timeSecond.setText(""+(millisUntilFinished/1000)%60);  //计算秒
        }


        @Override
        public void onFinish() {
//            timeTextView.setText("计时结束");
        }
    };

    class MyCountDownTimer extends CountDownTimer{

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeHour.setText(""+(millisUntilFinished/1000)/60);  //计算分钟
            timeSecond.setText(""+(millisUntilFinished/1000)%60);  //计算秒
        }

        @Override
        public void onFinish() {


            runTextView.setVisibility(View.VISIBLE);    //把跑步开始按钮设为可见状态
            new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("哇卡！完成啦！")
                    .setContentText("已经完成了设定的跑步计划！")
                    .show();

        }
    }
    /**
     * 初始化控件
     */
    private void initView() {
        xccSumTextView = (TextView) mView.findViewById(R.id.xccsum);
        xccHzTextView  = (TextView) mView.findViewById(R.id.xcchz);
        xccMmzTextView = (TextView) mView.findViewById(R.id.xccmmz);
        xccTextView = (TextView) mView.findViewById(R.id.xcc);
        xccTextView.setOnClickListener(this);

        xccHzTextView.setOnClickListener(this);
        xccMmzTextView.setOnClickListener(this);
//        mView.findViewById(R.id.datarun).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xcc :

//                getowSum_2();
                addToRun();
                break;

            case R.id.datarun :
                Toast.makeText(getActivity(),"请先完善个人资料",Toast.LENGTH_SHORT).show();
                break;

            case R.id.xccmmz :
                 addGirl();
                 break;

            case R.id.xcchz :
                addBoy();
                break;
        }
    }

    /**
     * 添加跑步的男同学人数
     */
    private void addBoy() {

    }

    /**
     * 添加约跑步的妹子人数
     */
    private void addGirl() {

    }

    /**
     * 添加约跑人数到数据库
     */
    private void addToRun() {

        DataRunBean dataRunBeen  = new DataRunBean();

         int nowSum=xccSum+1;

         //点击一次相当于有一个人约跑，数据加1

        Log.i("bmob",nowSum+"--1");
        nowSum = getNowSum_2();
        Log.i("bmob",nowSum+"--2");
        dataRunBeen.setXccSum(nowSum);

        dataRunBeen.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(getActivity(),"您成功约到了西操场，go...",Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("bmob",e.toString());
                    Toast.makeText(getActivity(),"失败",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private int getNowSum_2(){

      int sum=0;
        final DataRunBean dataRunBeen  = new DataRunBean();

        BmobQuery<DataRunBean> query = new BmobQuery<DataRunBean>();
        List<BmobQuery<DataRunBean>> and = new ArrayList<BmobQuery<DataRunBean>>();
        //大于00：00：00
        BmobQuery<DataRunBean> q1 = new BmobQuery<DataRunBean>();
//        String start = "2015-05-01 00:00:00";
        String start =MyUtils.decreaseTime(60) ;

        Log.i("xiedong","startTime"+start);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
        and.add(q1);
        //小于23：59：59
        BmobQuery<DataRunBean> q2 = new BmobQuery<DataRunBean>();
        String end = MyUtils.currentTime();
        Log.i("xiedong","endTime "+end);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1  = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date1));
        and.add(q2);
       //添加复合与查询
        query.and(and);


        query.findObjects(new FindListener<DataRunBean>() {
            @Override
            public void done(List<DataRunBean> dataRunBeens, BmobException e) {

                if(e==null){

                    dataRunBeen.setXccSum(dataRunBeens.size());
                    xccSumTextView.setText("总人数"+dataRunBeens.size()+"");
                }

            }
        });
       return dataRunBeen.getXccSum();
    }
    /**
     * 实时从网络更新约跑人数
     */
    private int getNowSum() {

        final DataRunBean dataRunBeen  = new DataRunBean();
        BmobQuery<DataRunBean> query = new BmobQuery<DataRunBean>();
////查询playerName叫“比目”的数据
//        query.addWhereEqualTo("playerName", "比目");
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<DataRunBean>() {
            @Override
            public void done(List<DataRunBean> object, BmobException e) {
                if(e==null){

                    dataRunBeen.setXccSum(object.size());
                    xccSumTextView.setText("总人数"+object.size()+"");
                    Log.i("bmob", object.size()+"--3");

                }else{
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        return dataRunBeen.getXccSum();
    }
}
