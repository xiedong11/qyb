package com.zhuandian.qxe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
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
import com.zhuandian.qxe.bean.GoodsBean;
import com.zhuandian.qxe.utils.myUtils.MyImageCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 二手商品列表ListView的数据adapter
 *
 * @author 谢栋
 *         2016年8月21日10:39:39
 */

public class MyESGoodsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<GoodsBean> beens;

    private RequestQueue queue;   //volley请求

    public MyESGoodsAdapter(Context context, List<GoodsBean> datas) {
        mInflater = LayoutInflater.from(context);
        beens = datas;
//       Log.i("xiedong",beens.get(9).getGoodsTiltle()+"");

        //实例化Volley请求队列
        queue = Volley.newRequestQueue(context);

    }

    @Override
    public int getCount() {
        return beens.size();
    }

    @Override
    public Object getItem(int position) {
        return beens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.esgoods_item,null);
            convertView = mInflater.inflate(R.layout.esgoods_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);

            //加载图片的时候忘记找控件了，白浪费了好多脑细胞
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        GoodsBean mGoodsBenn = beens.get(position);
        viewHolder.title.setText(mGoodsBenn.getGoodsTiltle() + "");
        viewHolder.content.setText(mGoodsBenn.getGoodsContent() + "");
        viewHolder.price.setText("￥" + mGoodsBenn.getPrice());

        //正则切割时间字符串 2016-10-06 14:58:06
        String time[] = mGoodsBenn.getCreatedAt().split(" ");
        viewHolder.time.setText(time[0]);

//            setBitmap(mGoodsBenn.getGoodsUrl(),viewHolder.image);

//            viewHolder.image.setImageBitmap(setBitmap(mGoodsBenn.getGoodsUrl(),));


//            asyncImage(viewHolder.image, mGoodsBenn.getGoodsUrl());
//            Log.i("xiedong",beens.get(position).getGoodsTiltle()+beens.get(position).getGoodsContent());

//            viewHolder.image. setTag ( mGoodsBenn.getGoodsUrl() ) ;


        //使用Volley的缓存机制，三级缓存，磁盘，内存，网络
        ImageLoader imageLoader = new ImageLoader(queue,
                MyImageCache.getMyImageCache());
        imageLoader.get(mGoodsBenn.getGoodsUrl(), // url
                ImageLoader.getImageListener(viewHolder.image,// imageview控件
                        R.drawable.gesture,// 等待下载的图片
                        R.drawable.gesture),// 网络异常的图片
                100, 80// 二次采样的大小
        );


        //只
//                loadImageByImageLoader(imageView, mGoodsBenn.getGoodsUrl());

        return convertView;
    }

    /**
     * 使用volley的ImageLoader加载图片（自带缓存）
     *
     * @param image
     * @param goodsUrl
     */
    private void loadImageByImageLoader(ImageView image, String goodsUrl) {

        //创建ImageLoader对象，参数（加入请求队列，自定义缓存机制）
        ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());


        //获取图片监听器 参数（要显示的ImageView控件，默认显示的图片，加载错误显示的图片）
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(image,
                R.drawable.gesture,
                R.drawable.gesture);

        imageLoader.get(goodsUrl, listener, 350, 350);
    }


    //自定义图片缓存BitmapCache
    private class BitmapCache implements ImageLoader.ImageCache {

        //使用安卓提供的缓存机制
        private LruCache<String, Bitmap> mCache;

        //重写构造方法
        private BitmapCache() {
            int maxSize = 10 * 1024 * 1024;  //缓存大小为10兆
            mCache = new LruCache<String, Bitmap>(maxSize);

        }


        @Override
        public Bitmap getBitmap(String s) {
            return mCache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            mCache.put(s, bitmap);

        }
    }


    //异步任务类加载网络图片
    private void asyncImage(ImageView image, String goodsUrl) {
        AsyncImageLoad asyncImageLoad = new AsyncImageLoad(image);
        asyncImageLoad.execute(goodsUrl);
    }


    private class AsyncImageLoad extends AsyncTask<String, Void, Bitmap> {


        private ImageView image;
        private String imageUrl;

        //构造方法，给image传递值
        public AsyncImageLoad(ImageView image) {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            URLConnection conn;
            InputStream is;

            Bitmap bitmap = null;

            try {
                conn = new URL(params[0]).openConnection();

                is = conn.getInputStream();

//            BufferedInputStream bis = new BufferedInputStream(is);

                //用BitmapFactory的decodeStream()方法解析bitmap
                bitmap = BitmapFactory.decodeStream(is);

                Log.i("xied", "success");
                //关闭流
//            bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {

                Log.i("xied", bitmap.hashCode() + "这次看你有没有");
                image.setImageBitmap(bitmap);

                Log.i("xied", "啦啦啦");
            } else {
                image.setImageResource(R.drawable.ic_launcher);
                Log.i("xied", "图片没加载到");

            }
        }
    }

    private void setBitmap(String goodsUrl, ImageView image) {
        URLConnection conn;
        InputStream is;

        Bitmap bitmap = null;

        try {
            conn = new URL(goodsUrl).openConnection();

            is = conn.getInputStream();

//            BufferedInputStream bis = new BufferedInputStream(is);

            //用BitmapFactory的decodeStream()方法解析bitmap
            bitmap = BitmapFactory.decodeStream(is);

            Log.i("xied", "success");
            image.setImageBitmap(bitmap);
            //关闭流
//            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class ViewHolder {
        private TextView title;
        private TextView content;
        private ImageView image;
        private TextView price;
        private TextView time;
    }
}