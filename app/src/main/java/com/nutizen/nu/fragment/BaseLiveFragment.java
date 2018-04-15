package com.nutizen.nu.fragment;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.BaseLiveListAdapter;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseFragment;
import com.nutizen.nu.presenter.BaseLivePresetner;
import com.nutizen.nu.utils.DownArrowAnimUtil;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.BaseLiveView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.nutizen.nu.fragment.BaseLiveFragment.MyHandler.RETRY_PLAYER;

public abstract class BaseLiveFragment extends BaseFragment<BaseLivePresetner> implements View.OnClickListener, BaseLiveView, BaseLiveListAdapter.ItemOnClickListener {

    private TextView mTitleView;
    private TextView mContentView;
    protected SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private BaseLiveFragment.ComponentListener mListener;
    private RecyclerView mRecyclerView;
    private BaseLiveListAdapter mBaseLiveListAdapter;
    private BaseLiveFragment.MyHandler mMyHandler;
    private LiveResponseBean mInitLiveBean;

    @Override
    protected int getLayout() {
        return R.layout.fragment_base_live;
    }


    @Override
    protected BaseLivePresetner initPresenter() {
        return new BaseLivePresetner(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.iv_open_desc).setOnClickListener(this);
        mTitleView = rootView.findViewById(R.id.live_title);
        mContentView = rootView.findViewById(R.id.live_content);
        mExoPlayerView = rootView.findViewById(R.id.exo_top_play);
        mRecyclerView = rootView.findViewById(R.id.recyclerv_list_lives);
    }

    @Override
    protected void initEvent() {
        mMyHandler = new BaseLiveFragment.MyHandler(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBaseLiveListAdapter = new BaseLiveListAdapter(getContext());
        mBaseLiveListAdapter.setItemOnClickListener(this);
        mRecyclerView.setAdapter(mBaseLiveListAdapter);

        mInitLiveBean = initLiveBean();
        initPlayerMessage(mInitLiveBean);
    }

    protected void initPlayerMessage(LiveResponseBean liveResponseBean) {
        if (liveResponseBean == null) {
            return;
        }
        if (mInitLiveBean == null || !mInitLiveBean.equals(liveResponseBean)) {
            mInitLiveBean = liveResponseBean;
        }

        mTitleView.setText(mInitLiveBean.getTitle());
        String synopsis = TextUtils.isEmpty(mInitLiveBean.getSynopsis()) ? getString(R.string.there_is_no_description) : mInitLiveBean.getSynopsis();
        mContentView.setText(synopsis);
    }

    protected LiveResponseBean initLiveBean() {
        return null;
    }

    @Override
    public void refreshData() {
        preparePlayer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_desc:
                DownArrowAnimUtil.switchDownArrow(v, mContentView);
                break;
        }
    }

    @Override
    public void onLivesResponse(ArrayList<LiveResponseBean> liveResponseBeans) {
        onDataRefreshFinish(true);
        mBaseLiveListAdapter.setData(liveResponseBeans);
    }

    @Override
    public void onLivesFailure(String errorMsg) {
        onDataRefreshFinish(false);
        ToastUtils.showShort(errorMsg);
    }

    @Override
    protected void onViewPagerFragmentResume() {
        super.onViewPagerFragmentResume();
        preparePlayer();
    }

    @Override
    protected void onViewPagerFragmentPause() {
        releasePlayer();
        super.onViewPagerFragmentPause();
    }

    protected void preparePlayer() {
        String url = mInitLiveBean == null ? null : mInitLiveBean.getUrl();
        if (!getUserVisibleHint() || TextUtils.isEmpty(url)) {
            return;
        }
        releasePlayer();
        TrackSelector trackSelector = new DefaultTrackSelector();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), bandwidthMeter, new DefaultHttpDataSourceFactory("Karaoke", bandwidthMeter));
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        mListener = new BaseLiveFragment.ComponentListener();
        mSimpleExoPlayer.addListener(mListener);
        mExoPlayerView.setPlayer(mSimpleExoPlayer);
        MediaSource mediaSource = new HlsMediaSource(Uri.parse(url), mediaDataSourceFactory, null, null);
        mSimpleExoPlayer.prepare(mediaSource, true, false);
        mSimpleExoPlayer.setPlayWhenReady(false);
        mSimpleExoPlayer.seekTo(0);
    }

    private void releasePlayer() {
        if (mPresenter != null) {
            mPresenter.unkeepScreen();
        }
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.removeListener(mListener);
            mSimpleExoPlayer.release();
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
                mPresenter.keepScreen(getContext());
            } else {
                mPresenter.unkeepScreen();
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            mMyHandler.sendEmptyMessageDelayed(RETRY_PLAYER, 3000);
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
        WeakReference<BaseLiveFragment> mWeakReference;

        public MyHandler(BaseLiveFragment BaseLiveFragment) {
            mWeakReference = new WeakReference<>(BaseLiveFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseLiveFragment BaseLiveFragment = mWeakReference.get();
            if (BaseLiveFragment == null) {
                return;
            }
            switch (msg.what) {
                case RETRY_PLAYER:
                    BaseLiveFragment.preparePlayer();
                    break;
            }
        }
    }
}
