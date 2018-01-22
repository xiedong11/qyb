package com.zhuandian.qxe.ExtralModule.secretNote;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.entity.SercetNoteEntity;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/11/3.
 */
public class New_Secret_Note extends Fragment {
    private View view;
    private EditText content;
    private Button commit;

    private DbUtils db;   //声明DbUtils框架

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view  = inflater.inflate(R.layout.new_srcret_note,null);
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("新建笔记");

        //初始化DbUtils框架
        db = DbUtils.create(getActivity());

        initView();

        return view;
    }

    private void initView() {

//        time = (EditText) view .findViewById(R.id.time);
        content = (EditText) view .findViewById(R.id.content);
        commit = (Button) view.findViewById(R.id.commit);



        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("".equals(content.getText().toString())){
//                   //空字符的处理
//                    Log.i("xiedong","空字符串");
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("字符为空")
                            .setContentText("记事内容都不允许为空！！")
                            .show();
                }else{


                    //插入数据
                    SercetNoteEntity noteBean = new SercetNoteEntity();

                    String time[]=MyUtils.currentTime().split(" ");
                    String time1[] =time[0].split("-");
                    String time2[] =time[1].split(":");

                    String timeStr = time1[1]+"月"+time1[2]+"日"+" "+time2[0]+":"+time2[1];

                    noteBean.settime(timeStr);
                    noteBean.setContent(content.getText().toString());
                    Log.i("xiedong",noteBean.gettime()+noteBean.getContent());

                    try {
                        db.save(noteBean);
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("插入成功")
                                .setContentText("放心，小园会为您妥善保管的！！")
                                .show();
                    } catch (DbException e) {
                        e.printStackTrace();
                        Log.i("xiedong",e.toString());
                    }

                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("日记记录");
    }
}
