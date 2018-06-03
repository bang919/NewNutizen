package com.nutizen.nu.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.utils.DownloadDatabaseUtil;

import java.util.ArrayList;

public class DownloadViewPagerAdapter extends PagerAdapter {

    private int[] tabTitles = {R.string.downloading, R.string.downloaded};
    private ArrayList<View> mViews;
    private DownloadListAdapter mDownloadingAdapter;
    private DownloadListAdapter mDownloadedAdapter;

    public DownloadViewPagerAdapter(final Context context, final DownloadViewPagerAdapterListener downloadViewPagerAdapterListener) {
        mViews = new ArrayList<>();
        mDownloadingAdapter = new DownlaodingListAdapter(new DownlaodingListAdapter.DownloadingListAdapterListener() {
            @Override
            public void onCompleteOne(BaseDownloadTask task) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        downloadViewPagerAdapterListener.onDownloadCompleteData();
                    }
                });
            }
        });
        mViews.add(mDownloadingAdapter.initView(context));

        mDownloadedAdapter = new DownlaodCompleteListAdapter();
        mViews.add(mDownloadedAdapter.initView(context));
    }

    public void setDownloadingData(ArrayList<ContentResponseBean.SearchBean> datas) {
        mDownloadingAdapter.setData(datas);
    }

    public void setDownloadedData(ArrayList<ContentResponseBean.SearchBean> datas) {
        mDownloadedAdapter.setData(datas);
    }

    public void changeEditStatus(int pagePosition, boolean edit) {
        mDownloadingAdapter.changeEditStatus(pagePosition == 0 && edit);
        mDownloadedAdapter.changeEditStatus(pagePosition != 0 && edit);
    }

    public void removeDownloadSelected(Context context, int pagePosition) {
        ArrayList<ContentResponseBean.SearchBean> selectDownload;
        if (pagePosition == 0) {
            selectDownload = mDownloadingAdapter.getSelectDownload();
        } else {
            selectDownload = mDownloadedAdapter.getSelectDownload();
        }
        for (ContentResponseBean.SearchBean contentBean : selectDownload) {
            DownloadDatabaseUtil.removeDownloadBeanByDownloadFileName(context, contentBean.getDownloadFileName());
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

    public interface DownloadViewPagerAdapterListener {
        void onDownloadCompleteData();
    }
}
