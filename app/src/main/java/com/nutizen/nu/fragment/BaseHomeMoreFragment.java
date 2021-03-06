package com.nutizen.nu.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.ContentPlayerActivity;
import com.nutizen.nu.adapter.NormalContentAdapter;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.listener.ContentItemClickListener;
import com.nutizen.nu.presenter.MoreHomePresenter;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.MoreHomeView;
import com.nutizen.nu.widget.MySwipeRefreshLayout;

import java.util.ArrayList;

public abstract class BaseHomeMoreFragment extends BaseDialogFragment<MoreHomePresenter> implements View.OnClickListener, ContentItemClickListener, MoreHomeView {

    public static final String TAG = "BaseHomeMoreFragment";
    private TextView mTitleTv;
    private MySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mDataRv;
    private View mLoadingView;
    private NormalContentAdapter mNormalContentAdapter;
    private ArrayList<ContentResponseBean.SearchBean> mDatas;

    private View mBackToTopBt;

    public static <T extends BaseHomeMoreFragment> T getInstance(Class<T> cls, ArrayList<ContentResponseBean.SearchBean> datas, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        ArrayList<ContentResponseBean.SearchBean> s = datas != null ? new ArrayList<>(datas) : null;
        bundle.putSerializable("datas", s);
        try {
            T newlyMoreFragment = cls.newInstance();
            newlyMoreFragment.setArguments(bundle);
            return newlyMoreFragment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home_more;
    }

    @Override
    protected MoreHomePresenter initPresenter() {
        return new MoreHomePresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mTitleTv = rootView.findViewById(R.id.tv_title_home_more);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_home_more);
        mDataRv = rootView.findViewById(R.id.rv_more_home);
        mLoadingView = rootView.findViewById(R.id.loading);
        mBackToTopBt = rootView.findViewById(R.id.back_to_top);
        mBackToTopBt.setOnClickListener(this);
        rootView.findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        Bundle arguments = getArguments();

        if (arguments != null) {
            mTitleTv.setText(arguments.getString("title"));
            mDatas = (ArrayList<ContentResponseBean.SearchBean>) arguments.getSerializable("datas");
        }

        mDataRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mNormalContentAdapter = new NormalContentAdapter();
        mNormalContentAdapter.setOnContentItemClickListener(this);
        mDataRv.setAdapter(mNormalContentAdapter);
        mDataRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!mDataRv.canScrollVertically(-1)) {
                    mBackToTopBt.setVisibility(View.GONE);
                } else {
                    mBackToTopBt.setVisibility(View.VISIBLE);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !mDataRv.canScrollVertically(1) && mDatas != null) {//到达底部，加载更多
                    requestMoreData();
                }
            }
        });
        if (mDatas != null) {
            mNormalContentAdapter.setDatas(mDatas);
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            refreshData();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                hideLoadingView();
            }
        });
    }

    protected abstract void refreshData();

    protected abstract void requestMoreData();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.back_to_top:
                mDataRv.smoothScrollToPosition(0);
//                mBackToTopBt.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onContentItemClick(ContentResponseBean.SearchBean searchBean) {
        getDialog().getWindow().setWindowAnimations(0);
        ContentPlayerActivity.startContentPlayActivity(getContext(), searchBean);
    }

    @Override
    public void onContentDatas(ContentResponseBean contentResponseBean) {
        mDatas = contentResponseBean.getSearch();
        mNormalContentAdapter.setDatas(contentResponseBean.getSearch());
        mSwipeRefreshLayout.setRefreshing(false);
        hideLoadingView();
    }

    @Override
    public void onMoreContentDatas(ContentResponseBean contentResponseBean) {
        mNormalContentAdapter.addDatas(contentResponseBean.getSearch());
        mSwipeRefreshLayout.setRefreshing(false);
        hideLoadingView();
    }

    @Override
    public void requestingMoreData() {
        //Show LoadingView
        mLoadingView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_loading_more_show);
        mLoadingView.startAnimation(animation);
    }

    private void hideLoadingView() {
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_loading_more_hide);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLoadingView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mLoadingView.startAnimation(animation);
        }
    }

    @Override
    public void noMoreData() {
        ToastUtils.showShort("no more datas");
        hideLoadingView();
    }

    @Override
    public void onDataRequestFailure(String error) {
        ToastUtils.showShort(error);
        mSwipeRefreshLayout.setRefreshing(false);
        hideLoadingView();
    }
}
