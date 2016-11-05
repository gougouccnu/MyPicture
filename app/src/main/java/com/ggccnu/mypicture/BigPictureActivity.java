package com.ggccnu.mypicture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by lishaowei on 2016/11/4.
 */
public class BigPictureActivity extends AppCompatActivity {

    private String picUrl;
    private SimpleDraweeView iv_pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);

        Intent intent = getIntent();
        picUrl = intent.getStringExtra("picUrl");

        initViews();
    }

    private void initViews() {
        iv_pic = (SimpleDraweeView) findViewById(R.id.iv_pic);
        Uri uri = Uri.parse(picUrl);
        iv_pic.setImageURI(uri);
    }
}
