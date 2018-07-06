package com.nutizen.nu.activity;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.LiveStreamingActivityPresenter;
import com.nutizen.nu.view.LiveStreamingView;

public class LiveStreamingActivity extends BaseActivity<LiveStreamingActivityPresenter> implements LiveStreamingView {

    @Override
    protected int getLayout() {
        return R.layout.activity_live_streaming;
    }

    @Override
    protected LiveStreamingActivityPresenter initPresenter() {
        return new LiveStreamingActivityPresenter(this, this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
