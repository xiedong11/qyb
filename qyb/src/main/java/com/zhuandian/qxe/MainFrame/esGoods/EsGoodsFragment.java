package com.zhuandian.qxe.MainFrame.esGoods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.MainFrame.ListItemFragment;
import com.zhuandian.qxe.adapter.MyESGoodsAdapter;
import com.zhuandian.qxe.bean.GoodsBean;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author 谢栋
 *         2016年8月14日21:11:16
 */
public class EsGoodsFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private ListView listViewGoods;
    private List<GoodsBean> beens;
    private View mview;
    private LayoutInflater mInflater;
    private int index = 0;
    private MyESGoodsAdapter myAdapter;
    private int lastItemIndex;   //设置listview的最后一个item的索引值
    private static int UPDATEDATA = 0x01;
    private View footView;  //定义底部进度条

    private int count=-10; //数据分页加载的分页值

    private boolean flag=true;   //为adapter装初值设置一个标志点

    private ListItemFragment mListItemFragment;    //用于显示用户商品详情的fragment
    private LinearLayout loadLinearLayout;   //用户等待视图
    private SwipeRefreshLayout swipeRefresh;
    private boolean pullState=false;    //刷新状态开关，true代表下拉


    //创建一个handle对象，用于线程之间的通信
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == UPDATEDATA) {

                //通知适配器更新数据
                myAdapter.notifyDataSetChanged();
            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //得到view对象，并且把view填充成一个布局文件
        mview = inflater.inflate(R.layout.esgoods, container, false);
        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("曲园淘宝");


        loadLinearLayout = (LinearLayout) mview.findViewById(R.id.load_animation);  //用户等待视图
        swipeRefresh = (SwipeRefreshLayout) mview.findViewById(R.id.swipe_refresh);
        //设置下拉刷新的动画的颜色
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //设置下拉刷新监听
        swipeRefresh.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullState=true; //置为下拉
               initData();

            }
        });

        //Fragment依存于activity，在Fragment中添加布局必须先找到上层activity
        mInflater = LayoutInflater.from(getActivity());
        listViewGoods = (ListView) mview.findViewById(R.id.listView);

        //初始化Bmob的SDK
        Bmob.initialize(getActivity(),"df25a6c6a79479d11a60f2e89c68b467");

        //绑定搜按钮的点击监听事件
        ((FloatingActionButton)mview.findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchGoodsFragment searchGoodsFragment = new SearchGoodsFragment();
//                Toast.makeText(getActivity(),"开发者还未移植该功能...",Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft =fm.beginTransaction();


                Log.i("xiedong","回退栈个数"+getFragmentManager().getBackStackEntryCount());

                //当搜索商品的fragment从未被加载过时，把searchGoodsFragment添加进fragment回退栈中
                if(getFragmentManager().getBackStackEntryCount()==0) {
                    ft.add(R.id.container,searchGoodsFragment,"searchGoodsFragment");
                    ft.addToBackStack("searchGoodsFragment");
                }
                ft.commit();  //提交事务

            }
        });


        //实例化fragment
        mListItemFragment =new ListItemFragment();
        //给listview的每个item设置监听事件有两种方式，一个是内部类实现，一个是实现listview的 AdapterView.OnItemClickListener接口，绑定this
//        listViewGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //参数中的每个View对象都对应一个item
//                TextView TV= (TextView) view;
//                Toast.makeText(getActivity(), TV.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
        listViewGoods.setOnItemClickListener(this);

        //给listview设置滚动监听事件
        listViewGoods.setOnScrollListener(this);

        //添加底部布局
        footView = mInflater.inflate(R.layout.loading, null);
        listViewGoods.addFooterView(footView);


//        footView.setVisibility(View.INVISIBLE);
//        listViewGoods.removeFooterView(footView);
        beens = new ArrayList<GoodsBean>();

        initData();





