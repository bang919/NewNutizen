package com.nutizen.nu.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.request.ArchiveBody;
import com.nutizen.nu.bean.response.LiveStreamingResult;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.dialog.NormalDialog;
import com.nutizen.nu.fragment.LiveStreamingEditPicFragment;
import com.nutizen.nu.presenter.LiveStreamingActivityPresenter;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.LiveStreamingView;

import java.io.File;
import java.lang.ref.WeakReference;

import static com.nutizen.nu.activity.LiveStreamingActivity.LiveStreamingHandler.INIT_MEETLIB_MANAGER;
import static com.nutizen.nu.activity.LiveStreamingActivity.LiveStreamingHandler.LIVE_DONE;
import static com.nutizen.nu.activity.LiveStreamingActivity.LiveStreamingHandler.LIVE_START;
import static com.nutizen.nu.activity.LiveStreamingActivity.LiveStreamingHandler.WAITING_LIVE_BUILD;
import static com.nutizen.nu.presenter.PhotoPresenter.REQUEST_PERMISSION_ALBUM;
import static com.nutizen.nu.presenter.PhotoPresenter.REQUEST_PERMISSION_CAMERA;

public class LiveStreamingActivity extends BaseActivity<LiveStreamingActivityPresenter> implements LiveStreamingView, View.OnClickListener, LiveStreamingEditPicFragment.LiveStreamingEditPicCallback, View.OnLongClickListener {

    private LiveStreamingHandler mHandler;
    //    private MeetLibManager mMeetLibManager;
    private boolean isRequestingPermission;//防止onresume&onRequestPermissionsResult无限循环请求权限
    private final int CREATE_CAMERA_REQUEST_CODE = 1;
    private SurfaceView mSurfaceView;
    private ImageView mPicIv;
    private View mArchiveBtn;
    private View mAudioOnlyBtn;
    private EditText mStreamingTitleEt;
    private View mSwitchCameraLayout, mInitMeetLayout, mWaitingLiveLayout, mLivingLayout, mDoneLayout;
    private TextView mWatchCountTv;
    private Chronometer mChronometer;
    private ArchiveBody mArchiveBody;
    private LiveStreamingResult mLiveStreamingResult;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏，去掉信息栏
        mSwitchCameraLayout = findViewById(R.id.switch_camera_layout);
        mInitMeetLayout = findViewById(R.id.prepare_bottom_layout);
        mWaitingLiveLayout = findViewById(R.id.waiting_live_layout);
        mLivingLayout = findViewById(R.id.living_layout);
        mDoneLayout = findViewById(R.id.done_layout);
        mSurfaceView = findViewById(R.id.surfaceview);
        mPicIv = findViewById(R.id.iv_streaming_pic);
        mArchiveBtn = findViewById(R.id.iv_archive);
        mAudioOnlyBtn = findViewById(R.id.iv_audioonly);
        mStreamingTitleEt = findViewById(R.id.et_streaming_title);
        mWatchCountTv = findViewById(R.id.tv_count);
        mChronometer = findViewById(R.id.chro_jishi);


        mPicIv.setOnClickListener(this);
        mArchiveBtn.setOnClickListener(this);
        mAudioOnlyBtn.setOnClickListener(this);
        findViewById(R.id.iv_streaming_switch).setOnClickListener(this);
        findViewById(R.id.btn_start_livestreaming).setOnClickListener(this);
        findViewById(R.id.waiting_live_cancel_button).setOnClickListener(this);
        findViewById(R.id.iv_streaming_stop).setOnClickListener(this);
        findViewById(R.id.iv_streaming_stop).setOnLongClickListener(this);
        findViewById(R.id.btn_done_close).setOnClickListener(this);

