package com.nutizen.nu.utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
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
        if (imgSource != 0) {
            RequestBuilder<Drawable> requestBuilder = Glide.with(imageView.getContext()).load(imgSource);
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
    }

    public static void loadImage(ImageView imageView, int imgDefaultSource, String imgUrl, BitmapTransformation... transformation) {
        if (!TextUtils.isEmpty(imgUrl)) {
            RequestBuilder<Drawable> requestBuilder = Glide.with(imageView.getContext()).load(imgUrl);
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
    }
}
