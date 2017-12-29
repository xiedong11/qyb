package com.zhuandian.qxe.MainFrame.esGoods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.GoodsBean;
import com.zhuandian.qxe.bean.Myuser;
import com.zhuandian.qxe.utils.myUtils.MyImageCache;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/9/1.
 */
public class ItemActivity extends ActionBarActivity {

    private TextView name;
    private TextView phone;
    private TextView miaoshu;
    private TextView price;
    private ImageView image;
    private TextView maijianame;
    private RequestQueue queue;
    private Bitmap popBitmap;   //用于存放popwindow中的image
    private ImageView imageDesc;   //图片放大详情
    private String imageURL;      //图片地址





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemactivity_2);


        //设置ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //设置左上角的返回按键

//
//
//        //关键下面两句话，设置了回退按钮，及点击事件的效果
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });



        //实例化Volley请求队列
        queue = Volley.newRequestQueue(this);

        initView();

        Intent intent =new Intent();
        Bundle bundle =getIntent().getExtras();
        Serializable serializable = bundle.getSerializable("goodsInfo");

        if(serializable != null && serializable instanceof GoodsBean)
        {
            final GoodsBean g = (GoodsBean)serializable;
            Log.i("123", "--url=" + g.getGoodsUrl());
            Log.i("123", "--title="+g.getName());

            name.setText(g.getGoodsTiltle());
            miaoshu.setText(g.getGoodsContent());
            price.setText("￥\n"+g.getPrice());
            phone.setText(g.getPhone());
            maijianame.setText(g.getName());


            //联系卖家的点击监听事件
            ((LinearLayout)findViewById(R.id.call_me)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new SweetAlertDialog(ItemActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("联系他？")
                            .setContentText("确定要跟他取得电话联系吗？？")
                            .setCancelText("暂时不需要")
                            .setConfirmText("马上联系他")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();

//                                    //Intent.ACTION_CALL直接拨打，Intent.ACTION_DIAL打开拨号键盘
//                                    Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + g.getPhone()));
//                                    startActivity(intent);

                                    showIMActivity(Myuser.getCurrentUser().getUsername(),g.getName());


                                }
                            })
                            .show();


                }
            });

            //开启异步任务下载图片
//            new ImageLoad().execute(g.getGoodsUrl());

            //使用ImageLoad加载图片
