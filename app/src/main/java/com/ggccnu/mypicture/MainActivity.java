package com.ggccnu.mypicture;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ggccnu.mypicture.Model.MyUser;
import com.ggccnu.mypicture.Model.picInf;
import com.ggccnu.mypicture.adaptor.PictureAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.widget.LinearLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rvMyPictures;
    private LinearLayoutManager mLayoutManager;
    private List<picInf> mMyPicList = new ArrayList<>();
    private PictureAdapter mPictureAdapter;
    private boolean isCollected = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        queryPictures();
    }

    private void initViews() {

        rvMyPictures = (RecyclerView) findViewById(R.id.rv_myPic);
        mLayoutManager = new LinearLayoutManager(MainActivity.this, HORIZONTAL, false);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvMyPictures);
        rvMyPictures.setLayoutManager(mLayoutManager);
    }

    private void queryPictures() {

        BmobQuery<picInf> picturesBmobQuery = new BmobQuery<>();
        picturesBmobQuery.order("createdAt");
        picturesBmobQuery.setLimit(100);
        picturesBmobQuery.addWhereEqualTo("user", "JLkR444G");
        picturesBmobQuery.include("user, starter");
        picturesBmobQuery.findObjects(MainActivity.this, new FindListener<picInf>() {
            @Override
            public void onSuccess(List<picInf> list) {
                if (list != null && list.size() > 0) {
                    mMyPicList.addAll(list);
                    mPictureAdapter = new PictureAdapter(mMyPicList, MainActivity.this);
                    rvMyPictures.setAdapter(mPictureAdapter);
                    mPictureAdapter.setOnItemClickLitener(new PictureAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemLongClick(View view, final int position) {
                            String itemMsg = "收藏该宝贝";

                            MyUser starter = mMyPicList.get(position).getStarter();
                            if(!starter.getObjectId().contentEquals("none")) {
                                itemMsg = "不收藏这个宝贝了";
                                isCollected = false;
                            }
                            final String items[] = {itemMsg, "查看淘宝网页"};

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //builder.setTitle("提示"); //设置标题
                            //builder.setMessage("是否确认退出?"); //设置内容
                            //builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                            //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            UpdatePicStarter(mMyPicList.get(position).getObjectId(), mMyPicList.get(position).getUser().getObjectId(),isCollected);
                                            //UpdatePicItemUrl(mMyPicList.get(position).getObjectId(),"");
                                            break;
                                        case 1:
                                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                            intent.putExtra("itemUrl", mMyPicList.get(position).getItemUrl());
                                            startActivity(intent);
                                        default:
                                            break;
                                    }
                                }
                            });

//                            builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//
//                                }
//                            });
                            builder.create().show();
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

    private void UpdatePicStarter(String objectId, String starterObjectId, boolean isCollected) {
        picInf p = new picInf();
        //p.setObjectId(objectId);
        MyUser starter = new MyUser();
        if(!isCollected) {
            starter.setObjectId("none");
        } else {
            starter.setObjectId(starterObjectId);
        }
//        p.setStarter(starter);
        p.setValue("starter", starter);
        p.update(MainActivity.this, objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "update success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MainActivity.this, "update failed" + s, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void UpdatePicItemUrl(String objectId, String newItemUrl) {
        picInf p = new picInf();
        p.setValue("itemUrl", newItemUrl);
        p.update(MainActivity.this, objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "update item url success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MainActivity.this, "update item url failed", Toast.LENGTH_LONG).show();
            }
        });
    }

}
