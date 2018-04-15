package com.nutizen.nu.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.TvListAdapter;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseFragment;
import com.nutizen.nu.presenter.TvPresetner;
import com.nutizen.nu.utils.DownArrowAnimUtil;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.TvView;

import java.util.ArrayList;

public class TvFragment extends BaseFragment<TvPresetner> implements View.OnClickListener, TvView, TvListAdapter.ItemOnClickListener {

    private TextView mContentView;
    private SimpleExoPlayerView mExoPlayerView;
    private RecyclerView mTvRecyclerView;
    private TvListAdapter mTvListAdapter;

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
        LiveResponseBean initLive = new LiveResponseBean();
        initLive.setTitle("Net Tv");
        initLive.setSynopsis("Net Tv , an amazing Tv !");
        initLive.setUrl("http://nu.onwardsmg.com/live/ch010.m3u8");

        TextView titleView = rootView.findViewById(R.id.tv_title);
        titleView.setText(initLive.getTitle());
        mContentView.setText(initLive.getSynopsis());
    }

    @Override
    protected void initEvent() {
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
                DownArrowAnimUtil.switchDownArrow(v,mContentView);
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
}
