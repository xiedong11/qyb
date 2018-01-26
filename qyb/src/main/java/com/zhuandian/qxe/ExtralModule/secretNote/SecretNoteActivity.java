package com.zhuandian.qxe.ExtralModule.secretNote;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.entity.SercetNoteEntity;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/10/31.
 */
public class SecretNoteActivity extends Activity implements View.OnClickListener {

    private EditText passwordEditText;
    private DbUtils dbUtils;
    private TextInputLayout textinputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView)findViewById(R.id.toolbar_title)).setText("私密笔记");


        //初始化DbUtils框架
        dbUtils = DbUtils.create(this);

        //ActionBarActivity模式下设置的左上角的向上箭头
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);



        toolbar.setNavigationIcon(R.drawable.md_nav_icon);
        //设置左上角导航键的点击监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.secret_note_menu
        );   //设置溢出菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.delete :

                        //删除所有数据记录
                        try {
                            dbUtils.delete(SercetNoteEntity.class, WhereBuilder.b());
                            new SweetAlertDialog(SecretNoteActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("清除成功")
                                    .setContentText("已成功清除所有私密记录！")
                                    .show();

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.make :
                        new SweetAlertDialog(SecretNoteActivity.this,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("无权提交")
                                .setContentText("请升级为VIP用户获得更多权限")
                                .show();

                        break;
                }
                return false;
            }
        });



        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.missing_password).setOnClickListener(this);

        passwordEditText = (EditText) findViewById(R.id.ed_password);
        passwordEditText.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //监听android中的按键返回事件
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login :

                loginNote();

                break;
            case R.id.missing_password :
                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("暂不支持！")
                        .setContentText("暂不支持该功能，敬请期待！！！")
                        .show();
                break;
        }

    }

    /**
     * 登陆到编辑私密记事本的逻辑跳转
     */
    private void loginNote() {
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String password = sp.getString("password","");
        Log.i("xiedong",password);

        if("".equals(passwordEditText.getText().toString())){

            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("格式错误")
                    .setContentText("请输入登陆密码！！")
                    .show();
        }else if(!password.equals(passwordEditText.getText().toString())){
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("登陆失败")
                    .setContentText("请输入正确的登陆密码！！")
                    .show();

        }else{
//            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fram_note, new SecretNoteFragment(), "fragment")
                    .addToBackStack("fragment")
                    .commit();


        }
    }
}
