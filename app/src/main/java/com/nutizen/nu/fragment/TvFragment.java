package com.nutizen.nu.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.MainActivity;
import com.nutizen.nu.bean.response.LiveResponseBean;

public class TvFragment extends BaseLiveFragment {

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        View fullscreenBt = mExoPlayerView.findViewById(R.id.exo_fullscreen);
        fullscreenBt.setVisibility(View.VISIBLE);
        fullscreenBt.setOnClickListener(new FullscreenClickListener());
    }

    @Override
    protected Bitmap setArtwork() {
        return BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.bg_tv_player);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        mPresenter.requestTvs();
    }

    @Override
    public void onItemClickListener(LiveResponseBean liveBean) {
        if (mPresenter != null) {
            initPlayerMessage(liveBean);
            mPresenter.preparePlayer(true);
        }
    }

    class FullscreenClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switchFullScreen();
        }
    }

    public void switchFullScreen() {
        MainActivity mainactivity = (MainActivity) getContext();
        mainactivity.switchLiveFullScreen(!mPresenter.isFullScreen());
        mPresenter.switchPlayerSize(mainactivity, null, !mPresenter.isFullScreen());
    }
}