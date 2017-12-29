package com.zhuandian.qxe.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuandian.qxe.MenuActivity;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.Myuser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by 谢栋 on 2016/9/7.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView register;
    private EditText userword,password;
    private Button login;
    private  Myuser myuser;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        //设置为全屏显示
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //初始化Bmob的SDK
        Bmob.initialize(this, "df25a6c6a79479d11a60f2e89c68b467");

       myuser =new Myuser();

        initView();

    }



    private void initView() {
        register = (TextView) findViewById(R.id.register);
        userword = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login    = (Button) findViewById(R.id.login);

        SharedPreferences setUserInfo = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String username = setUserInfo.getString("username","");
        String secret = setUserInfo.getString("password","");

        //设置输入框的内容
        userword.setText(username);
        password.setText(secret);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        RegisterFragment myFragment = new RegisterFragment();
        switch (v.getId()){
            case  R.id.register :
                android.app.FragmentManager  fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.uselocal,myFragment,"myFragment");
                ft.addToBackStack("myFragment");    //添加Fragment到回退栈，点击返回后返回上一层fragment
                ft.commit();

                break;

            case R.id.login :
                login();
                break;
                }


        }


    /**
     * 用户登陆函数
     */
    private void login() {
        final String username;
        final String screat;
        dialog = new SpotsDialog(this,"\n登陆中...");

        dialog.show();

         username = userword.getText().toString();
         screat   = password.getText().toString();

        myuser.setUsername(username);
        myuser.setPassword(screat);

        myuser.login(new SaveListener<Myuser>() {

            @Override
            public void done(Myuser myuser, BmobException e) {

                if (e == null) {

                    SharedPreferences sp =getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                    //生成一个保存编辑对象
                    SharedPreferences.Editor editor = sp.edit();
                    //添加要保存的对象
                    editor.putString("username", username);
                    editor.putString("password",screat);

                    //提交保存
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
//                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Log.i("xie", e.toString());
                    dialog.dismiss();
                    new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("登陆失败")
                            .setContentText("请先注册或检查账号密码是否正确输入")
                            .show();
                }

            }
        });

    }



}


