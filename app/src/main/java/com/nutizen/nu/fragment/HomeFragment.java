package com.nutizen.nu.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.ContentPlayerActivity;
import com.nutizen.nu.activity.LivePlayerActivity;
import com.nutizen.nu.adapter.HomeContentAdapterd;
import com.nutizen.nu.adapter.HomeLiveAdapter;
import com.nutizen.nu.adapter.MainBannerAdapter;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseMainFragment;
import com.nutizen.nu.listener.ContentItemClickListener;
import com.nutizen.nu.presenter.HomeFragmentPresenter;
import com.nutizen.nu.utils.ScreenUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.HomeFragmentView;
import com.nutizen.nu.widget.MyScrollView;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class HomeFragment extends BaseMainFragment<HomeFragmentPresenter> implements View.OnClickListener, HomeFragmentView, ContentItemClickListener, HomeLiveAdapter.OnLiveClickListener {

    private View mLiveLayout;
    private LinearLayout mBannerDotLayout;
    private RecyclerView mBannerView;
    private RecyclerView mEditorRv, mNewlyRv, mLiveRv;
    private HomeContentAdapterd mEditorAdapter, mNewlyAdapter;
    private ArrayList<ContentResponseBean.SearchBean> mEditorDatas, mNewlyDatas;
    private ArrayList<LiveResponseBean> mLiveDatas;
    private HomeLiveAdapter mLiveAdapter;
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
    public void onViewPagerFragmentResume() {
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

        mEditorAdapter = createAndBindAdapter(mEditorRv, HomeContentAdapterd.class);
        mNewlyAdapter = createAndBindAdapter(mNewlyRv, HomeContentAdapterd.class);
        mLiveAdapter = createAndBindAdapter(mLiveRv, HomeLiveAdapter.class);
    }

    public <T extends RecyclerView.Adapter> T createAndBindAdapter(RecyclerView recyclerView, Class<T> tClass) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        T adapter = null;
        if (tClass.equals(HomeContentAdapterd.class)) {
            adapter = (T) new HomeContentAdapterd();
            ((HomeContentAdapterd) adapter).setOnContentItemClickListener(this);
        } else if (tClass.equals(HomeLiveAdapter.class)) {
            adapter = (T) new HomeLiveAdapter();
            ((HomeLiveAdapter) adapter).setOnLiveClickListener(this);
        }
        recyclerView.setAdapter(adapter);
        LinearSnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    setRefreshEnable(false);
                } else {
                    setRefreshEnable(true);
                }
            }
        });
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
    public void onContentItemClick(ContentResponseBean.SearchBean searchBean) {
        ContentPlayerActivity.startContentPlayActivity(getContext(), searchBean);
    }


    @Override
    public void onLiveItemClick(LiveResponseBean liveBean, int position) {
        LivePlayerActivity.startLivePlayActivity(getContext(), liveBean);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_iv:
                switch (((View) v.getParent().getParent()).getId()) {
                    case R.id.vid_layout:
                        EditorMoreFragment.getInstance(mEditorDatas, getString(R.string.editor)).show(getFragmentManager(), BaseHomeMoreFragment.TAG);
                        break;
                    case R.id.news_layout:
                        NewlyMoreFragment.getInstance(mNewlyDatas, getString(R.string.newly)).show(getFragmentManager(), BaseHomeMoreFragment.TAG);
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
        mBannerView.setBackground(null);
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
        mEditorDatas = contentResponseBean.getSearch();

        //首页的editors要排序
        ArrayList<ContentResponseBean.SearchBean> searchs = contentResponseBean.getSearch();

        Pattern pattern = Pattern.compile("editors[0-9]");
        TreeMap<Integer, ContentResponseBean.SearchBean> map = new TreeMap<>();

        for (ContentResponseBean.SearchBean searchBean : searchs) {
            String[] genres = searchBean.getGenres().split(",");
            for (String genre : genres) {
                if (pattern.matcher(genre).matches()) {
                    map.put(Integer.valueOf(genre.replace("editors", "")), searchBean);
                    break;
                }
            }
        }
        ArrayList datas;
        if (map.size() > 0) {
            datas = new ArrayList(map.values());
        } else {
            datas = mEditorDatas;
        }
        mEditorAdapter.setDatas(datas);
    }

    @Override
    public void onNewlyData(ContentResponseBean contentResponseBean) {
        mNewlyDatas = contentResponseBean.getSearch();
        mNewlyAdapter.setDatas(mNewlyDatas);
    }

    @Override
    public void onLiveData(ArrayList<LiveResponseBean> liveResponseBeans) {
        if (liveResponseBeans != null && liveResponseBeans.size() > 0) {
            mLiveLayout.setVisibility(View.VISIBLE);
            mLiveDatas = liveResponseBeans;
            mLiveAdapter.setDatas(mLiveDatas);
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
        onDataRefreshFinish();
    }

}
