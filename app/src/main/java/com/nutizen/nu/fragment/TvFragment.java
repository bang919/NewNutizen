package com.nutizen.nu.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LiveResponseBean;

public class TvFragment extends BaseLiveFragment {

    @Override
    protected Bitmap setArtwork() {
        return BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.bg_tv_player);
    }

    @Override
    protected LiveResponseBean initLiveBean() {
        LiveResponseBean bean = new LiveResponseBean();
        bean.setTitle("Net Tv");
        bean.setSynopsis("Net Tv !");
        bean.setUrl("http://nu.onwardsmg.com/live/ch010.m3u8");
//        bean.setUrl("http://dlhls.cdn.zhanqi.tv/zqlive/49858_wgGj1.m3u8");
        return bean;
    }

    @Override
    public void refreshData() {
        super.refreshData();
        mPresenter.requestTvs();
    }

    @Override
    public void onItemClickListener(LiveResponseBean liveBean) {

    }
}