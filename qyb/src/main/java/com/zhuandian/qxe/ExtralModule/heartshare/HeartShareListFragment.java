package com.zhuandian.qxe.ExtralModule.heartshare;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.adapter.HeartShareAdapter;
import com.zhuandian.qxe.entity.HeartShareEntity;
import com.zhuandian.qxe.utils.GlobalVariable;
import com.zhuandian.qxe.utils.myUtils.MyL;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
/**
 * 用于装载所有用户分享的动态
 * Created by 谢栋 on 2016/10/26.
 */
public class HeartShareListFragment extends Fragment implements AbsListView.OnScrollListener {
    private ListView listView;
    private HeartShareAdapter myAdapter;

    private SwipeRefreshLayout swipeRefresh;
    List<HeartShareEntity> mDatas = new ArrayList<>();    //数据集合

    private View view;
    private FloatingActionButton fab;
    private LinearLayout loadLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_heart_share_list,null);

        //初始化Bmob的SDK
        Bmob.initialize(getActivity(),"df25a6c6a79479d11a60f2e89c68b467");
        init();

        loadDatas();
//        view.setClickable(true);
        return view;
    }

    /**
     *装载数据
     */
    private void loadDatas() {

//        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        BmobQuery<HeartShareEntity> query = new BmobQuery<HeartShareEntity>();
//        query.addWhereEqualTo("author", user);    // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<HeartShareEntity>() {

            @Override
            public void done(List<HeartShareEntity> object, BmobException e) {
                if(e==null){


                    loadLinearLayout.setVisibility(View.INVISIBLE);  //取消用户等待视图
                    mDatas=object;   //查询到的数据赋值给mDatas
                    myAdapter = new HeartShareAdapter(getActivity(),object);
                    listView.setAdapter(myAdapter);
                    swipeRefresh.setRefreshing(false);

                    MyL.e("成功查询——————————"+object.size()+object.get(0).getUsername());
                }else{
                    MyL.e("失败："+e.getMessage());
                }
            }


        });

    }



    /**
     * 初始化主布局上的各控件及配置
     */
    private void init() {

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listView = (ListView) view.findViewById(R.id.heart_list);
        loadLinearLayout = (LinearLayout) view.findViewById(R.id.loading);  //用户等待视图
        swipeRefresh = (SwipeRefreshLayout)view. findViewById(R.id.heart_list_swipeRefresh);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(new MyItemClickListener());
        //设置下拉刷新的动画的颜色
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //设置下拉刷新监听
        swipeRefresh.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                pullState=true; //置为下拉
                loadDatas();
                Snackbar snackbar=Snackbar.make(fab,"无最新数据可加载...",Snackbar.LENGTH_SHORT);
                MyUtils.setSnackbarMessageTextColor(snackbar, Color.parseColor("#ffffff"));
                snackbar.show();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {    //点击跳转到发动态Fragment
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.heart_content,new NewHeartShareFragment(),"newsharefragment")
                        .addToBackStack("newsharefragment")
                        .commit();
            }
        });

//
    }

    private int currentIndex=0;  //记录当前ListView的索引

    /**
     * 监听ListView的滚动状态
     * @param view
     * @param scrollState
     * 开始滚动（SCROLL_STATE_FLING）
     * 正在滚动(SCROLL_STATE_TOUCH_SCROLL)
     * 已经停止（SCROLL_STATE_IDLE）
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {


        switch (scrollState) {

            // 滚动之前,手还在屏幕上  记录滚动前的下标
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                //view.getLastVisiblePosition()得到当前屏幕可见的第一个item在整个listview中的下标
                currentIndex = view.getLastVisiblePosition();
                break;

            //滚动停止
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                //记录滚动停止后 记录当前item的位置
                int scrolled = view.getLastVisiblePosition();
                MyL.e(scrolled+"---"+currentIndex);
                //滚动后下标大于滚动前 向下滚动了
                if (scrolled > currentIndex) {
                    //scroll = false;
                    fab.setVisibility(View.INVISIBLE);
                }
                //向上滚动了
                else {
                    fab.setVisibility(View.VISIBLE);
                    //scroll = true;
                }
                break;
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    /**
     *自定义ListView上的Item的点击事件继承自OnItemClickListener
     */
    private class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyL.e("点击事件被触发");

            //心语互享的逻辑跳转
            HeartShareItemFragment itemFram = new HeartShareItemFragment();   //new一个 Fragmen的实例
            Bundle bundle = new Bundle();
            bundle.putSerializable("item",mDatas.get(position));
            itemFram.setArguments(bundle);

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.heart_content,itemFram,"shareitem")
                    .addToBackStack("shareitem")
                    .commit();
           GlobalVariable.SHARE_LIST_FLAG=false;  //设置跳转标志位为全部动态
        }
    }




}
