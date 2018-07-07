package com.onwardsmg.rtmppushlib.manager;

import android.app.Activity;
import android.view.SurfaceView;

public abstract class MeetLibManager {

    private static MeetLibManager mMeetLibManager;
    private static int defaultWidth = 1280;
    private static int defaultHeight = 720;
    private static int defaultBitRate = 1000000;

    public static MeetLibManager getMeetLibManagerInstance(Activity activity, SurfaceView cameraSurfaceView, MediaEncodeCallback mediaEncodeCallback) {
        return getMeetLibManagerInstance(activity, cameraSurfaceView, defaultWidth, defaultHeight, defaultBitRate, mediaEncodeCallback);
    }

    public static MeetLibManager getMeetLibManagerInstance(Activity activity, SurfaceView cameraSurfaceView, int width, int height, int codecBitRate, MediaEncodeCallback mediaEncodeCallback) {
        if (mMeetLibManager == null) {
            mMeetLibManager = new MeetLibManagerImpl(activity, cameraSurfaceView, width, height, codecBitRate, mediaEncodeCallback);
        }
        return mMeetLibManager;
    }

    public void destroy() {
        mMeetLibManager.pauseEncode();
        mMeetLibManager = null;
    }

    public abstract boolean checkMeetPermission(Activity activity);

    public abstract boolean isEncoding();//是否正在编码

    public abstract void createCamera();//创建Camera

    public abstract void stopCamera();//关闭Camera

    public abstract void startEncode();//开始编码并返回data

    public abstract void pauseEncode();//停止编码

    public abstract void enableAssembleToRtmp(String rtmpUrl);

    public interface MediaEncodeCallback {
        void onCreateCamera();

        void onConnectRtmp();

        void onDisconnectRtmp();
    }
}
