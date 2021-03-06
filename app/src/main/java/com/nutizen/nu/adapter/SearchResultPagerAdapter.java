package com.nutizen.nu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.ContentPlayerActivity;
import com.nutizen.nu.activity.SearchActivity;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.fragment.KanalDetailFragment;
import com.nutizen.nu.utils.ToastUtils;

import java.util.ArrayList;

public class SearchResultPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int[] tabTitles = {R.string.video, R.string.kanal};
    private ArrayList<View> mViews;
    private SearchListAdapter<ContentResponseBean.SearchBean> mVideoDataAdapter;
    private SearchListAdapter<KanalRspBean.SearchBean> mKanalDataAdapter;

    public SearchResultPagerAdapter(Context context) {
        mContext = context;

        mViews = new ArrayList<>();

        mVideoDataAdapter = new SearchListAdapter<>();
        mVideoDataAdapter.setOnSearchListListener(new SearchListAdapter.OnSearchListListener<ContentResponseBean.SearchBean>() {
            @Override
            public void onSearchClick(ContentResponseBean.SearchBean data, int position) {
                ContentPlayerActivity.startContentPlayActivity(mContext, data);
            }

            @Override
            public void onBottomShowMore() {
                if (mContext instanceof SearchActivity) {
                    ((SearchActivity) mContext).requestMoreContent();
                }
            }
        });
        mViews.add(mVideoDataAdapter.initView(context));

        mKanalDataAdapter = new SearchListAdapter<>();
        mKanalDataAdapter.setOnSearchListListener(new SearchListAdapter.OnSearchListListener<KanalRspBean.SearchBean>() {
            @Override
            public void onSearchClick(KanalRspBean.SearchBean data, int position) {
                KanalDetailFragment.getInstance(data).show(((SearchActivity) mContext).getSupportFragmentManager(), KanalDetailFragment.TAG);
            }

            @Override
            public void onBottomShowMore() {
                ToastUtils.showShort("No more kanals");
                mKanalDataAdapter.hideLoadingView();
            }
        });
        mViews.add(mKanalDataAdapter.initView(context));
    }

    /**
     *  ------------------ Video ------------------
     */
    public void setVideoDatas(ArrayList<ContentResponseBean.SearchBean> contentResponseBeans) {
        if (mVideoDataAdapter != null) {
            mVideoDataAdapter.setDatas(contentResponseBeans);
        }
    }

    public void setMoreVieoDatas(ArrayList<ContentResponseBean.SearchBean> contentResponseBeans) {
        if (mVideoDataAdapter != null) {
            mVideoDataAdapter.setMoreDatas(contentResponseBeans);
        }
    }

    public void hideVideoLoadingMore(){
        if (mVideoDataAdapter != null) {
            mVideoDataAdapter.hideLoadingView();
        }
    }

    /**
     *  ------------------ Kanal ------------------
     */
    public void setKanalDatas(ArrayList<KanalRspBean.SearchBean> kanalDatas) {
        if (mKanalDataAdapter != null) {
            mKanalDataAdapter.setDatas(kanalDatas);
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return MyApplication.getMyApplicationContext().getString(tabTitles[position]);
    }
}
