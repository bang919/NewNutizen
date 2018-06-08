package com.nutizen.nu.activity;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.presenter.SplashPresenter;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.view.SplashView;

import java.io.IOException;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private final long SPLASH_WAIT_DURATION = 2500;

    @Override
    protected void onDestroy() {
        dis();
        super.onDestroy();
    }

    private void dis() {
        mPresenter.cancelAllRequest();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public int getBarColor() {
        return Constants.NULL_COLOR;
    }

    @Override
    protected SplashPresenter initPresenter() {
        return new SplashPresenter(this, this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        ImageView bgIv = findViewById(R.id.iv_bg_splash);
        try {
            mPresenter.handleBackgroundPic(bgIv);
        } catch (IOException e) {
            GlideUtils.loadImage(bgIv, -1, R.mipmap.splash_default, new CenterCrop());
        }
    }

    @Override
    protected void initEvent() {
        mPresenter.checkDynamicLinkAndNotification(this, getIntent(), SPLASH_WAIT_DURATION);
    }


    /**
     * 下载到了新的图片，重新设置Splash图片等数据
     */
    @Override
    public void getNewPic() {
        dis();
        initData();
        initEvent();
    }

    @Override
    public void isLogin(boolean login) {
        if (login) {
            jumpToActivity(MainActivity.class);
        } else {
            jumpToActivity(LoginActivity.class);
        }
        finish();
    }

    @Override
    public void jumpToContentPlayerActivity(ContentResponseBean.SearchBean contentBean) {
        ContentPlayerActivity.startContentPlayActivity(this, contentBean);
    }

    @Override
    public void jumpToLivePlayerActivity(LiveResponseBean liveResponseBean) {
        LivePlayerActivity.startLivePlayActivity(this, liveResponseBean);
    }


}
