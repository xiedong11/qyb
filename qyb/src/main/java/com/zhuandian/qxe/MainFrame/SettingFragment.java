package com.zhuandian.qxe.MainFrame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.service.LoginActivity;
import com.zhuandian.qxe.utils.MyView.HorizontalItem;
import com.zhuandian.qxe.utils.MyView.TvRightOnclick;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 软件设置Fragment相关业务类
 * Created by 谢栋 on 2016/10/3.
 */
public class SettingFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private View view;
    private EditText username,phone,trade_local;
    private SharedPreferences sPreferences;

    //小龙测试使用
    private HorizontalItem horizontalItem;

    TvRightOnclick tvRightOnclick ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_1,null);
        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("软件设置");
        //初始化Bmob的SDK
        Bmob.initialize(getActivity(), getString(R.string.bmobkey));

        //得到注册界面用于保存用户信息的SharedPreference
         sPreferences= getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        initView();
        return view;

    }

    private void initView() {

        ((TextView)view.findViewById(R.id.logout)).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.phone)).setOnClickListener(this);
        ((LinearLayout)view.findViewById(R.id.feedback)).setOnClickListener(this);
        (( LinearLayout)view.findViewById(R.id.aboutus)).setOnClickListener(this);
        (( LinearLayout)view.findViewById(R.id.cancel_click)).setOnClickListener(this);
        (( LinearLayout)view.findViewById(R.id.version)).setOnClickListener(this);
        (( LinearLayout)view.findViewById(R.id.update_zixishi)).setOnClickListener(this);

        horizontalItem = (HorizontalItem) view.findViewById(R.id.horitem);

       horizontalItem.setOnClickListener(this);



//        view.findViewById(R.id.cancel_click).setOnClickListener(this);   //给设置的父控件设计点击事件，用户隐藏软键盘
        username = ((EditText) view.findViewById(R.id.user_name));
        phone = ((EditText) view.findViewById(R.id.phone));
        trade_local = ((EditText) view.findViewById(R.id.trade_local));

        //获取在注册界面得到的sharedpreferences对象存放的内容
        SharedPreferences sp = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        username.setText(sp.getString("real_name","曲小二"));
        phone.setText(sp.getString("phone","长按编辑"));
        trade_local.setText(sp.getString("trade_local","长按编辑"));
        trade_local.setOnLongClickListener(this);
        phone.setOnLongClickListener(this);
        username.setOnLongClickListener(this);


        //设置版本号
        ( (TextView)view.findViewById(R.id.versionname)).setText(MyUtils.getVersionName(getActivity()) + "");
    }


    //点击监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.horitem:
//                Log.i("sbl","在fragment中处理的");
//                break;

            case R.id.logout :
                BmobUser.logOut();   //清除缓存用户对象
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
//            BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                break;

            case R.id.feedback :
                changFragment(new FeedBackFragment());
                break;
            case R.id.aboutus :
                changFragment(new AboutUsFragrant());
                break;

            case R.id.phone :
               //编辑phone
                break;
            case R.id.cancel_click:
                 new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                         .setTitleText("想好了嘛！！")
                         .setContentText("确实要清除所有用户数据嘛！！")
                         .setCancelText("再也不会啦")
                         .setConfirmText("马上清除掉")
                         .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                             @Override
                             public void onClick(SweetAlertDialog sweetAlertDialog) {

                                 SharedPreferences.Editor editor = sPreferences.edit();
                                 editor.remove("real_name");
                                 editor.remove("phone");
                                 editor.remove("trade_local");
                                 editor.commit();
                                 sweetAlertDialog.cancel();

                                 new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                         .setTitleText("吼吼喔噢")
                                         .setContentText("已彻底干掉所有用户数据")
                                         .show();
                             }
                         })
                         .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                             @Override
                             public void onClick(SweetAlertDialog sweetAlertDialog) {
                                 sweetAlertDialog.cancel();

                                 new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                         .setTitleText("好险嘛！！")
                                         .setContentText("没有删除任何数据...")
                                         .show();
                             }
                         })
                         .show();


                break;

            case  R.id.version :
                new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("哦吼...")
                        .setContentText("已是最新版本啦 ！！")
                        .show();
                break;
            case  R.id.update_zixishi :
                new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("哦吼...")
                        .setContentText("自习室数据不用更新的啦！！")
                        .show();
                break;



        }
    }

    /**
     * 从当前界面跳转到目标fragment
     * @param targetFragment
     */
    private void changFragment(Fragment targetFragment) {
//        getFragmentManager()
//             .beginTransaction()
//            .add(R.id.main_fragment, targetFragment, "targetFragment")
//                .addToBackStack("targetFragment")
//                .commit();

       FragmentManager fm = getFragmentManager();
       FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container,targetFragment,"targetFragment");
        ft.addToBackStack("targetFragment");
        ft.commit();



    }

    //长按监听事件
    @Override
    public boolean onLongClick(View v) {



        switch (v.getId()){
            case R.id.user_name :

                setItemText(username,"real_name");
            break;

            case  R.id.phone :
                setItemText(phone ,"phone");
                break;

            case R.id.trade_local :
                setItemText(trade_local ,"trade_local");
                break;

        }

        return true;

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
                    SharedPreferences.Editor editor = sPreferences.edit();
                    editor.putString(keyValue, edittext.getText().toString());
                    editor.commit();

                    String real_name = sPreferences.getString(keyValue,"谢栋");
                    Log.d("xiedong",real_name);
                    return true;
                }
            }
            return false;
        }
    });
        edittext.setText(sPreferences.getString(keyValue, ""));
    }



}
