package com.zhuandian.qxe.service;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.entity.UserEntity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by 谢栋 on 2016/9/7.
 */
public class RegisterFragment extends Fragment {
    private View view;
    private EditText nikename,password,repassword,email,real_name,phone;
    private Button register;
    private UserEntity userEntity;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.register_2,null);
        //初始化Bmob的SDK
        Bmob.initialize(getActivity(), "df25a6c6a79479d11a60f2e89c68b467");
        initView();
        return  view;
    }

    private void initView() {
        nikename = (EditText) view.findViewById(R.id.nikename);
        password = (EditText) view.findViewById(R.id.password);
//        repassword = (EditText) view.findViewById(R.id.repassword);
        email    = (EditText) view.findViewById(R.id.email);
        real_name = (EditText) view.findViewById(R.id.real_name);
        phone = (EditText) view.findViewById(R.id.phone);
        register = (Button) view.findViewById(R.id.register);
        userEntity = new UserEntity();

       register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               if ("".equals(real_name.getText().toString()) || "".equals(phone.getText().toString())) {

                   Log.i("xiedong","进来啦");
                   new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                           .setTitleText("oh,no.no.no...")
                           .setContentText("请务必真实填写姓名跟联系方式")
                           .show();

               } else {

                   Log.i("xiedong",real_name.hashCode()+"real_name");
                   Log.i("xiedong",real_name.getText().toString());
                   Log.i("xiedong","进到不该进来的地方了啊");
                   //把用户名称跟电话号码存入sharedpreference中
                   SharedPreferences sp = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = sp.edit();

                   editor.putString("real_name",real_name.getText().toString());   //把用户姓名存入SharedPreferences
                   editor.putString("phone",phone.getText().toString());
                   editor.commit();

                   dialog = new SpotsDialog(getActivity(), "\n正在注册...");

                   dialog.show();
                   userEntity.setUsername(nikename.getText().toString());
                   userEntity.setPassword(password.getText().toString());
                   userEntity.setEmail(email.getText().toString());

                   Log.i("xiedong", email.getText().toString());
                   userEntity.signUp(new SaveListener<UserEntity>() {
                       @Override
                       public void done(UserEntity userEntity, BmobException e) {

                           if (e == null) {
                               dialog.dismiss();
                               new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                       .setTitleText("好的嘛 !")
                                       .setContentText("注册成功啦，请点击返回登陆界面")
                                       .show();
//                           Toast.makeText(getActivity(),"注册成功"+userEntity.toString(),Toast.LENGTH_SHORT).show();
                           } else {
                               dialog.dismiss();
                               new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                       .setTitleText("注册失败")
                                       .setContentText("请更换注册名或检查邮箱格式是否正确")
                                       .show();
//                           Toast.makeText(getActivity(),"失败"+e.toString(),Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
               }
           }
       });

    }


}
