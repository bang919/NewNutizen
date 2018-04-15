package com.nutizen.nu.fragment;

import com.nutizen.nu.bean.response.LiveResponseBean;

public class TvFragment extends BaseLiveFragment {

    @Override
    protected LiveResponseBean initLiveBean() {
        LiveResponseBean bean = new LiveResponseBean();
        bean.setTitle("Net Tv");
        bean.setSynopsis("Net Tv , an amazing Tv !");
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