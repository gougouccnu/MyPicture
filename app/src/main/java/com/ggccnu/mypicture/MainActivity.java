package com.ggccnu.mypicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ggccnu.mypicture.Model.picInf;
import com.ggccnu.mypicture.adaptor.PictureAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;

import static android.widget.LinearLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rvMyPictures;
    private LinearLayoutManager mLayoutManager;
    private List<picInf> mMyPicList = new ArrayList<>();
    private PictureAdapter mPictureAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "b78b5e674bac32a880a7b65c36531534");
        BmobUpdateAgent.update(this);

        Fresco.initialize(this);

        initViews();
        queryPictures();
    }

    private void initViews() {

        rvMyPictures = (RecyclerView) findViewById(R.id.rv_myPic);
        mLayoutManager = new LinearLayoutManager(MainActivity.this, HORIZONTAL, false);
        rvMyPictures.setLayoutManager(mLayoutManager);
    }

    private void queryPictures() {

        BmobQuery<picInf> picturesBmobQuery = new BmobQuery<>();
        picturesBmobQuery.order("createdAt");
        picturesBmobQuery.setLimit(15);
        //picturesBmobQuery.addWhereEqualTo("cid", categoryID);
        picturesBmobQuery.findObjects(MainActivity.this, new FindListener<picInf>() {
            @Override
            public void onSuccess(List<picInf> list) {
                if (list != null && list.size() > 0) {
                    mMyPicList.addAll(list);
                    mPictureAdapter = new PictureAdapter(mMyPicList, MainActivity.this);
                    rvMyPictures.setAdapter(mPictureAdapter);
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
