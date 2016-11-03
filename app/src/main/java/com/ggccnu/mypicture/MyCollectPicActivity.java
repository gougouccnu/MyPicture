package com.ggccnu.mypicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ggccnu.mypicture.Model.picInf;
import com.ggccnu.mypicture.adaptor.PictureAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import static android.widget.LinearLayout.HORIZONTAL;

public class MyCollectPicActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rvMyPictures;
    private LinearLayoutManager mLayoutManager;
    private List<picInf> mMyPicList = new ArrayList<>();
    private PictureAdapter mPictureAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        queryPictures();
    }

    private void initViews() {

        rvMyPictures = (RecyclerView) findViewById(R.id.rv_myPic);
        mLayoutManager = new LinearLayoutManager(MyCollectPicActivity.this, HORIZONTAL, false);
        rvMyPictures.setLayoutManager(mLayoutManager);
    }

    private void queryPictures() {

        BmobQuery<picInf> picturesBmobQuery = new BmobQuery<>();
        picturesBmobQuery.order("createdAt");
        picturesBmobQuery.setLimit(50);
        picturesBmobQuery.addWhereEqualTo("starter", "JLkR444G");
        picturesBmobQuery.findObjects(MyCollectPicActivity.this, new FindListener<picInf>() {
            @Override
            public void onSuccess(List<picInf> list) {
                if (list != null && list.size() > 0) {
                    mMyPicList.addAll(list);
                    mPictureAdapter = new PictureAdapter(mMyPicList, MyCollectPicActivity.this);
                    rvMyPictures.setAdapter(mPictureAdapter);
                    mPictureAdapter.setOnItemClickLitener(new PictureAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemLongClick(View view, int position) {
                            // 跳转到article详细页面
                            Intent intent = new Intent(MyCollectPicActivity.this, SecondActivity.class);
                            intent.putExtra("itemUrl", mMyPicList.get(position).getItemUrl());
                            startActivity(intent);
                        }
                    });
                }
                Log.d(TAG, "query new Articles onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "query new Articles onError");
            }
        });
    }

}
