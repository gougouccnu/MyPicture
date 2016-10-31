package com.ggccnu.mypicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by lishaowei on 2016/10/30.
 */
public class SecondActivity extends AppCompatActivity {

    private TextView tv_itemUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tv_itemUrl = (TextView) findViewById(R.id.tv_itemUrl);

        Intent intent = getIntent();
        String itemUrl = intent.getStringExtra("itemUrl");
        tv_itemUrl.setText(itemUrl);

    }

    private void initViews() {

    }
}
