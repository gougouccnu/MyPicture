package com.ggccnu.mypicture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by lishaowei on 2016/10/30.
 */
public class SecondActivity extends AppCompatActivity {

    private WebView wv_item;
    private String itemUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        itemUrl = intent.getStringExtra("itemUrl");

        initViews();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews() {
        wv_item = (WebView) findViewById(R.id.wv_item);
        wv_item.getSettings().setJavaScriptEnabled(true);
        wv_item.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                        super.doUpdateVisitedHistory(view, url, isReload);
                    }
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return false;
                    }
                }

        );
        wv_item.loadUrl(itemUrl);
    }
}
