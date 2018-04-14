package com.nutizen.nu.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.MainBannerAdapter;
import com.nutizen.nu.adapter.MainContentAdapterd;
import com.nutizen.nu.adapter.MainLiveAdapter;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseFragment;
import com.nutizen.nu.presenter.HomeFragmentPresenter;
import com.nutizen.nu.utils.ScreenUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.HomeFragmentView;
import com.nutizen.nu.widget.MyScrollView;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements View.OnClickListener, HomeFragmentView, MainBannerAdapter.OnBannerClickListener {

    private View mLiveLayout;
    private LinearLayout mBannerDotLayout;
    private RecyclerView mBannerView;
    private RecyclerView mEditorRv, mNewlyRv, mLiveRv;
    private MainContentAdapterd mEditorAdapter, mNewlyAdapter;
    private MainLiveAdapter mLiveAdapter;
    private MyScrollView mScrollView;
    private MainBannerAdapter mMainBannerAdapter;
    private int totalScroll;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomeFragmentPresenter initPresenter() {
        return new HomeFragmentPresenter(getContext(), this);
    }

    @Override
    protected void onViewPagerFragmentResume() {
        super.onViewPagerFragmentResume();
        setRefreshEnable(true);
    }

    /**
     * HomeFragment嵌套了ScrollView，所以设置RefreshEnable前要看看是否是在ScrollView的最上面
     */
    @Override
    public void setRefreshEnable(boolean enable) {
        if (enable && mScrollView != null && mScrollView.getScrollY() != 0) {
            super.setRefreshEnable(false);
            return;
        }
        super.setRefreshEnable(enable);
    }

    @Override
    protected void initView(View rootView) {
        mBannerDotLayout = rootView.findViewById(R.id.layout_banner_dot);
        mBannerView = rootView.findViewById(R.id.banner_recyclerview);
        mScrollView = rootView.findViewById(R.id.scrollview_home);
        View vidLayout = rootView.findViewById(R.id.vid_layout);
        View newsLayout = rootView.findViewById(R.id.news_layout);
        mLiveLayout = rootView.findViewById(R.id.live_layout);

        ((TextView) vidLayout.findViewById(R.id.title_tv)).setText(getString(R.string.editor));
        ((TextView) newsLayout.findViewById(R.id.title_tv)).setText(getString(R.string.newly));
        ((TextView) mLiveLayout.findViewById(R.id.title_tv)).setText(getString(R.string.livenow));

        vidLayout.findViewById(R.id.more_iv).setOnClickListener(this);
        newsLayout.findViewById(R.id.more_iv).setOnClickListener(this);
        mLiveLayout.findViewById(R.id.more_iv).setOnClickListener(this);

        mEditorRv = vidLayout.findViewById(R.id.recyclerView);
        mNewlyRv = newsLayout.findViewById(R.id.recyclerView);
        mLiveRv = mLiveLayout.findViewById(R.id.recyclerView);

        mBannerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mMainBannerAdapter = new MainBannerAdapter();
        mBannerView.setAdapter(mMainBannerAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mBannerView);

        mEditorAdapter = createAndBindAdapter(mEditorRv, MainContentAdapterd.class);
        mNewlyAdapter = createAndBindAdapter(mNewlyRv, MainContentAdapterd.class);
        mLiveAdapter = createAndBindAdapter(mLiveRv, MainLiveAdapter.class);
    }

    public <T extends RecyclerView.Adapter> T createAndBindAdapter(RecyclerView recyclerView, Class<T> tClass) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        T adapter = null;
        if (tClass.equals(MainContentAdapterd.class)) {
            adapter = (T) new MainContentAdapterd();
        } else if (tClass.equals(MainLiveAdapter.class)) {
            adapter = (T) new MainLiveAdapter();
        }
        recyclerView.setAdapter(adapter);
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        return adapter;
    }

    @Override
    protected void initEvent() {
        mBannerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    setRefreshEnable(false);
                } else {
                    int width = recyclerView.getWidth();
                    if (width != 0 && mBannerDotLayout.getChildCount() != 0) {
                        int currentPage = (totalScroll / width + width * mBannerDotLayout.getChildCount()) % mBannerDotLayout.getChildCount();
                        setlectBannerDot(currentPage);
                    }
                    setRefreshEnable(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalScroll += dx;
            }
        });
        mMainBannerAdapter.setOnBannerClickListener(this);
        mScrollView.setOnScrollToTopListener(new MyScrollView.OnScrollToTopListener() {
            @Override
            public void onScrollToTop() {
                setRefreshEnable(true);
            }

            @Override
            public void onLeaveFromTop() {
                setRefreshEnable(false);
            }
        });
    }

    @Override
    public void refreshData() {
        mPresenter.requestDatas();
    }


    @Override
    public void onBannerClick(ContentResponseBean.SearchBean searchBean, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_iv:
                switch (((View) v.getParent()).getId()) {
                    case R.id.vid_layout:
                        break;
                    case R.id.news_layout:
                        break;
                    case R.id.live_layout:
                        break;
                }
                break;
        }
    }

    @Override
    public void onBannerData(ContentResponseBean contentResponseBean) {
        ArrayList<ContentResponseBean.SearchBean> search = contentResponseBean.getSearch();
        mMainBannerAdapter.setDatas(search);
        int middle = MainBannerAdapter.BANNER_MAX / 2;
        mBannerView.scrollToPosition(middle - middle % search.size());
        initBannerDotLayout(search.size());
    }


    private void initBannerDotLayout(int size) {
        totalScroll = 0;
        mBannerDotLayout.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView dot = new ImageView(getContext());
            float diameter = ScreenUtils.dip2px(getContext(), 6);
            float margin = ScreenUtils.dip2px(getContext(), 4);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) diameter, (int) diameter);
            dot.setImageResource(R.drawable.banner_dot_selector);
            if (i != 0) {
                params.leftMargin = (int) margin;
            } else {
                dot.setSelected(true);
            }
            mBannerDotLayout.addView(dot, params);
        }
    }

    private void setlectBannerDot(int position) {
        for (int i = 0; i < mBannerDotLayout.getChildCount(); i++) {
            mBannerDotLayout.getChildAt(i).setSelected(position == i);
        }
    }


    @Override
    public void onEditorsData(ContentResponseBean contentResponseBean) {
        mEditorAdapter.setDatas(contentResponseBean.getSearch());
    }

    @Override
    public void onNewlyData(ContentResponseBean contentResponseBean) {
        mNewlyAdapter.setDatas(contentResponseBean.getSearch());
    }

    @Override
    public void onLiveData(ArrayList<LiveResponseBean> liveResponseBeans) {
        if (liveResponseBeans != null && liveResponseBeans.size() > 0) {
            mLiveLayout.setVisibility(View.VISIBLE);
            mLiveAdapter.setDatas(liveResponseBeans);
        } else {
            mLiveLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDataRequestSuccess() {
        onDataRefreshFinish(true);
    }

    @Override
    public void onDataRequestFailure(String error) {
        ToastUtils.showShort(error);
        onDataRefreshFinish(false);
    }

}
