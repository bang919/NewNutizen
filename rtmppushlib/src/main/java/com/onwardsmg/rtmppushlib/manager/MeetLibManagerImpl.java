package com.onwardsmg.rtmppushlib.manager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;

import com.onwardsmg.rtmppushlib.bean.RingBuffer;
import com.onwardsmg.rtmppushlib.presenter.AudioEncodePresenter;
import com.onwardsmg.rtmppushlib.presenter.CameraPresenter;
import com.onwardsmg.rtmppushlib.presenter.RtmpAssemblePresenter;
import com.onwardsmg.rtmppushlib.presenter.VideoEncodePresenter;


public class MeetLibManagerImpl extends MeetLibManager implements VideoEncodePresenter.VideoEncodeCallback, AudioEncodePresenter.AudioEncodeCallback {

    private Activity mActivity;
    public static int WIDTH, HEIGHT, CODEC_BIT_RATE;
    private SurfaceView mCameraSurfaceView;
    private CameraPresenter mCameraPresenter;
    private VideoEncodePresenter mVideoEncodePresenter;
    private AudioEncodePresenter mAudioEncodePresenter;
    private RingBuffer mRingBuffer;
    private MediaEncodeCallback mMediaEncodeCallback;
    private boolean isCameraCreate, isEncodeStart;

    //Rtmp
    private boolean enableAssembleRtmp;
    private String rtmpUrl;
    private RtmpAssemblePresenter mRtmpAssemblePresenter;

    MeetLibManagerImpl(Activity activity, SurfaceView cameraSurfaceView, int width, int height, int codecBitRate, MediaEncodeCallback mediaEncodeCallback) {
        mActivity = activity;
        mCameraSurfaceView = cameraSurfaceView;
        WIDTH = width;
        HEIGHT = height;
        CODEC_BIT_RATE = codecBitRate;
        mMediaEncodeCallback = mediaEncodeCallback;
        mCameraPresenter = new CameraPresenter();
    }

    @Override
    public boolean isEncoding() {
        return isEncodeStart;
    }

    @Override
    public void createCamera() {
        if (!isCameraCreate && checkMeetPermission(mActivity)) {
            mCameraPresenter.createCamera(mActivity, mCameraSurfaceView, new CameraPresenter.CameraPresenterCallback() {
                @Override
                public void onCameraCreate(int width, int height, RingBuffer ringBuffer) {
                    isCameraCreate = true;
                    mRingBuffer = ringBuffer;
                    checkSurfaceToStartEncode();

                    if (enableAssembleRtmp) {
                        enableAssembleToRtmp(rtmpUrl);
                    }
                }
            });
        }
    }

    @Override
    public boolean checkMeetPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            int checkCallPhonePermission2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED || checkCallPhonePermission2 != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void stopCamera() {
        if (mRtmpAssemblePresenter != null) {
            mRtmpAssemblePresenter.release();
            mRtmpAssemblePresenter = null;
        }
        pauseEncode();
        if (mCameraPresenter != null) {
            mCameraPresenter.releaseCamera();
        }
        isCameraCreate = false;
    }

    @Override
    public void startEncode() {
        if (!isEncodeStart) {
            if (isCameraCreate) {
                stopCamera();
            }
            createCamera();
            isEncodeStart = true;
            checkSurfaceToStartEncode();
        }
    }

    private void checkSurfaceToStartEncode() {
        if (isEncodeStart && isCameraCreate && mCameraSurfaceView.getHolder().getSurface().isValid() && mRingBuffer != null) {
            mVideoEncodePresenter = new VideoEncodePresenter(WIDTH, HEIGHT, mCameraPresenter.getFormat(), mCameraPresenter.getOrientation(), mRingBuffer);
            mVideoEncodePresenter.setVideoEncodeCallback(this);
            mVideoEncodePresenter.start();
            mAudioEncodePresenter = new AudioEncodePresenter();
            mAudioEncodePresenter.setAudioEncodeCallback(this);
            mAudioEncodePresenter.start();
        }
    }

    @Override
    public void pauseEncode() {
        isEncodeStart = false;
        if (mVideoEncodePresenter != null) {
            mVideoEncodePresenter.release();
            mVideoEncodePresenter = null;
        }
        if (mAudioEncodePresenter != null) {
            mAudioEncodePresenter.release();
            mAudioEncodePresenter = null;
        }
    }

    @Override
    public void enableAssembleToRtmp(String rtmpUrl) {
        if (mRtmpAssemblePresenter == null) {
            this.rtmpUrl = rtmpUrl;
            enableAssembleRtmp = true;
            mRtmpAssemblePresenter = new RtmpAssemblePresenter(rtmpUrl);
        }
    }

    @Override
    public void onVideoEncodePresenterOutput(byte[] avcBuf, int timestamp) {
        if (enableAssembleRtmp) {
            mRtmpAssemblePresenter.setRtmpVideoDatas(avcBuf, timestamp);
        }
    }

    @Override
    public void onAudioEncodePresenterOutput(byte[] avcBuf, int timestamp) {
        if (enableAssembleRtmp) {
            mRtmpAssemblePresenter.setRtmpAudioDatas(avcBuf, timestamp);
        }
    }

}
