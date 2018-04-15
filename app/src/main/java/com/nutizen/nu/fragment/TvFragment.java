package com.nutizen.nu.fragment;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.nutizen.nu.adapter.TvListAdapter;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseFragment;
import com.nutizen.nu.presenter.TvPresetner;
import com.nutizen.nu.utils.DownArrowAnimUtil;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.TvView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.nutizen.nu.fragment.TvFragment.MyHandler.RETRY_PLAYER;

public class TvFragment extends BaseFragment<TvPresetner> implements View.OnClickListener, TvView, TvListAdapter.ItemOnClickListener {

    private TextView mContentView;
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private ComponentListener mListener;
    private RecyclerView mTvRecyclerView;
    private TvListAdapter mTvListAdapter;
    private MyHandler mMyHandler;
    private LiveResponseBean mInitLiveBean;

    @Override
    protected int getLayout() {
        return R.layout.fragment_tv;
    }

    @Override
    protected TvPresetner initPresenter() {
        return new TvPresetner(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.iv_open_desc).setOnClickListener(this);
        mContentView = rootView.findViewById(R.id.tv_content);
        mExoPlayerView = rootView.findViewById(R.id.exo_top_play);
        mTvRecyclerView = rootView.findViewById(R.id.recyclerv_list_tvs);

        initTvMessage(rootView);
    }

    private void initTvMessage(View rootView) {
        mInitLiveBean = new LiveResponseBean();
        mInitLiveBean.setTitle("Net Tv");
        mInitLiveBean.setSynopsis("Net Tv , an amazing Tv !");
//        mInitLiveBean.setUrl("http://nu.onwardsmg.com/live/ch010.m3u8");
        mInitLiveBean.setUrl("http://dlhls.cdn.zhanqi.tv/zqlive/49858_wgGj1.m3u8");

        TextView titleView = rootView.findViewById(R.id.tv_title);
        titleView.setText(mInitLiveBean.getTitle());
        mContentView.setText(mInitLiveBean.getSynopsis());
    }

    @Override
    protected void initEvent() {
        mMyHandler = new MyHandler(this);
        mTvRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTvListAdapter = new TvListAdapter(getContext());
        mTvListAdapter.setItemOnClickListener(this);
        mTvRecyclerView.setAdapter(mTvListAdapter);
    }

    @Override
    public void refreshData() {
        mPresenter.requestTvs();
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
    public void onTvsResponse(ArrayList<LiveResponseBean> liveResponseBeans) {
        onDataRefreshFinish(true);
        mTvListAdapter.setData(liveResponseBeans);
    }

    @Override
    public void onTvsFailure(String errorMsg) {
        onDataRefreshFinish(false);
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void onItemClickListener(LiveResponseBean liveBean) {

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

    private void preparePlayer() {
        if (!getUserVisibleHint()) {
            return;
        }
        releasePlayer();
        TrackSelector trackSelector = new DefaultTrackSelector();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), bandwidthMeter, new DefaultHttpDataSourceFactory("Karaoke", bandwidthMeter));
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        mListener = new ComponentListener();
        mSimpleExoPlayer.addListener(mListener);
        mExoPlayerView.setPlayer(mSimpleExoPlayer);
        MediaSource mediaSource = new HlsMediaSource(Uri.parse(mInitLiveBean.getUrl()), mediaDataSourceFactory, null, null);
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
        WeakReference<TvFragment> mWeakReference;

        public MyHandler(TvFragment tvFragment) {
            mWeakReference = new WeakReference<>(tvFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            TvFragment tvFragment = mWeakReference.get();
            if (tvFragment == null) {
                return;
            }
            switch (msg.what) {
                case RETRY_PLAYER:
                    tvFragment.preparePlayer();
                    break;
            }
        }
    }
}