        initMeetMessage();
    }

    public void initMeetMessage() {
        mSwitchCameraLayout.setVisibility(View.VISIBLE);
        mInitMeetLayout.setVisibility(View.VISIBLE);
        mWaitingLiveLayout.setVisibility(View.GONE);
        mLivingLayout.setVisibility(View.GONE);
        mDoneLayout.setVisibility(View.GONE);
    }

    public void waitingLiveBuild() {
        mSwitchCameraLayout.setVisibility(View.GONE);
        mInitMeetLayout.setVisibility(View.GONE);
        mWaitingLiveLayout.setVisibility(View.VISIBLE);
        mLivingLayout.setVisibility(View.GONE);
        mDoneLayout.setVisibility(View.GONE);
    }

    public void livingStreaming() {
        mSwitchCameraLayout.setVisibility(View.VISIBLE);
        mInitMeetLayout.setVisibility(View.GONE);
        mWaitingLiveLayout.setVisibility(View.GONE);
        mLivingLayout.setVisibility(View.VISIBLE);
        mDoneLayout.setVisibility(View.GONE);
    }

    public void doneLiveStreaming() {
        mPresenter.cancelAllRequest();
//        mMeetLibManager.stopCamera();
        mSwitchCameraLayout.setVisibility(View.GONE);
        mInitMeetLayout.setVisibility(View.GONE);
        mWaitingLiveLayout.setVisibility(View.GONE);
        mLivingLayout.setVisibility(View.GONE);
        mDoneLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        mHandler = new LiveStreamingHandler(this);
    }

    public void buildMeetLibManager() {
        /*if (mMeetLibManager == null) {
            mMeetLibManager = MeetLibManager.getMeetLibManagerInstance(LiveStreamingActivity.this,
                    mSurfaceView, LiveStreamingActivity.this);
        }*/
        createCamera();
    }

    private void createCamera() {
        isRequestingPermission = false;
        /*if (mMeetLibManager != null && !mMeetLibManager.isEncoding()) {
            if (mMeetLibManager.checkMeetPermission(this)) {
                mMeetLibManager.createCamera();
            } else if (Build.VERSION.SDK_INT >= 23) {
                isRequestingPermission = true;
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, CREATE_CAMERA_REQUEST_CODE);
            }
        }*/
    }

    @Override
    protected void initEvent() {
        mHandler.sendEmptyMessageDelayed(INIT_MEETLIB_MANAGER, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRequestingPermission) {
            if (mLiveStreamingResult != null) {
//                mMeetLibManager.enableAssembleToRtmp(mLiveStreamingResult.getUrl());
//                mMeetLibManager.startEncode();
            } else {
                createCamera();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mMeetLibManager != null) {
//            mMeetLibManager.pauseEncode();
//            mMeetLibManager.stopCamera();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mMeetLibManager != null) {
//            mMeetLibManager.destroy();
//            mMeetLibManager = null;
//        }
        if (mPresenter != null) {
            mPresenter.finishLive(mLiveStreamingResult);
            mPresenter.deletePhotoFile();
        }
        if (mHandler != null) {
            mHandler.removeMessages(INIT_MEETLIB_MANAGER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
            case REQUEST_PERMISSION_ALBUM:
                mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            case CREATE_CAMERA_REQUEST_CODE:
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
                break;
        }

    }

    /*@Override
    public void onConnectRtmp() {
        mPresenter.startArchive(mArchiveBody);
    }

    @Override
    public void onDisconnectRtmp() {
        mPresenter.finishArchive(mArchiveBody);
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_streaming_switch:
//                mMeetLibManager.switchCamera();
                break;
            case R.id.iv_streaming_pic:
                LiveStreamingEditPicFragment liveStreamingEditPicFragment = LiveStreamingEditPicFragment.getInstance();
                liveStreamingEditPicFragment.setLiveStreamingEditPicCallback(this);
                liveStreamingEditPicFragment.show(getSupportFragmentManager(), LiveStreamingEditPicFragment.TAG);
                break;
            case R.id.btn_start_livestreaming:
                new NormalDialog(this, getString(R.string.yes), getString(R.string.no), getString(R.string.ready_to_start_live),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mPresenter.startLive(mStreamingTitleEt.getText().toString(), mArchiveBtn.isSelected(), mAudioOnlyBtn.isSelected())) {
                                    mHandler.sendEmptyMessage(WAITING_LIVE_BUILD);
                                } else {
                                    mHandler.sendEmptyMessage(INIT_MEETLIB_MANAGER);
                                }
                            }
                        }, null).show();
                break;
            case R.id.iv_archive:
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ToastUtils.showShort(R.string.live_archive);
                } else {
                    ToastUtils.showShort(R.string.live_unarchive);
                }
                break;
            case R.id.iv_audioonly:
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ToastUtils.showShort(R.string.open_record_only);
                } else {
                    ToastUtils.showShort(R.string.close_record_only);
                }
                break;
            case R.id.waiting_live_cancel_button:
                mPresenter.cancelAllRequest();
                mHandler.sendEmptyMessage(INIT_MEETLIB_MANAGER);
                break;
            case R.id.iv_streaming_stop:
                ToastUtils.showShort(R.string.please_hold_to_end_live);
                break;
            case R.id.btn_done_close:
                finish();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_streaming_stop:
                mHandler.sendEmptyMessage(LIVE_DONE);
                return true;
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean isLiving = mDoneLayout.getVisibility() == View.VISIBLE;
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.isLongPress() && isLiving) {
            mHandler.sendEmptyMessage(LIVE_DONE);
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && !event.isLongPress() && isLiving) {
            ToastUtils.showShort(R.string.long_press_exit);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void chooseAFunctionToDo(int requestPermissionCode) {
        mPresenter.requestPermissionTodo(requestPermissionCode);
    }

    @Override
    public void onError(String error) {
        ToastUtils.showShort(error);
        mHandler.sendEmptyMessage(INIT_MEETLIB_MANAGER);
    }

    @Override
    public void onLuBanSuccess(File picFile) {
        GlideUtils.loadImage(mPicIv, R.mipmap.portrait, picFile.getPath(), new CenterCrop());
    }

    @Override
    public void onLiveStreamCreate(LiveStreamingResult liveStreamingResult, String liveTitle, boolean archive, boolean audioOnly) {
        if (archive) {
            mArchiveBody = new ArchiveBody(liveStreamingResult.getUuid(), liveTitle);
        }
        mLiveStreamingResult = liveStreamingResult;
//        mMeetLibManager.enableAssembleToRtmp(liveStreamingResult.getUrl());
//        mMeetLibManager.setAudioOnly(audioOnly);
//        mMeetLibManager.startEncode();
        mHandler.sendEmptyMessage(LIVE_START);
    }


    public static class LiveStreamingHandler extends Handler {

        public static final int INIT_MEETLIB_MANAGER = 0x01;
        public static final int WAITING_LIVE_BUILD = 0x02;
        public static final int LIVE_START = 0x03;
        public static final int LIVE_DONE = 0x04;
        private WeakReference<LiveStreamingActivity> mLiveStreamingActivityWeakReference;

        public LiveStreamingHandler(LiveStreamingActivity liveStreamingActivity) {
            mLiveStreamingActivityWeakReference = new WeakReference<>(liveStreamingActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            LiveStreamingActivity liveStreamingActivity = mLiveStreamingActivityWeakReference.get();
            switch (msg.what) {
                case INIT_MEETLIB_MANAGER:
                    if (liveStreamingActivity != null) {
                        liveStreamingActivity.initMeetMessage();
                        liveStreamingActivity.buildMeetLibManager();
                    }
                    break;
                case WAITING_LIVE_BUILD:
                    if (liveStreamingActivity != null) {
                        liveStreamingActivity.waitingLiveBuild();
                    }
                    break;
                case LIVE_START:
                    if (liveStreamingActivity != null) {
                        liveStreamingActivity.livingStreaming();
                    }
                    break;
                case LIVE_DONE:
                    if (liveStreamingActivity != null) {
                        liveStreamingActivity.doneLiveStreaming();
                    }
                    break;
            }
        }
    }
}
