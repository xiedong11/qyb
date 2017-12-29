package com.zhuandian.qxe.ExtralModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.qxe.R;

/**
 * 考研计划，研途有你日程表安排类
 * Created by 谢栋 on 2016/10/20.
 */
public class Postgraduate_Plan extends ActionBarActivity implements View.OnLongClickListener {
    private EditText class_12;
    private EditText class_34;
    private EditText class_57;
    private EditText class_911;
    private SharedPreferences sp;
    private EditText morning_time;
    private EditText afternoon_time;
    private EditText jishibenTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postgraduate_plan);


        initView();
    }

    private void initView() {



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView)findViewById(R.id.toolbar_title)).setText("研途日程");

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this,"长按可编辑作息时间和课程",Toast.LENGTH_LONG).show();

        class_12 = (EditText) findViewById(R.id.class_12);
        class_34 = (EditText) findViewById(R.id.class_34);
        class_57 = (EditText) findViewById(R.id.class_57);
        class_911 = (EditText) findViewById(R.id.class_911);
        morning_time = (EditText) findViewById(R.id.morning_time);
        afternoon_time = (EditText) findViewById(R.id.afternoon_time);
        jishibenTV = (EditText) findViewById(R.id.jishibenTV);

        sp = getSharedPreferences("class_plan", Context.MODE_PRIVATE);
        class_12.setText(sp.getString("class_12","高数"));
        class_34.setText(sp.getString("class_34","政治"));
        class_57.setText(sp.getString("class_57","英语"));
        class_911.setText(sp.getString("class_911","专业课"));
        morning_time.setText(sp.getString("morning_time","7:00"));
        afternoon_time.setText(sp.getString("afternoon_time","13:00"));
        jishibenTV.setText(sp.getString("jishibenTV","座右铭激励自己吧！(长按编辑,返回键保存)"));



        class_12.setOnLongClickListener(this);
        class_34.setOnLongClickListener(this);
        class_57.setOnLongClickListener(this);
        class_911.setOnLongClickListener(this);
        morning_time.setOnLongClickListener(this);
        afternoon_time.setOnLongClickListener(this);
        jishibenTV.setOnLongClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //监听系统返回按键
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 设置edittext的点击监听事件，用户长按可修改edittext中的内容，
     * 点击返回按键，存入sharedpreference中
     *
     * @param edittext  要修改的edittext
     * @param keyValue  存入sharedpreference中的键值
     */
    private void setItemText(final EditText edittext, final String keyValue) {
        edittext.setFocusableInTouchMode(true);
        edittext.setFocusable(true);
        Log.i("xieodng", "被长按");
        edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (edittext.isFocused()) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        edittext.setFocusableInTouchMode(false);
                        edittext.clearFocus();
                        edittext.setTextColor(getResources().getColor(R.color.accent_material_dark));
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(keyValue, edittext.getText().toString());
                        editor.commit();

                        return true;
                    }
                }
                return false;
            }
        });
        edittext.setText(sp.getString(keyValue, ""));
    }

    /**
     * EditText的长按事件监听
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {


        switch (v.getId()){
            case R.id.class_12 :

                setItemText(class_12,"class_12");
                break;

            case  R.id.class_34 :
                setItemText(class_34,"class_34");
                break;

            case R.id.class_57 :
                setItemText(class_57,"class_57");
                break;
            case R.id.class_911 :
                setItemText(class_911,"class_911");
                break;
            case R.id.morning_time :
                setItemText(morning_time,"morning_time");
                break;

            case R.id.afternoon_time :
                setItemText(afternoon_time,"afternoon_time");
                break;
            case R.id.jishibenTV :
                setItemText(jishibenTV,"jishibenTV");
                break;
        }
        return true;
    }
}
