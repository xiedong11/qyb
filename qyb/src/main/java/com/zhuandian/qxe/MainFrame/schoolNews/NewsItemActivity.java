package com.zhuandian.qxe.MainFrame.schoolNews;

import android.databinding.ViewDataBinding;
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
import com.zhuandian.qxe.databinding.ActivityNewsItemBinding;
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
public class NewsItemActivity extends QYBActivity<NewsItemViewModel,ActivityNewsItemBinding> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_news_item;
    }

    @Override
    public void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("曲园快讯");
        toolbar.setNavigationIcon(R.drawable.md_nav_icon);  //设置向上的返回箭头
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
        viewModel.initView();
    }
}
