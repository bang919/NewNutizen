package com.nutizen.nu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.ContentPlayerActivity;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.MyApplication;

import java.util.ArrayList;

public class KanalDetailPagerAdapter extends PagerAdapter implements KanalDetailDataAdapter.OnKanalDetailDataClickListener {

    private Context mContext;
    private int[] tabTitles = {R.string.video, R.string.live};
    private ArrayList<View> mViews;
    private KanalDetailDataAdapter mVideoDataAdapter;
    private KanalDetailDataAdapter mLiveDataAdapter;

    public KanalDetailPagerAdapter(Context context) {
        mContext = context;

        mViews = new ArrayList<>();

        mVideoDataAdapter = new KanalDetailDataAdapter();
        mVideoDataAdapter.setOnKanalDetailDataClickListener(this);
        mViews.add(mVideoDataAdapter.initView(context));

        mLiveDataAdapter = new KanalDetailDataAdapter();
        mLiveDataAdapter.setOnKanalDetailDataClickListener(this);
        mViews.add(mLiveDataAdapter.initView(context));
    }

    public void setContentData(ArrayList<ContentResponseBean.SearchBean> contentResponseBeans) {
        if (mVideoDataAdapter != null) {
            mVideoDataAdapter.setContentData(contentResponseBeans);
        }
    }

    public void setLiveData(ArrayList<LiveResponseBean> liveData) {
        if (mLiveDataAdapter != null) {
            mLiveDataAdapter.setLiveData(liveData);
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

    @Override
    public void onContentClick(ContentResponseBean.SearchBean contentBean) {
        ContentPlayerActivity.startContentPlayActivity(mContext, contentBean);
    }

    @Override
    public void onLiveClick(LiveResponseBean liveResponseBean) {

    }
}
