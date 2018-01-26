package com.zhuandian.qxe.MainFrame.schoolNews;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;
import com.zhuandian.qxe.entity.NewsEntity;
import com.zhuandian.qxe.utils.GlobalVariable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 谢栋 on 2016/9/3.
 */
public class NewsItemActivity extends QYBActivity {

    private NewsEntity newsEntity;
    private List<NewsEntity> newsEntities;

    private RequestQueue queue;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.image1)
    ImageView imageview1;
    @BindView(R.id.image2)
    ImageView imageview2;


    private String newsTitle = "";    //接收从newsfragment传递过来的新闻标题


    @Override
    public int getLayoutId() {
        return R.layout.news_activity;
    }

    @Override
    public void setupView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("曲园快讯");
        toolbar.setNavigationIcon(R.drawable.ic_found_course_icon);  //设置向上的返回箭头
        //箭头的点击监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void setModle() {
        newsEntity = new NewsEntity();
        //创建一个Volley请求队列
        queue = Volley.newRequestQueue(this);
        //得到Bundle对象，用户获取newsFragment传递过来的数据
        Bundle bundle = getIntent().getExtras();
        String newsurl = bundle.getString("newsUrl");
        newsTitle = bundle.getString("newstitle");

        new NewsTask().execute(GlobalVariable.JWC_URL + newsurl);
    }


    private class NewsTask extends AsyncTask<String, Void, NewsEntity> {

        @Override
        protected NewsEntity doInBackground(String... params) {

            Document doc;
            String image2 = "";
            String image1 = "";
            Log.i("xie111", params[0]);
            try {
                doc = Jsoup.connect(params[0]).get();

                Elements listClass = doc.getElementsByAttributeValue("class", "zw_content");
                for (Element listUrl : listClass) {


                    try {
                        image1 = listUrl.getElementsByTag("img").get(0).attr("src");
                    } catch (Exception e) {
                        image1 = "http://123";
                    } finally {
                        loadIamge(GlobalVariable.JWC_URL + image1, imageview1);
                    }


                    //防止新闻内容中没有第二个图片链接，做try-catch处理
                    try {
                        image2 = listUrl.getElementsByTag("img").get(1).attr("src");
                    } catch (Exception e) {
                        image2 = "http://123";
                    } finally {
                        loadIamge(GlobalVariable.JWC_URL + image2, imageview2);
                    }
                    String text = listUrl.getElementsByTag("span").text();

                    Log.i("xiedong", image1 + text);
                    newsEntity.setItemContent(text);

//                    content.setText(text);


                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return newsEntity;

        }

        @Override
        protected void onPostExecute(NewsEntity newsEntity) {
            content.setText(newsEntity.getItemContent());
            title.setText(newsTitle);
        }
    }


    //使用Volley加载图片
    private void loadIamge(String s, final ImageView imageView) {

        ImageRequest request = new ImageRequest(s,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 200, 150, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        imageView.setImageResource(R.drawable.jwc_news_logo);
                    }
                }
        );

        //千万要把请求添加到请求队列，否则请求不会生效
        queue.add(request);

    }
}