//        setListAdapter(myAdapter);

        return mview;
    }


    //开启一个子线程，模拟请求网络数据
    class MyThread extends Thread {
        @Override
        public void run() {

            //重新加载数据
            initData();
//            try {
//                MyThread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }





    /**
     * 装载数据
     */
    private void initData() {



//        listViewGoods.addFooterView(footView);
        count=count+10;

        //查询数据库中的具体商品信息
        final GoodsBean goodsBenn = new GoodsBean();

        BmobQuery<GoodsBean> query = new BmobQuery<GoodsBean>();
        ////查询playerName叫“比目”的数据
        //        query.addWhereEqualTo("playerName", "比目");
        //返回50条数据，如果不加上这条语句，默认返回10条数据

//        Collections.reverse(list);     实现把list集合逆序排列

        query.order("-updatedAT");
        query.setLimit(10);
        query.setSkip(count);
        //执行查询方法
        query.findObjects(new FindListener<GoodsBean>() {
            @Override
            public void done(List<GoodsBean> object, BmobException e) {
                if (e == null) {

                    Log.i("xiedong",object.size()+"size");

                    if(object.size()==0){
                        //数据全部被加载，，已经没有可再加载的数据时，对footview的操作
                        ((TextView)(footView.findViewById(R.id.textView2))).setText("商品数据已全部加载");
                        ((TextView)(footView.findViewById(R.id.textView2))).setTextSize(15);
                        ((TextView)(footView.findViewById(R.id.textView2))).setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
                        ((ProgressBar)(footView.findViewById(R.id.progressBar))).setVisibility(View.INVISIBLE);

                    }
//                    toast("查询成功：共"+object.size()+"条数据。");
                    for (final GoodsBean goods : object) {

                      if(pullState==true) {   //下拉时数据装载在顶部位置
                          beens.add(0,goods);
                          pullState=false;
                      }else{               //上拉时数据装载在底部
                          beens.add(goods);
                      }
                    }


                    //如果是第一次加载数据


                    if(flag==true) {
                        loadLinearLayout.setVisibility(View.INVISIBLE);   //用户等级视图
                        flag=false;
                        myAdapter = new MyESGoodsAdapter(getActivity(), beens);
                        listViewGoods.setAdapter(myAdapter);
                        Log.i("xie", "数据加载第一次" );
//                        listViewGoods.removeFooterView(footView);

                    }else {

                        myAdapter.notifyDataSetChanged();
                        Log.i("xie", "数据加载第2次" );
//                        listViewGoods.removeFooterView(footView);

                        swipeRefresh.setRefreshing(false);   //加载数据完成，设置swipeRefresh状态为false
                    }
                } else {
                    //数据全部被加载，，已经没有可再加载的数据时，对footview的操作
                    ((TextView)(footView.findViewById(R.id.textView2))).setText("没有数据可加载了哇※");
                    ((TextView)(footView.findViewById(R.id.textView2))).setTextSize(15);
                    ((ProgressBar)(footView.findViewById(R.id.progressBar))).setVisibility(View.INVISIBLE);

                    Log.i("bmob", "12323132123");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

//        myAdapter.notifyDataSetChanged();
//        for (int i = 0; i < 10; i++) {
//            Bean been = new Bean();
//            been.title = "商品" + index;
//            been.content = "商品详情" + index;
//            index++;
//            Log.i("a", been.title + been.content);
//            beens.add(been);
//        }
//
//        Log.i("xxxxxx", beens.size() + "");
    }


    class GetBitmap extends AsyncTask<String ,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            URLConnection conn ;
            InputStream is;   //操作网络数据必须涉及IO操作


            Bitmap bitmap=null;
            try {
                //使用URL的openConnection()操作
                conn=new URL(params[0]).openConnection();

                //关联输入流
                is = conn.getInputStream();

                //用字符缓存输入流读取从网络上得到的数据
                BufferedInputStream bis = new BufferedInputStream(is);

                //用BitmapFactory的decodeStream()方法解析bitmap
                bitmap= BitmapFactory.decodeStream(bis);

                Log.i("xied","success");
                //关闭流
                bis.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

        }
    }
    private Bitmap getbitmap(String goodsUrl) {
        URLConnection conn ;
        InputStream is;   //操作网络数据必须涉及IO操作


        Bitmap bitmap=null;
        try {
            //使用URL的openConnection()操作
            conn=new URL(goodsUrl).openConnection();

            //关联输入流
            is = conn.getInputStream();

            //用字符缓存输入流读取从网络上得到的数据
            BufferedInputStream bis = new BufferedInputStream(is);

            //用BitmapFactory的decodeStream()方法解析bitmap
            bitmap= BitmapFactory.decodeStream(bis);

            Log.i("xied","success");
            //关闭流
            bis.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    //实现listview的滚动监听事件

    /**
     * 监听listview的状态改变，三种状态第一是静止状态，SCROLL_STATE_IDLE
     * 第二是手指滚动状态，SCROLL_STATE_TOUCH_SCROLL
     * 第三是手指不动了，但是屏幕还在滚动状态。SCROLL_STATE_FLING
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //判断listview的滚动状态是停止，并且是滑动到最后一个item
        if (myAdapter.getCount() == lastItemIndex && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE ) {
            //执行下一页操作
//            listViewGoods.addFooterView(footView);
//            footView.setVisibility(View.VISIBLE);

             initData();



            Log.i("xiedong",lastItemIndex+"滑动停止");

            Log.i("xiedong","新数据加载");

//            myAdapter.notifyDataSetChanged();
////            发送消息到主线程
//            Message msg = new Message();
//            msg.what = UPDATEDATA;
//            mHandler.sendMessage(msg);
        }

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        Log.i("tag",lastItemIndex+"dd");
//        Toast.makeText(getActivity(),view.toString(),Toast.LENGTH_SHORT).show();
        //得到最后一个item的索引值
        lastItemIndex = firstVisibleItem + visibleItemCount - 1;


    }


    /**
     * listView的每个item的点击监听事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //参数中的每个View对象都对应一个item
//        TextView TV = (TextView) view;
//        Toast.makeText(getActivity(), "商品被点击", Toast.LENGTH_SHORT).show();

        Intent mIntent =new Intent(getActivity(),ItemActivity.class);

        Bundle bundle =new Bundle();

        bundle.putSerializable("goodsInfo",beens.get(position));
        mIntent.putExtras(bundle);
        startActivity(mIntent);

//        bundle.putParcelable("bitmap",beens.get(position).getGoodsBitmap());
//        bundle.putString("title",beens.get(position).getGoodsTiltle());
//

//       FragmentManager fm = getActivity().getSupportFragmentManager();
//
//
//       FragmentTransaction ft = fm.beginTransaction();
//
//        ft.replace(R.id.main_fragment, mListItemFragment, "itemFragment");
//        ft.addToBackStack("itemFragment");
//        ft.commit();

//        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
//                .setTitleText("联系卖主 ?")
//                .setContentText("卖家大名 ：" + beens.get(position).getName() + "\n联系方式\n" + beens.get(position).getPhone() + "")
//                .show();
    }
}
