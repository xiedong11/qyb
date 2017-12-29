package com.zhuandian.qxe.ExtralModule.gradeQuery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBFragment;

import butterknife.BindView;


/**
 * Created by 谢栋 on 2017/5/14.
 */

public class QueryFragment extends QYBFragment implements WebViewSetting {
    @BindView(R.id.webview)
    WebView webView;
    private final String CET_4_6_URL = "http://cjcx.neea.edu.cn/html1/folder/1508/206-1.htm?sid=280";
    private final String COMPUTER_SCORE = "http://cjcx.neea.edu.cn/html1/folder/1508/206-1.htm?sid=300";
    private final String INTERRUPT_SCORE = "http://cjcx.neea.edu.cn/html1/folder/1508/206-1.htm?sid=420";
    private final String PUTONGHUA_SCORE = "http://www.cltt.org/studentscore";
    private final String SPOKEN_ENGLISH_URL = "http://cjcx.neea.edu.cn/html1/folder/1508/206-1.htm?sid=409";
    private String type;


    public static QueryFragment getInstance(String title) {
        QueryFragment queryFragment = new QueryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        queryFragment.setArguments(bundle);
        return queryFragment;
    }



    @Override
    protected void setModle() {
    }

    @Override
    protected void setupView() {
        type = getArguments().getString("title");
        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText(type);
        loadWebViewByType(type);
    }

    private void loadWebViewByType(String type) {

        switch (type) {
            case "英语四六级":
                webView.loadUrl(CET_4_6_URL);
                break;
            case "英语口语":
                webView.loadUrl(SPOKEN_ENGLISH_URL);
                break;
            case "计算机":
                webView.loadUrl(COMPUTER_SCORE);
                break;
            case "普通话":
                webView.loadUrl(PUTONGHUA_SCORE);
                break;
            case "翻译":
                webView.loadUrl(INTERRUPT_SCORE);
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query;
    }

    @Override
    public void webViewSetting() {
        // 设置WebView属性，能够执行JavaScript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        webView.getSettings().setUseWideViewPort(true);

        webView.setInitialScale(70);   //100代表不缩放
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("成绩查询");
    }
}
