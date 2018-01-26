package com.zhuandian.qxe.ExtralModule.secretNote;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.adapter.SecretNoteAdapter;
import com.zhuandian.qxe.entity.SercetNoteEntity;
import com.zhuandian.qxe.utils.myUtils.MyBehavior;
import com.zhuandian.qxe.utils.myUtils.MyL;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/11/2.
 */
public class SecretNoteFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<SercetNoteEntity> mdatas;
    private SecretNoteAdapter secretNoteAdapter;

    private DbUtils dbUtils;
    private List<SercetNoteEntity> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_secret_note, null);

        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("日记记录");

        //使用xUtils的DButils框架
        dbUtils = DbUtils.create(getActivity());


        initData();

        initView();


//        try {
//            //删除所有
//            dbUtils.delete(SercetNoteEntity.class, WhereBuilder.b());
//            dbUtils.delete(SercetNoteEntity.class, WhereBuilder.b("id","=",1));   //删除对应的列表名，操作符合 ，具体值
//        } catch (DbException e) {
//            e.printStackTrace();
//        }

////        SercetNoteEntity noteBean = new SercetNoteEntity();
////        noteBean.setTitle("谢栋");
////        noteBean.setContent("内容");
//
//        try {
//
//            dbUtils.save(noteBean);
//
//        } catch (DbException e) {
//            e.printStackTrace();
//        }


        secretNoteAdapter = new SecretNoteAdapter(getActivity(), mdatas);

        recyclerView.setAdapter(secretNoteAdapter);
        //设置LinearLayoutManager的布局管理，用于设置recyclerview的滚动样式
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);  //设置成llistView样式
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 5);   //设置成5列的GridView

        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        //为适配器中的item设置点击监听事件
        secretNoteAdapter.setOnItemClickListener(new SecretNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Snackbar.make(v,"长按可选择删除",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View v, final int position) {
//                Toast.makeText(getActivity(),"Item"+position+"被长按键",Toast.LENGTH_SHORT).show();

                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定删除?")
                        .setContentText("确定要删除所选的条目吗!")
                        .setCancelText("确定删除")
                        .setConfirmText("我后悔了")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();

                                try {
                                    dbUtils.delete(SercetNoteEntity.class, WhereBuilder.b("id","like",list.get(position).getId()));
                                    MyL.e(position+"---"+list.get(position).getContent());

                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                secretNoteAdapter.deleteData(position);

                            }
                        })

                        .show();

            }
        });
        recyclerView.setLayoutManager(layoutManager);

        //给Item添加删除跟添加的动画效果
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;

    }



    /**
     * 初始化控件
     */
    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.new_note);
        CoordinatorLayout.LayoutParams layoutParams_2 = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
        MyBehavior myBehavior = new MyBehavior();
        layoutParams_2.setBehavior(myBehavior);

        view.findViewById(R.id.new_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fram_note, new NewSecretNoteFragment(), "fragment")
                        .addToBackStack("fragment")
                        .commit();

            }
        });

    }

    /**
     * 装载数据
     */
    private void initData() {
        mdatas = new ArrayList<SercetNoteEntity>();


        try {
           list = dbUtils.findAll(SercetNoteEntity.class);//通过类型查找

            //如果返回的list不为空，为mdatas装载数据
            if (list != null) {
                mdatas = list;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("私密笔记");
    }
}
