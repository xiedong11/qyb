package com.zhuandian.qxe.MainFrame.schoolNews;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.adapter.NewsAdapter;
import com.zhuandian.qxe.base.QYBFragment;
import com.zhuandian.qxe.entity.NewsEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiedong on 2017/7/15.
 */

public class CommonNewsFragment extends QYBFragment implements AdapterView.OnItemClickListener {
    private static final int OK_DATA = 1;
    @BindView(R.id.header_layout)
    TextView headerLayout;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.load_animation)
    LinearLayout loadLinearLayout;   //用户等待视图
    private List<NewsEntity> newsEntities;
    private NewsAdapter newsAdapter;

    public static CommonNewsFragment getInstance(String type, String url) {
        CommonNewsFragment fragment = new CommonNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("news_url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == OK_DATA) {
                listView.setAdapter(newsAdapter);
                loadLinearLayout.setVisibility(View.INVISIBLE);   //用户等待视图
            }
        }
    };


    @Override
    protected void setupView() {
        //设置下拉刷新的动画的颜色
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new MyAsyncTask().execute();    //模拟加载数据过程，并未真实加载数据

            }
        });
        //listview设置监听事件
        listView.setOnItemClickListener(this);
        newsEntities = new ArrayList<NewsEntity>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void setModle() {
        checkNewsType();
    }

    private void checkNewsType() {
        String type = getArguments().getString("type");
        switch (type) {
            case "XXYW":
                initData("http://www.qfnu.edu.cn/xxyw.htm");
                break;
            case "XYCZ":
                initData("http://www.qfnu.edu.cn/xycz.htm");
                break;
            case "TZGG":
                initData("http://www.qfnu.edu.cn/tzgg.htm");
                break;
            case "MTGZ":
                initData("http://www.qfnu.edu.cn/mtgz.htm");
                break;
        }

    }

    /**
     * 初始化数据
     *
     * @param url
     */
    private void initData(final String url) {
        new Thread() {
            @Override
            public void run() {
                Document doc;
                try {
                    doc = Jsoup.connect(url).get();
                    Elements listClass = doc.getElementsByAttributeValue("class", "newslist");
                    for (Element listName : listClass) {
                        Elements listItem = listName.getElementsByTag("li");
                        for (Element item : listItem) {
                            NewsEntity newsEntity = new NewsEntity();
                            //爬取新闻的标题、时间跟连接
                            String title = item.getElementsByTag("a").text().trim();
                            if ("下页".equals(title) || "尾页".equals(title)) {   //解析时出现此两个标题，跳出循环
                                continue;
                            }
                            String contentUrl = item.getElementsByTag("a").attr("href");
                            String newsTime = item.getElementsByTag("span").text().trim();
                            //添加进集合
                            newsEntity.setTitle(title);
                            newsEntity.setContentUrl(contentUrl);
                            newsEntity.setTime(newsTime);
                            newsEntities.add(newsEntity);

                        }

                    }
                    newsAdapter = new NewsAdapter(getActivity(), newsEntities);
//                    listView.setAdapter(newsAdapter);
                    Message msg = new Message();
                    msg.what = OK_DATA;
                    mHandler.sendMessage(msg);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }


    /***
     * 为listview设置点击跳转
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("newsUrl", newsEntities.get(position).getContentUrl());
        bundle.putString("newstitle", newsEntities.get(position).getTitle());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    class MyAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

//            ******************加载新闻数据逻辑未实现*********************
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ("success".equals(s)) {
                swipeRefresh.setRefreshing(false);
                Snackbar snackbar = Snackbar.make(getView(), "未加载到新数据...", Snackbar.LENGTH_SHORT);
                setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
            }
        }
    }

    /**
     * 设置Snackbar上的字体颜色
     *
     * @param snackbar
     * @param color
     */
    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
