package com.ggccnu.mypicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

public class SplashActivity extends AppCompatActivity {

    private Button btn_newPic;
    private Button btn_myCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Bmob.initialize(this, "b78b5e674bac32a880a7b65c36531534");
        BmobUpdateAgent.update(this);

        Fresco.initialize(this);

        initViews();
    }

    private void initViews() {

        btn_newPic = (Button) findViewById(R.id.btn_newPic);
        btn_myCollect = (Button) findViewById(R.id.btn_myCollect);

        btn_newPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn_myCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, MyCollectPicActivity.class);
                startActivity(intent);
            }
        });
    }

}
