package com.nutizen.nu.fragment;

import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.BaseLiveListAdapter;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseFragment;
import com.nutizen.nu.presenter.BaseLivePresetner;
import com.nutizen.nu.utils.AnimUtil;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.BaseLiveView;

import java.util.ArrayList;

public abstract class BaseLiveFragment extends BaseFragment<BaseLivePresetner> implements View.OnClickListener, BaseLiveView, BaseLiveListAdapter.ItemOnClickListener {

    private TextView mTitleView;
    private TextView mContentView;
    protected SimpleExoPlayerView mExoPlayerView;
    private RecyclerView mRecyclerView;
    private BaseLiveListAdapter mBaseLiveListAdapter;
    private LiveResponseBean mInitLiveBean;
    private View grayLine, greenLine, mDescView;

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
        mDescView = rootView.findViewById(R.id.iv_open_desc);
        mDescView.setOnClickListener(this);
        mTitleView = rootView.findViewById(R.id.live_title);
        mContentView = rootView.findViewById(R.id.live_content);
        mExoPlayerView = rootView.findViewById(R.id.exo_top_play);
        grayLine = rootView.findViewById(R.id.line);
        greenLine = rootView.findViewById(R.id.line2);
        Bitmap bitmap = setArtwork();
        if (bitmap != null) {
            mExoPlayerView.setDefaultArtwork(bitmap);
        }
        mRecyclerView = rootView.findViewById(R.id.recyclerv_list_lives);
    }

    protected abstract Bitmap setArtwork();

    @Override
    protected void initEvent() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBaseLiveListAdapter = new BaseLiveListAdapter(getContext());
        mBaseLiveListAdapter.setItemOnClickListener(this);
        mRecyclerView.setAdapter(mBaseLiveListAdapter);

        mPresenter.setSimpleExoPlayerView(mExoPlayerView);
    }

    protected void initPlayerMessage(LiveResponseBean liveResponseBean) {
        if (liveResponseBean == null) {
            return;
        }
        if (mInitLiveBean == null || !mInitLiveBean.equals(liveResponseBean)) {
            mInitLiveBean = liveResponseBean;
        }

        mTitleView.setText(mInitLiveBean.getTitle());
        ((TextView) mExoPlayerView.findViewById(R.id.tv_content_title)).setText(mInitLiveBean.getTitle());
        String synopsis = TextUtils.isEmpty(mInitLiveBean.getSynopsis()) ? getString(R.string.there_is_no_description) : mInitLiveBean.getSynopsis();
        mContentView.setText(synopsis);
        mPresenter.setTitleAndUrl(mInitLiveBean.getTitle(), mInitLiveBean.getUrl());
    }

    @Override
    public void refreshData() {
        mPresenter.preparePlayer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_desc:
                AnimUtil.switchDownArrow(v, mContentView);
                break;
        }
    }

    @Override
    public void onLivesResponse(ArrayList<LiveResponseBean> liveResponseBeans) {
        onDataRefreshFinish(true);

        grayLine.setVisibility(View.VISIBLE);
        greenLine.setVisibility(View.VISIBLE);
        mDescView.setVisibility(View.VISIBLE);

        if (liveResponseBeans != null && liveResponseBeans.size() > 0 && mInitLiveBean == null) {
            int random = (int) (Math.random() * liveResponseBeans.size());
            mBaseLiveListAdapter.setCurrentPosition(random);
            LiveResponseBean liveResponseBean = liveResponseBeans.get(random);
            initPlayerMessage(liveResponseBean);//这里presenter设置了url
            mPresenter.preparePlayer();
        }

        mBaseLiveListAdapter.setData(liveResponseBeans);
    }

    @Override
    public void onLivesFailure(String errorMsg) {
        onDataRefreshFinish();
        ToastUtils.showShort(errorMsg);
    }

    @Override
    protected void onViewPagerFragmentResume() {
        super.onViewPagerFragmentResume();
        if (mPresenter != null)
            mPresenter.preparePlayer();
    }

    @Override
    protected void onViewPagerFragmentPause() {
        if (mPresenter != null)
            mPresenter.releasePlayer();
        super.onViewPagerFragmentPause();
    }
}
