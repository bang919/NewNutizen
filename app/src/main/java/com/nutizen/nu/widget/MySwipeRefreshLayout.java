package com.nutizen.nu.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by bigbang on 2018/4/11.
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    boolean mTouchEnable;

    public MySwipeRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public MySwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTouchEnable = true;
    }

    public void setRefreshEnable(boolean touchEnable) {
        mTouchEnable = touchEnable;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mTouchEnable) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
