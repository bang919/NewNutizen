package com.nutizen.nu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nutizen.nu.R;

/**
 * Created by Administrator on 2017/12/16.
 */

public class GlideUtils {

    /**
     * @param imgDefaultSource 0 or -1 not set default placeholder
     * @param transformation   CenterCrop/CircleCrop/FitCenterCenterInside/...
     */
    public static void loadImage(ImageView imageView, int imgDefaultSource, int imgSource, BitmapTransformation... transformation) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(imageView.getContext()).load(imgSource != 0 ? imgSource : imgDefaultSource != 1 ? imgDefaultSource : R.drawable.glide_default_bg);
        RequestOptions requestOptions = new RequestOptions();
        if (transformation.length != 0) {
            requestOptions.transforms(transformation);
        }
        if (imgDefaultSource != 0 && imgDefaultSource != -1) {
            requestOptions.placeholder(imgDefaultSource);
        } else {
            requestOptions.placeholder(R.drawable.glide_default_bg);
        }
        requestBuilder.apply(requestOptions);
        requestBuilder.into(imageView);
    }

    public static void loadImage(ImageView imageView, int imgDefaultSource, String imgUrl, BitmapTransformation... transformation) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(imageView.getContext()).load(!TextUtils.isEmpty(imgUrl) ? imgUrl : imgDefaultSource != 1 ? imgDefaultSource : R.drawable.glide_default_bg);
        RequestOptions requestOptions = new RequestOptions();
        if (transformation.length != 0) {
            requestOptions.transforms(transformation);
        }
        if (imgDefaultSource != 0 && imgDefaultSource != -1) {
            requestOptions.placeholder(imgDefaultSource);
        } else {
            requestOptions.placeholder(R.drawable.glide_default_bg);
        }
        requestBuilder.apply(requestOptions);
        requestBuilder.into(imageView);
    }

    /**
     * MyFirebaseMessagingService用到的用BitmapCallback加载
     */
    public static void requestBitmap(Context context, String url, final BitmapCallback bitmapCallback) {
        if (TextUtils.isEmpty(url)) {
            bitmapCallback.onBitmapCallback(null);
            return;
        }
        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if(resource!=null){
                    bitmapCallback.onBitmapCallback(FileUtils.drawable2Bitmap(resource));
                }else {
                    bitmapCallback.onBitmapFailure();
                }
            }
        });
    }

    public interface BitmapCallback {
        void onBitmapCallback(Bitmap bitmap);
        void onBitmapFailure();
    }
}
