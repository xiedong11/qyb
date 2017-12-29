package com.zhuandian.qxe.ExtralModule.heartshare;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.adapter.HeartShareAdapter;
import com.zhuandian.qxe.bean.HeartShare;
import com.zhuandian.qxe.bean.Myuser;
import com.zhuandian.qxe.utils.GlobalVariable;
import com.zhuandian.qxe.utils.myUtils.MyL;
import com.zhuandian.qxe.utils.myUtils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 状态当前用户下发布的所有动态
 * Created by 谢栋 on 2017/1/4.
 */
public class CurrentUserShareList extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private HeartShareAdapter myAdapter;

    private SwipeRefreshLayout swipeRefresh;
    List<com.zhuandian.qxe.bean.HeartShare> mDatas = new ArrayList<>();    //数据集合

    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.current_user_share_list, null);

        //初始化Bmob的SDK
        Bmob.initialize(getActivity(), "df25a6c6a79479d11a60f2e89c68b467");
        init();

        loadDatas();
//        view.setClickable(true);
        return view;
    }

    /**
     * 装载数据
     */
    private void loadDatas() {


        Myuser user = BmobUser.getCurrentUser(Myuser.class);
        BmobQuery<HeartShare> query = new BmobQuery<com.zhuandian.qxe.bean.HeartShare>();
        query.addWhereEqualTo("author", user);    // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<HeartShare>() {

            @Override
            public void done(List<com.zhuandian.qxe.bean.HeartShare> object, BmobException e) {
                if (e == null) {

                    mDatas = object;   //查询到的数据赋值给mDatas

                    myAdapter = new HeartShareAdapter(getActivity(), object);
                    listView.setAdapter(myAdapter);
                    swipeRefresh.setRefreshing(false);
//                    MyL.e("成功查询——————————" + object.size() + object.get(0).getUsername());
                } else {
                    MyL.e("失败：" + e.getMessage());
                }
            }


        });

    }


    /**
     * 初始化主布局上的各控件及配置
     */
    private void init() {
        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("我的动态");  //重置Toolbar上的标题
        listView = (ListView) view.findViewById(R.id.heart_list);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.heart_list_swipeRefresh);
        listView.setOnScrollListener(this);
        listView.setOnItemLongClickListener(this);
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
                Snackbar snackbar = Snackbar.make(view, "无最新数据可加载...", Snackbar.LENGTH_SHORT);
                MyUtils.setSnackbarMessageTextColor(snackbar, Color.parseColor("#ffffff"));
                snackbar.show();

            }
        });


//
    }

    private int currentIndex = 0;  //记录当前ListView的索引

    /**
     * 监听ListView的滚动状态
     *
     * @param view
     * @param scrollState 开始滚动（SCROLL_STATE_FLING）
     *                    正在滚动(SCROLL_STATE_TOUCH_SCROLL)
     *                    已经停止（SCROLL_STATE_IDLE）
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * listView的长按监听事件,实现用户长按可编辑删除属于自己发布的动态
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        Snackbar snackbar =Snackbar.make(view, "确定删除此条动态吗？", Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {


                        HeartShare share = new HeartShare();
                        share.setObjectId(mDatas.get(position).getObjectId());  //删除当前position下ObjectId()为对应ObjectId()的动态
                        share.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                swipeRefresh.setRefreshing(true);   //设置swipeRefresh加载动画
                                loadDatas();   //数据删除后，重新加载一遍数据
                                Snackbar snackbar=Snackbar.make(listView, "删除成功 ！", Snackbar.LENGTH_SHORT);
                                MyUtils.setSnackbarMessageTextColor(snackbar,Color.parseColor("#FFFFFF"));  //设置Snackbar上的字体颜色为白色
                                snackbar.show();
                            }

                        });
                    }
                })
                .setActionTextColor(Color.RED);

        MyUtils.setSnackbarMessageTextColor(snackbar,Color.parseColor("#FFFFFF"));  //设置Snackbar上的字体颜色为白色
        snackbar.show();


        return true;
    }


    /**
     * 自定义ListView上的Item的点击事件继承自OnItemClickListener
     */
    private class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyL.e("点击事件被触发");

            //心语互享的逻辑跳转
            HeartShareItemFragment itemFram = new HeartShareItemFragment();   //new一个 Fragmen的实例
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", mDatas.get(position));
            itemFram.setArguments(bundle);

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.heart_content, itemFram, "currentShareList")
                    .addToBackStack("currentShareList")
                    .commit();
            GlobalVariable.SHARE_LIST_FLAG = true;    //跳转全局开关，设置跳转标志位当前用户的动态
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("心语互享");  //恢复Toolbar上的标题
    }
}
