package com.onwardsmg.rtmppushlib.presenter;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.onwardsmg.rtmppushlib.bean.RingBuffer;
import com.onwardsmg.rtmppushlib.manager.MeetLibManagerImpl;
import com.onwardsmg.rtmppushlib.util.CameraUtil;

import java.util.List;

public class CameraPresenter {

    private String TAG = "CameraPresenter";
    //    private static final int UseCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private static final int UseCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Camera mCamera = null;
    private RingBuffer mRingBuffer;
    private int mDisplayOrientation;
    private int mFormat;

    public CameraPresenter() {
        mRingBuffer = new RingBuffer();
    }

    public void createCamera(final Activity activity, final SurfaceView surfaceView, final CameraPresenterCallback previewCallback) {
        mDisplayOrientation = CameraUtil.getDisplayOrientation(activity);
        if (mCamera != null) {
            releaseCamera();
        }
        surfaceView.setZOrderMediaOverlay(true);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startCamera(surfaceView);
                previewCallback.onCameraCreate(MeetLibManagerImpl.WIDTH, MeetLibManagerImpl.HEIGHT, mRingBuffer);//需要先prepareCamera获取mFormat
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                    refreshCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                holder.removeCallback(this);
                releaseCamera();
            }
        });
        if (holder.getSurface().isValid()) {
            startCamera(surfaceView);
            previewCallback.onCameraCreate(MeetLibManagerImpl.WIDTH, MeetLibManagerImpl.HEIGHT, mRingBuffer);//需要先prepareCamera获取mFormat
        }
    }

    public int getFormat() {
        return mFormat;
    }

    private void startCamera(final SurfaceView surfaceView) {

        if (mCamera != null || mRingBuffer == null) {
            return;
        }

        if (!(UseCamera == Camera.CameraInfo.CAMERA_FACING_FRONT || UseCamera == Camera.CameraInfo.CAMERA_FACING_BACK)) {
            return;
        }

        int facing = UseCamera;

        try {
            int cameraId = -1;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == facing) {
                    cameraId = i;
                    break;
                }
            }

            if (cameraId == -1) {
                return;
            }

            mCamera = Camera.open(cameraId);
            mFormat = CameraUtil.getSupportedPreviewFormats(mCamera);
            Camera.Parameters params = mCamera.getParameters();
            choosePreviewSize(params);
            params.setPreviewSize(MeetLibManagerImpl.WIDTH, MeetLibManagerImpl.HEIGHT);
            params.setPreviewFormat(mFormat);
            //params.setPreviewFpsRange(chosenFps[0], chosenFps[1]);
            mCamera.setParameters(params);

            int bufferSize = (MeetLibManagerImpl.WIDTH * MeetLibManagerImpl.HEIGHT * ImageFormat.getBitsPerPixel(params.getPreviewFormat())) / 8;
            mCamera.addCallbackBuffer(new byte[bufferSize]);
            mCamera.addCallbackBuffer(new byte[bufferSize]);
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                public void onPreviewFrame(byte[] data, Camera camera) {
                    mRingBuffer.set(data);
                }
            });

            mRingBuffer.setCallback(new RingBuffer.Callback() {
                @Override
                public void onBufferRelease(byte[] buffer) {
                    if (mCamera != null) {
                        mCamera.addCallbackBuffer(buffer);
                    }
                }
            });

            mCamera.setPreviewDisplay(surfaceView.getHolder());
            mCamera.startPreview();
            refreshCamera();

        } catch (Exception e) {
            Log.e(TAG, "exception", e);
        }
    }

    public int getOrientation() {
        return mDisplayOrientation;
    }

    private void refreshCamera() {
        if (mCamera != null) {
            mCamera.setDisplayOrientation(mDisplayOrientation);
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void choosePreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size size : supportedPreviewSizes) {
            if (size.width > MeetLibManagerImpl.WIDTH && size.height > MeetLibManagerImpl.HEIGHT && size.width <= 1280) {
                MeetLibManagerImpl.WIDTH = size.width;
                MeetLibManagerImpl.HEIGHT = size.height;
            }
        }
        Log.d(TAG, "choosePreviewSize -- " + MeetLibManagerImpl.WIDTH + " x " + MeetLibManagerImpl.HEIGHT);
    }

    public interface CameraPresenterCallback {
        void onCameraCreate(int width, int height, RingBuffer ringBuffer);
    }
}
