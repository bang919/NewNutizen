package com.nutizen.nu.presenter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.BasePresenter;

import java.lang.ref.WeakReference;

import static android.content.Context.POWER_SERVICE;

public class BasePlayerPresenter<V> extends BasePresenter<V> {

    private final String TAG = "BasePlayerPresenter";
    private PowerManager.WakeLock mWakeLock;
    private Context mContext;
    private MyHandler mMyHandler;
    private SimpleExoPlayer mSimpleExoPlayer;
    private ComponentListener mListener;
    private String mUrl;
    private SimpleExoPlayerView mSimpleExoPlayerView;

    public BasePlayerPresenter(Context context, V view) {
        super(context, view);
        mContext = context;
        mMyHandler = new MyHandler(this);
    }

    public void setSimpleExoPlayerView(SimpleExoPlayerView simpleExoPlayerView) {
        mSimpleExoPlayerView = simpleExoPlayerView;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void preparePlayer() {
        preparePlayer(false);
    }

    private void preparePlayer(boolean fromRetry) {
        if (mSimpleExoPlayerView == null || TextUtils.isEmpty(mUrl) || !mSimpleExoPlayerView.getGlobalVisibleRect(new Rect())) {//SimperExoPlayer不可见
            return;
        }
        releasePlayer();
        TrackSelector trackSelector = new DefaultTrackSelector();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, bandwidthMeter, new DefaultHttpDataSourceFactory("Karaoke", bandwidthMeter));
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        mListener = new ComponentListener();
        mSimpleExoPlayer.addListener(mListener);
        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
        MediaSource mediaSource = new HlsMediaSource(Uri.parse(mUrl), mediaDataSourceFactory, null, null);
        mSimpleExoPlayer.prepare(mediaSource, true, false);
        mSimpleExoPlayer.setPlayWhenReady(fromRetry);
        mSimpleExoPlayer.seekTo(0);
        Log.d(TAG, "preparePlayer - " + mUrl);
    }

    public void releasePlayer() {
        unkeepScreen();
        if (mMyHandler != null) {
            mMyHandler.removeMessages(MyHandler.RETRY_PLAYER);
        }
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.removeListener(mListener);
            mSimpleExoPlayer.release();
        }
    }

    private void keepScreen(Context context) {
        if (mWakeLock == null) {
            PowerManager pManager = ((PowerManager) context.getSystemService(POWER_SERVICE));
            mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "cn");
            mWakeLock.acquire();
        }
    }

    private void unkeepScreen() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


    /**
     * -------------------------  ExoPlayer Callback ------------------------
     */
    private final class ComponentListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playWhenReady) {
                keepScreen(mContext);
            } else {
                unkeepScreen();
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            releasePlayer();
            mMyHandler.sendEmptyMessageDelayed(MyHandler.RETRY_PLAYER, 3000);
        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    }

    static class MyHandler extends Handler {

        static final int RETRY_PLAYER = 0;
        WeakReference<BasePlayerPresenter> mWeakReference;

        public MyHandler(BasePlayerPresenter basePlayerPresenter) {
            mWeakReference = new WeakReference<>(basePlayerPresenter);
        }

        @Override
        public void handleMessage(Message msg) {
            BasePlayerPresenter basePlayerPresenter = mWeakReference.get();
            if (basePlayerPresenter == null) {
                return;
            }
            switch (msg.what) {
                case RETRY_PLAYER:
                    basePlayerPresenter.preparePlayer(true);
                    break;
            }
        }
    }

    private boolean isFullScreen;

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void switchPlayerSize(BaseActivity baseActivity, View topBarView) {
        if (isFullScreen) {//变成小窗口
            isFullScreen = false;
            topBarView.setVisibility(View.VISIBLE);
            baseActivity.setSystemBarTransparent();
            baseActivity.setSystemBarColor(baseActivity.getBarColor());
            baseActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mSimpleExoPlayerView.getLayoutParams();
            layoutParams.dimensionRatio = "16:9";
            layoutParams.height = 0;
            mSimpleExoPlayerView.setLayoutParams(layoutParams);
            mSimpleExoPlayerView.requestLayout();
        } else {//变成全屏
            isFullScreen = true;
            int width = mSimpleExoPlayerView.getWidth();
            topBarView.setVisibility(View.GONE);
            baseActivity.getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
            baseActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//切换成左侧横屏
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mSimpleExoPlayerView.getLayoutParams();
            layoutParams.dimensionRatio = null;
            layoutParams.height = width;
            mSimpleExoPlayerView.setLayoutParams(layoutParams);
            mSimpleExoPlayerView.requestLayout();
        }
    }
}
