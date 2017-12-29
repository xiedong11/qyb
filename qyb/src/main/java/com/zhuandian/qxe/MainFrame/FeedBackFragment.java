package com.zhuandian.qxe.MainFrame;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.FeedBack;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/9/2.
 */
public class FeedBackFragment extends Fragment{

    private EditText phoneEditText,feedbackEditText;
    private Button mButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.feedback,null);

        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("反馈建议   ");
        //初始化Bmob的SDK
        Bmob.initialize(getActivity(), "df25a6c6a79479d11a60f2e89c68b467");
        initView();

        return view;

    }

    private void initView() {

        phoneEditText    = (EditText) view.findViewById(R.id.phone);
        feedbackEditText = (EditText) view.findViewById(R.id.feedback);
        mButton          = (Button) view.findViewById(R.id.commit);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("正则提交反馈");
                pDialog.setCancelable(false);
                pDialog.show();


                FeedBack feedBack = new FeedBack();

                String phone = phoneEditText.getText().toString();
                String feedbackString = feedbackEditText.getText().toString();

                if("".equals(phone)||"".equals(feedbackString)){
                    pDialog.cancel();
                    //请完善反馈信息
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请完善反馈信息")
                            .show();
                }else{

                    feedBack.setPhone(phone);
                    feedBack.setFeedback(feedbackString);
                    feedBack.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {

                            if(e==null){
                                //反馈成功
                                pDialog.dismiss();
                                pDialog.cancel();

                                new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("反馈成功 !")
                                        .show();

                            }else{
                                //反馈失败
                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("反馈失败，请重新反馈")
                                        .show();
                            }
                        }
                    });
                }
            }
        });
    }



}
