package com.nutizen.nu.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.SurfaceView;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.presenter.LiveStreamingActivityPresenter;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.LiveStreamingView;
import com.onwardsmg.rtmppushlib.manager.MeetLibManager;

public class LiveStreamingActivity extends BaseActivity<LiveStreamingActivityPresenter> implements LiveStreamingView, MeetLibManager.MediaEncodeCallback {


    private SurfaceView mSurfaceView;
    private MeetLibManager mMeetLibManager;
    private boolean isRequestingPermission;//防止onresume&onRequestPermissionsResult无限循环请求权限

    @Override
    protected int getLayout() {
        return R.layout.activity_live_streaming;
    }

    @Override
    public int getBarColor() {
        return Constants.NULL_COLOR;
    }

    @Override
    protected LiveStreamingActivityPresenter initPresenter() {
        return new LiveStreamingActivityPresenter(this, this);
    }

    @Override
    protected void initView() {
        mSurfaceView = findViewById(R.id.surfaceview);
    }

    @Override
    protected void initData() {
        mMeetLibManager = MeetLibManager.getMeetLibManagerInstance(this, mSurfaceView, this);
        isRequestingPermission = false;
        createCamera();
    }

    private void createCamera() {
        if (mMeetLibManager != null && !mMeetLibManager.isEncoding()) {
            if (mMeetLibManager.checkMeetPermission(this)) {
                mMeetLibManager.createCamera();
            } else if (Build.VERSION.SDK_INT >= 23) {
                isRequestingPermission = true;
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
            }
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRequestingPermission) {
            createCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMeetLibManager != null) {
            mMeetLibManager.pauseEncode();
            mMeetLibManager.stopCamera();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMeetLibManager != null) {
            mMeetLibManager.destroy();
            mMeetLibManager = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0) {
            boolean grant = true;
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    grant = false;
                }
            }
            if (grant) {
                createCamera();
            } else {
                ToastUtils.showShort("need permission.");
            }
        }
    }

    @Override
    public void onCreateCamera() {

    }

    @Override
    public void onConnectRtmp() {

    }

    @Override
    public void onDisconnectRtmp() {

    }
}
