package com.nutizen.nu.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.presenter.SplashPresenter;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.view.SplashView;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private MyHandler mHandler;
    private final int SPLASH_WAIT_DURATION = 2500;
    private static final int JUMP_LOGIN_ACT = 0;
    private static final int JUMP_MAIN_ACT = 1;
    private int mTargetAct;

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(mTargetAct);
        super.onDestroy();
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

        mHandler = new MyHandler(this);
        mTargetAct = JUMP_LOGIN_ACT;
        if (checkLogin()) {
            mTargetAct = JUMP_MAIN_ACT;
        }
    }

    @Override
    protected void initEvent() {
        mHandler.sendEmptyMessageDelayed(mTargetAct, SPLASH_WAIT_DURATION);
    }

    private boolean checkLogin() {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        return accountMessage != null;
    }

    /**
     * 下载到了新的图片，重新设置Splash图片等数据
     */
    @Override
    public void getNewPic() {
        mHandler.removeMessages(mTargetAct);
        initData();
        initEvent();
    }

    static class MyHandler extends Handler {
        WeakReference<SplashActivity> actHolder;

        public MyHandler(SplashActivity activity) {
            this.actHolder = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity splashActivity = actHolder.get();
            if (splashActivity != null) {
                switch (msg.what) {
                    case JUMP_LOGIN_ACT:
                        splashActivity.jumpToActivity(LoginActivity.class);
                        break;
                    case JUMP_MAIN_ACT:
                        splashActivity.jumpToActivity(MainActivity.class);
                        break;
                }
                splashActivity.finish();
            }
        }
    }

}
