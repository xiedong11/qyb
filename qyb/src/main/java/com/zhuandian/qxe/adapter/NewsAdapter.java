package com.zhuandian.qxe.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.NewsBean;
import com.zhuandian.qxe.utils.GlobalVariable;
import com.zhuandian.qxe.utils.myUtils.MyImageCache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by 谢栋 on 2016/9/3.
 */
public class NewsAdapter extends BaseAdapter {

    private final RequestQueue queue;
    private List<NewsBean> newsBeans;
    private LayoutInflater mInflater;

    //初始化构造方法
    public NewsAdapter(Context context ,List<NewsBean > datas) {
        mInflater = LayoutInflater.from(context);
        newsBeans =datas;
        //实例化Volley请求队列
        queue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount() {
        return newsBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return newsBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;

        if(convertView==null){

            //利用打气筒的方式填充一个VIew对象
            convertView = mInflater.inflate(R.layout.news_item,null);

            viewHolder = new ViewHolder();
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.news_time);

            convertView.setTag(viewHolder);

        }else{
           viewHolder = (ViewHolder) convertView.getTag();
        }

        //为item添加内容
        viewHolder.content.setText(newsBeans.get(position).getTitle()+"");
        viewHolder.time.setText(newsBeans.get(position).getTime()+"");


        /*************************放弃新闻图片加载***************************************

         //        http://www.qfnu.edu.cn/index_files/logo.jpg

//        第一步爬取会把标题和链接缓存，上下滑动会根据链接把图片再进行一次缓存，两次的缓存不在一个频道上，上下拖动肯定会有卡顿的，除非网特别好
//        new MyImgTask(newsBeans.get(position).getContentUrl(),viewHolder.newsImg).execute("");
        ImageLoader imageLoader = new ImageLoader(queue,
                MyImageCache.getMyImageCache());
        imageLoader.get(GlobalVariable.JWC_URL+newsBeans.get(position).getImage1URL(), // 图片url
                ImageLoader.getImageListener(viewHolder.newsImg,// imageview控件
                        R.drawable.gesture,// 等待下载的图片
                        R.drawable.gesture),// 网络异常的图片
                100, 80// 二次采样的大小
        );*/


        return convertView;
    }




    /**********************************************类未调用，留后期加载图片使用，暂不删除*************************************
    /**
     * 传入指定URL得到相关Item下的新闻资讯的图片到Listview的列表上
     */
    class MyImgTask extends AsyncTask<String ,Void,String> {

        private String url;
        private ImageView imageView;
        private String imgUrl="http://www.qfnu.edu.cn/index_files/logo.jpg";

        public MyImgTask(String url,ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(String... params) {
            Document document = null;
            try {
                document = Jsoup.connect(GlobalVariable.JWC_URL+url).get();

                Elements listImg = document.getElementsByAttributeValue("class","zw_content");
                if (listImg.size()>0) {  //防止加载不到图片导致的角标越界异常
                     imgUrl = listImg.get(0).getElementsByTag("img").attr("src"); //得到第一个Elements节点下的img地址
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



            return imgUrl;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.e("xiedong", GlobalVariable.JWC_URL+s);
//            使用Volley的缓存机制，三级缓存，磁盘，内存，网络
            ImageLoader imageLoader = new ImageLoader(queue,
                    MyImageCache.getMyImageCache());
            imageLoader.get(GlobalVariable.JWC_URL+s, // 图片url
                    ImageLoader.getImageListener(imageView,// imageview控件
                            R.drawable.gesture,// 等待下载的图片
                            R.drawable.gesture),// 网络异常的图片
                    100, 80// 二次采样的大小
            );
        }


    }

    private class ViewHolder{
        private TextView content;
        private TextView time;
    }
}
