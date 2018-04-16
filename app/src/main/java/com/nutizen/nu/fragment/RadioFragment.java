package com.nutizen.nu.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.utils.FileUtils;

import java.util.ArrayList;

public class RadioFragment extends BaseLiveFragment {


    @Override
    protected Bitmap setArtwork() {
        return BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.bg_radio_player);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        mPresenter.requestRadios();
    }

    @Override
    public void onLivesResponse(ArrayList<LiveResponseBean> liveResponseBeans) {
        super.onLivesResponse(liveResponseBeans);
        if (liveResponseBeans != null && liveResponseBeans.size() > 0) {
            int random = (int) (Math.random() * liveResponseBeans.size());
            LiveResponseBean liveResponseBean = liveResponseBeans.get(random);
            initPlayerMessage(liveResponseBean);
            preparePlayer();
            Glide.with(getContext()).load(liveResponseBean.getThumbnail()).apply(new RequestOptions().transforms(new CenterCrop()))
                    .transition(new DrawableTransitionOptions().crossFade()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    mExoPlayerView.setDefaultArtwork(FileUtils.drawable2Bitmap(resource));
                }
            });
        }
    }

    @Override
    public void onItemClickListener(LiveResponseBean liveBean) {

    }
}
