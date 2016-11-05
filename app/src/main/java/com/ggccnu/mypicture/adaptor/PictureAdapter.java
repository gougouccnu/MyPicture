package com.ggccnu.mypicture.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ggccnu.mypicture.Model.picInf;
import com.ggccnu.mypicture.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private static final int MSG_GET_ARTICLE_PIC = 10;
    private static final int MSG_GET_AUTHOR_PIC = 11;
    private List<picInf> mPicList;
    private Context mContext;

    public PictureAdapter(List<picInf> data, Context context) {
        mPicList = data;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_pic, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Uri uri = Uri.parse(mPicList.get(position).getPicUrl());
        //holder.iv_pic.setImageURI(uri);

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.d("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                FLog.d("Intermediate image received", "intermedia");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                FLog.e(getClass(), throwable, "Error loading %s", id);
            }
        };

        int width = 200, height = 200;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setOldController(holder.iv_pic.getController())
                .setImageRequest(request)
                .setControllerListener(controllerListener)
                .build();

        holder.iv_pic.setController(controller);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView iv_pic;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_pic = (SimpleDraweeView) itemView.findViewById(R.id.iv_pic);
        }

    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    // TODO:refactor
    //加载图片
    private Bitmap getURLimage(String url, int reqWidth, int reqHeight) {
        Bitmap bmp = null;
        HttpURLConnection conn;
        InputStream is;
        try {
            conn = getUrlConnection(url);
            if (conn != null) {
                is = conn.getInputStream();//获得图片的数据流
            } else {
                is = null;
            }
            // 第一次加载时 将inJustDecodeBounds设置为true 表示不真正加载图片到内存
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);

            // 根据目标宽和高 以及当前图片的大小 计算出压缩比率
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // 将inJustDecodeBounds设置为false 真正加载图片 然后根据压缩比率压缩图片 再去解码
            options.inJustDecodeBounds = false;
            // inputStream不能重用，要先关掉，再new一个
            if (is != null) {
                is.close();
            }
            if (conn != null) {
                conn.disconnect();
            }

            // 新建conn，重新获取图片
            conn = getUrlConnection(url);
            if (conn != null) {
                is = conn.getInputStream();
            }
            bmp = BitmapFactory.decodeStream(is, null, options);
            if (is != null) {
                is.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    private HttpURLConnection getUrlConnection(String url) {
        URL myurl = null;
        try {
            myurl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //计算压缩比率 android官方提供的算法
    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            //将当前宽和高 分别减小一半
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