//            loadImageByVolley(g.getGoodsUrl());

            imageURL=g.getGoodsUrl();  //设置图片的连接地址
            imageByImageLoad(g.getGoodsUrl());

        }

    }

    /**
     * 聊天界面
     * @param xie
     */
    private void showIMActivity(String myName ,final String sellName) {
        LCChatKit.getInstance().open(myName, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    finish();
                    Intent intent = new Intent(ItemActivity.this, LCIMConversationActivity.class);
                    intent.putExtra(LCIMConstants.PEER_ID, sellName);
                    startActivity(intent);
                } else {
                    Toast.makeText(ItemActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 使用ImageLoad加载图片，优化缓存图片机制
     * @param goodsUrl
     */
    private void imageByImageLoad(String goodsUrl) {

        //创建ImageLoader对象，参数（加入请求队列，自定义缓存机制）
        ImageLoader imageLoader =new ImageLoader(queue, new BitmapCache());


        //获取图片监听器 参数（要显示的ImageView控件，默认显示的图片，加载错误显示的图片）
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(image,
                R.drawable.jwc_news_logo,
                R.drawable.jwc_news_logo);

        imageLoader.get(goodsUrl,listener,350,350);
    }


    private class BitmapCache implements ImageLoader.ImageCache {

        //使用安卓提供的缓存机制
        private LruCache <String , Bitmap> mCache;

        //重写构造方法
        private BitmapCache() {
             int maxSize = 10*1024*1024;  //缓存大小为10兆
             mCache = new LruCache<String ,Bitmap>(maxSize);

             }


        @Override
        public Bitmap getBitmap(String s) {
            return mCache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            mCache.put(s,bitmap);

        }
    }



    /**
     * 使用Volley框架加载图片
     * @param goodsUrl
     * ImageRequest的构造函数接收六个参数，
     * 第一个参数就是图片的URL地址
     * 第二个参数是图片请求成功的回调，
     * 第三第四个参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行压缩，指定成0的话就表示不管图片有多大，都不会进行压缩。
     * 第五个参数用于指定图片的颜色属性，Bitmap.Config下的几个常量都可以在这里使用，其中ARGB_8888可以展示最好的颜色属性，每个图片像素占据4个字节的大小，而RGB_565则表示每个图片像素占据2个字节大小。
     * 第六个参数是图片请求失败的回调
     */
    private void loadImageByVolley(String goodsUrl) {

        ImageRequest request = new ImageRequest(goodsUrl, new Response.Listener<Bitmap>(){

            @Override
            public void onResponse(Bitmap bitmap) {
             image.setImageBitmap(bitmap);
                popBitmap = bitmap;   //存放popwindos中的image
            }
        },0,0, Bitmap.Config.RGB_565,new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                image.setImageResource(R.drawable.jwc_news_logo);
            }
        });

        //最后将这个ImageRequest对象添加到RequestQueue里
        queue.add(request);


    }


    /**
     * 采用异步任务加载图片
     */
    class ImageLoad extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream is;
            Bitmap bitmap=null;

            try {
                URL mUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)mUrl.openConnection();

                is =conn.getInputStream();

                bitmap = BitmapFactory.decodeStream(is);

                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }
    private void initView() {

        name = (TextView) findViewById(R.id.name);
        miaoshu = (TextView) findViewById(R.id.miaoshu);
        price = (TextView) findViewById(R.id.price);
        phone = (TextView) findViewById(R.id.phone);
        maijianame = (TextView) findViewById(R.id.maijianame);
        imageDesc = (ImageView) findViewById(R.id.image_desc);


        image  = (ImageView) findViewById(R.id.image);

        //点击下载图片，并详情显示
        image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //使用Volley的缓存机制，三级缓存，磁盘，内存，网络
                ImageLoader imageLoader = new ImageLoader(queue,
                        MyImageCache.getMyImageCache());
                imageLoader.get(imageURL, // url
                        ImageLoader.getImageListener(imageDesc,// imageview控件
                                R.drawable.gesture,// 等待下载的图片
                                R.drawable.gesture),// 网络异常的图片
                        100, 80// 二次采样的大小
                );


 /**************************************************未引入图片缓存的Volley请求***********************************************/
//                ImageLoader imageLoader =new ImageLoader(queue, new BitmapCache());
//
//
//                //获取图片监听器 参数（要显示的ImageView控件，默认显示的图片，加载错误显示的图片）
//                ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageDesc,
//                        R.drawable.background,
//                        R.drawable.background_2);
//
//                imageLoader.get(imageURL,listener,350,350);

                findViewById(R.id.image_desc_layout).setVisibility(View.VISIBLE);
            }
        });


        imageDesc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.image_desc_layout).setVisibility(View.INVISIBLE);   //设置点击图片隐藏ImageView控件

            }
        });
    }

    /**
     * 菜单的选择监听事件
     * @param item  点击条目
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //设置左上角按钮的点击监听事件
        if(item.getItemId()==android.R.id.home){

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 点击图片放大
     */

//    public void goods_image(View v){
//
//        View view = getLayoutInflater().inflate(R.layout.popwindow_goods_image,null);
//
//
//        ImageView imageView = (ImageView) view.findViewById(R.id.pop_image);
//        imageView.setImageBitmap(popBitmap);
//        Log.d("xiedong",popBitmap.hashCode()+"bitmap");
//
//        PopupWindow popupWindow = new PopupWindow(view,350,350);
//
//        //为方便测试，popupwindows中使用的资源引用安卓系统自带的资源
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));   //设置背景图片
//        popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);    //设置弹出的动画效果（可以自定义效果）
//        popupWindow.getBackground().setAlpha(50);   //设置透明度（0-255之间）
//        popupWindow.setOutsideTouchable(true);       //设置点击边缘区域windows消失
//        popupWindow.setFocusable(true);
//        popupWindow.setTouchable(true);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);    //设置软键盘的弹出自适应
//
//
//        popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);   //设置popupWindows的显示位置，0,0代表偏移量
//        popupWindow.showAsDropDown(view);
//    }

}
