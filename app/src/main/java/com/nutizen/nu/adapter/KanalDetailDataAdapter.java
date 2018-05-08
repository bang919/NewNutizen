package com.nutizen.nu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.listener.ContentItemClickListener;

import java.util.ArrayList;

public class KanalDetailDataAdapter {

    private RecyclerView mRecyclerView;
    private View mEmptyView;

    private NormalContentAdapter mNormalContentAdapter;
    private NormalLiveAdapter mNormalLiveAdapter;
    private OnKanalDetailDataClickListener mOnKanalDetailDataClickListener;

    public void setOnKanalDetailDataClickListener(OnKanalDetailDataClickListener listener) {
        mOnKanalDetailDataClickListener = listener;
    }

    public View initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.pager_kanal_detail, null);
        mRecyclerView = inflate.findViewById(R.id.pager_item_kanal_recycler);
        mEmptyView = inflate.findViewById(R.id.pager_item_kanal_nodata_layout);
        return inflate;
    }

    public void setContentData(ArrayList<ContentResponseBean.SearchBean> contentResponseBeans) {
        if (mNormalContentAdapter == null) {
            mNormalContentAdapter = new NormalContentAdapter();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
            mRecyclerView.setAdapter(mNormalContentAdapter);
            mNormalContentAdapter.setOnContentItemClickListener(new ContentItemClickListener() {
                @Override
                public void onContentItemClick(ContentResponseBean.SearchBean searchBean) {
                    if (mOnKanalDetailDataClickListener != null) {
                        mOnKanalDetailDataClickListener.onContentClick(searchBean);
                    }
                }
            });
        }
        mNormalContentAdapter.setDatas(contentResponseBeans);
        mEmptyView.setVisibility(contentResponseBeans != null && contentResponseBeans.size() > 0 ? View.GONE : View.VISIBLE);
    }

    public void setLiveData(ArrayList<LiveResponseBean> liveData) {
        if (mNormalLiveAdapter == null) {
            Context context = mRecyclerView.getContext();
            mNormalLiveAdapter = new NormalLiveAdapter(context);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(mNormalLiveAdapter);
            mNormalLiveAdapter.setItemOnLiveClickListener(new NormalLiveAdapter.ItemOnLiveClickListener() {
                @Override
                public void onLiveClick(LiveResponseBean liveBean) {
                    if (mOnKanalDetailDataClickListener != null) {
                        mOnKanalDetailDataClickListener.onLiveClick(liveBean);
                    }
                }
            });
        }
        mNormalLiveAdapter.setData(liveData);
        mEmptyView.setVisibility(liveData != null && liveData.size() > 0 ? View.GONE : View.VISIBLE);
    }

    public interface OnKanalDetailDataClickListener {
        void onContentClick(ContentResponseBean.SearchBean contentBean);

        void onLiveClick(LiveResponseBean liveResponseBean);
    }
}
