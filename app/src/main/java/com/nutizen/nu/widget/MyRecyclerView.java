package com.nutizen.nu.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

/**
 * 快速滑动暂停加载图片
 */
public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setScrollStateChangeListener();
    }

    private void setScrollStateChangeListener() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    if (getContext() != null) Glide.with(getContext()).resumeRequests();
                } else {
                    if (getContext() != null) Glide.with(getContext()).pauseRequests();
                }
            }
        });
    }
}
