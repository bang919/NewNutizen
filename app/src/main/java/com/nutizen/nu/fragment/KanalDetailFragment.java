package com.nutizen.nu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.KanalDetailPagerAdapter;
import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.KanalDetailPresenter;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.KanalDetailView;
import com.nutizen.nu.widget.DetailKanalSwipeRefreshLayout;

import java.util.ArrayList;

public class KanalDetailFragment extends TransNutizenTitleFragment<KanalDetailPresenter> implements KanalDetailView {

    public static final String TAG = "KanalDetailFragment";
    public static final String KANAL_BEAN_DETAIL = "kanal_bean_detail";

    private KanalRspBean.SearchBean mKanalBean;
    private DetailKanalSwipeRefreshLayout mDetailKanalSwipeRefreshLayout;
    private View mRootView;
    private ImageView mFollowBt;
    private View mProgressBarView;
    private TabLayout mTabLayout;
    private ViewPager mDataViewpager;
    private KanalDetailPagerAdapter mKanalDetailPagerAdapter;

    private boolean initFollow;
    private Runnable followRunnable;
    private Handler mHandler;

    public static KanalDetailFragment getInstance(boolean isFromMainAct, Bundle bundle) {
        return getInstance(KanalDetailFragment.class, isFromMainAct, bundle);
    }

    @Override
    public void onPause() {
        checkFollowRequest(true);
        super.onPause();
    }

    @Override
    protected int getBodyLayout() {
        return R.layout.fragment_kanal_detail;
    }

    @Override
    protected void initBodyView(View rootView) {
        mRootView = rootView;
        mDetailKanalSwipeRefreshLayout = rootView.findViewById(R.id.kanal_detail_swiperefreshlayout);
        mFollowBt = rootView.findViewById(R.id.follow_bt);
        mProgressBarView = rootView.findViewById(R.id.progress_bar_layout);
        mTabLayout = rootView.findViewById(R.id.kanal_detail_tablayout);
        mDataViewpager = rootView.findViewById(R.id.kanal_detail_viewpager);
    }

    @Override
    protected KanalDetailPresenter initPresenter() {
        return new KanalDetailPresenter(getContext(), this);
    }

    @Override
    protected void initEvent() {
        mHandler = new Handler();
        initData();

        mTabLayout.setupWithViewPager(mDataViewpager);
        mKanalDetailPagerAdapter = new KanalDetailPagerAdapter(getContext());
        mDataViewpager.setAdapter(mKanalDetailPagerAdapter);

        /**
         * setOnXXXListener
         */
        mFollowBt.setOnClickListener(this);

        followRunnable = new Runnable() {
            @Override
            public void run() {
                LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
                boolean isSelected = mFollowBt.isSelected();
                if (accountMessage != null && initFollow != isSelected) {
                    initFollow = isSelected;

                    EditFavouriteReqBean editFavouriteReqBean = new EditFavouriteReqBean();
                    editFavouriteReqBean.setContentid(mKanalBean.getViewer_id());
                    editFavouriteReqBean.setContenttype(mKanalBean.getType());
                    editFavouriteReqBean.setViewerid(accountMessage.getViewer_id());
                    editFavouriteReqBean.setOperation(isSelected ? EditFavouriteReqBean.EDIT_MARK : EditFavouriteReqBean.EDIT_UNMARK);
                    mPresenter.editFavourite(editFavouriteReqBean);
                }
            }
        };

        mDataViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mDetailKanalSwipeRefreshLayout.setRefreshing(false);
                    mDetailKanalSwipeRefreshLayout.setRefreshEnable(false);
                } else {
                    mDetailKanalSwipeRefreshLayout.setRefreshEnable(true);
                }
            }
        });

        mDetailKanalSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDatas();
            }
        });
    }

    private void initData() {
        if (mKanalBean == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mKanalBean = (KanalRspBean.SearchBean) arguments.getSerializable(KANAL_BEAN_DETAIL);
            }
        }

        if (mKanalBean != null) {
            setKanalDetailDatas(mKanalBean);
            refreshDatas();
        } else {
            onFailure(getContext().getString(R.string.loadfail));
        }
    }

    private void refreshDatas() {
        if (mKanalBean != null) {
            checkFollowRequest(true);
            mPresenter.getDatas(mKanalBean);
        } else {
            onFailure(getContext().getString(R.string.loadfail));
        }
    }

    private void setKanalDetailDatas(KanalRspBean.SearchBean kanalBean) {
        ((TextView) mRootView.findViewById(R.id.title)).setText(kanalBean.getUsername());
        ((TextView) mRootView.findViewById(R.id.videos)).setText(getString(R.string.videos, kanalBean.getMovie_count()));
        try {
            GlideUtils.loadImage((ImageView) mRootView.findViewById(R.id.kanal_banner), -1, kanalBean.getPoster().getHorizontal().get(0).getPoster_url(), new CenterCrop());
        } catch (Exception e) {
            GlideUtils.loadImage((ImageView) mRootView.findViewById(R.id.kanal_banner), -1, null, new CenterCrop());
            Log.d(TAG, "initData() called error -- no Horizontal poster");
        }
        try {
            GlideUtils.loadImage((ImageView) mRootView.findViewById(R.id.portrait), R.drawable.glide_default_circle_bg, kanalBean.getPoster().getVertical().get(0).getPoster_url(), new CenterCrop(), new CircleCrop());
        } catch (Exception e) {
            GlideUtils.loadImage((ImageView) mRootView.findViewById(R.id.portrait), R.drawable.glide_default_circle_bg, null, new CenterCrop(), new CircleCrop());
            Log.d(TAG, "initData() called error -- no Horizontal poster");
        }
    }


    @Override
    public void onContentResult(ArrayList<ContentResponseBean.SearchBean> contentResponseBeans) {
        mKanalDetailPagerAdapter.setContentData(contentResponseBeans);
    }

    @Override
    public void onLiveResullt(ArrayList<LiveResponseBean> liveResponseBeans) {
        mKanalDetailPagerAdapter.setLiveData(liveResponseBeans);
    }

    @Override
    public void onSuccess() {
        setLoadingVisibility(false);
        mDetailKanalSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String errorMsg) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMsg);
        mDetailKanalSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void isFollow(boolean isFollow) {
        initFollow = isFollow;
        mFollowBt.setSelected(isFollow);
    }

    private void setLoadingVisibility(boolean visibility) {
        mProgressBarView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.follow_bt:
                if (mPresenter.checkLogin((BaseActivity) getContext())) {
                    v.setSelected(!v.isSelected());
                    checkFollowRequest(false);
                }
                break;
        }
    }

    private void checkFollowRequest(boolean withoutDelay) {//看看是否需要请求更改喜爱状态
        mHandler.removeCallbacks(followRunnable);
        mHandler.postDelayed(followRunnable, withoutDelay ? 0 : 3000);//3秒内连续按无效
    }
}
