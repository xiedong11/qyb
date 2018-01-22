package com.zhuandian.qxe.ExtralModule.heartshare;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.entity.HeartShareEntity;
import com.zhuandian.qxe.entity.UserEntity;
import com.zhuandian.qxe.utils.myUtils.MyL;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 新建心语互享
 * Created by 谢栋 on 2016/10/26.
 */
public class NewHeartShareFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView heartType;
    private EditText content;
    private LinearLayout commitContent;
    private String heartShartType="谈天说地";    //获取从单选对话框得到的动态类型,默认为"谈天说地"
    private SweetAlertDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.new_heart_share, null);

        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("发布心情");

        //给控件绑定监听事件
        heartType = ((TextView) view.findViewById(R.id.chose_heart_type));
        content = (EditText) view.findViewById(R.id.heart_content);
        commitContent = ((LinearLayout) view.findViewById(R.id.commit_content));
        commitContent.setOnClickListener(this);
        heartType.setOnClickListener(this);

        //定义对话框
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("提交中...");
        pDialog.setCancelable(false);


        return view;


    }

    /**
     * 当Fragment结束时，重新设置上层activity上toolbar的标题
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("心语互享");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chose_heart_type:
                choseHeartType();
                break;
            case R.id.commit_content:
                commitHeartContent();
                break;
        }

    }


    /**
     * 提交用户发布的动态到后台数据库
     */
    private void commitHeartContent() {

        String heartContent = content.getText().toString();

        if (!"".equals(heartContent)) {
            pDialog.show();  //开始提交
            UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
            // 创建动态信息
            HeartShareEntity post = new HeartShareEntity();
            post.setContent(content.getText().toString());
            //添加一对一关联
            post.setAuthor(user);   //设置作者

            MyL.e("user----"+user.getUsername());
            post.setUsername(user.getUsername());
            post.setContentType(heartShartType);   //设置类型
            post.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    pDialog.dismiss();  //提交完成
                    if (e == null) {
                        MyL.e("提交成功");
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("提交成功")
                                .show();
                    } else {
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("提交失败")
                                .show();
                        MyL.e("提交失败-----" + e.getMessage());
                    }
                }
            });
        } else {
            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("动态内容不能为空哦！")
                    .show();
        }
    }

    /**
     * 提供给用户选择动态类型的单选列表对话框
     */
    private void choseHeartType() {
        final String typeArray[] = new String[]{"鸡血心声", "我要吐槽", "无聊减压", "寂寞排压", "谈天说地"};

          MyL.e("dialog");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("动态类型")
                .setSingleChoiceItems(typeArray,  //装载数组信息
                        //默认选中第二项
                        1,
                        //为列表添加监听事件
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        heartType.setText(typeArray[0]);
                                        break;
                                    case 1:
                                        heartType.setText(typeArray[1]);
                                        break;
                                    case 2:
                                        heartType.setText(typeArray[2]);
                                        break;
                                    case 3:
                                        heartType.setText(typeArray[3]);
                                        break;
                                    case 4:
                                        heartType.setText(typeArray[4]);
                                        break;

                                }

                                heartShartType=heartType.getText().toString();
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("取消",null)  //确定事件的监听事件设为空
                .create()
                .show();

    }


}
