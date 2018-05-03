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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.LogUtils;

import java.lang.ref.WeakReference;

import static android.content.Context.POWER_SERVICE;

public class BasePlayerPresenter<V> extends BasePresenter<V> {

    private final String TAG = "BasePlayerPresenter";
    private PowerManager.WakeLock mWakeLock;
    private Context mContext;
    private MyHandler mMyHandler;
    private SimpleExoPlayer mSimpleExoPlayer;
    private ComponentListener mListener;
    private String mTitle;
    private String mUrl;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private TextView mTitleTextView;

    public BasePlayerPresenter(Context context, V view) {
        super(context, view);
        mContext = context;
        mMyHandler = new MyHandler(this);
    }

    public void setSimpleExoPlayerView(SimpleExoPlayerView simpleExoPlayerView) {
        mSimpleExoPlayerView = simpleExoPlayerView;
        mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);//满屏
        mTitleTextView = mSimpleExoPlayerView.findViewById(R.id.tv_content_title);
        mSimpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                View profileSettingView = mSimpleExoPlayerView.findViewById(R.id.rv_profile_settings);
                if (profileSettingView != null) {
                    profileSettingView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setTitleAndUrl(String title, String url) {
        this.mTitle = title;
        this.mUrl = url;
    }

    public void preparePlayer() {
        preparePlayer(false);
    }

    public void preparePlayer(boolean playWhenReady) {
        preparePlayer(playWhenReady, true);
    }

    public void preparePlayer(boolean playWhenReady, boolean continuePlay) {
        if (mSimpleExoPlayerView == null || TextUtils.isEmpty(mUrl) || !mSimpleExoPlayerView.getGlobalVisibleRect(new Rect())) {//SimperExoPlayer不可见
            return;
        }
        long lastPosition = -1;
        if (continuePlay && mSimpleExoPlayer != null) {
            lastPosition = mSimpleExoPlayer.getCurrentPosition();
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
        mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
        mSimpleExoPlayer.seekTo(continuePlay && lastPosition != -1 ? lastPosition : 0);
        LogUtils.d(TAG, "preparePlayer - " + mUrl);
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

    public void setPlayWhenReady(boolean playWhenReady) {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
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
            if (playWhenReady && playbackState != Player.STATE_ENDED) {
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

    public void switchPlayerSize(BaseActivity baseActivity, View topBarView, boolean fullScreen) {
        if (!fullScreen) {//变成小窗口
            isFullScreen = false;
            if (mTitleTextView != null) {
                mTitleTextView.setVisibility(View.GONE);
            }
            if (topBarView != null)
                topBarView.setVisibility(View.VISIBLE);
            baseActivity.setSystemBarTransparent();
            baseActivity.setSystemBarColor(baseActivity.getBarColor());
            baseActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            baseActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mSimpleExoPlayerView.getLayoutParams();
            layoutParams.dimensionRatio = "16:9";
            layoutParams.height = 0;
            mSimpleExoPlayerView.setLayoutParams(layoutParams);
            mSimpleExoPlayerView.requestLayout();
        } else {//变成全屏
            isFullScreen = true;
            int width = mSimpleExoPlayerView.getWidth();
            if (mTitleTextView != null) {
                mTitleTextView.setText(mTitle);
                mTitleTextView.setVisibility(View.VISIBLE);
            }
            if (topBarView != null)
                topBarView.setVisibility(View.GONE);
            baseActivity.getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);//去掉DecorView
            baseActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//切换成左侧横屏

            baseActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mSimpleExoPlayerView.getLayoutParams();
            layoutParams.dimensionRatio = null;
            layoutParams.height = width;
            mSimpleExoPlayerView.setLayoutParams(layoutParams);
            mSimpleExoPlayerView.requestLayout();
        }
    }
}
