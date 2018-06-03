package com.nutizen.nu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;

import com.nutizen.nu.activity.ContentPlayerActivity;
import com.nutizen.nu.bean.response.ContentResponseBean;

public class DownlaodCompleteListAdapter extends DownloadListAdapter {

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //屏蔽mProgressBar/mTitle
        DownloadViewHolder downloadViewHolder = super.onCreateViewHolder(parent, viewType);
        downloadViewHolder.mProgressBar.setVisibility(View.GONE);
        downloadViewHolder.mPlayBtn.setVisibility(View.GONE);
        downloadViewHolder.mTitle.setLines(3);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) downloadViewHolder.mTitle.getLayoutParams();
        layoutParams.topToTop = 0;
        layoutParams.bottomToBottom = 0;
        return downloadViewHolder;
    }

    @Override
    public void onItemClick(Context context, DownloadViewHolder holder, ContentResponseBean.SearchBean bean) {
        ContentPlayerActivity.startContentPlayActivity(context, bean);
    }

}
