package com.nutizen.nu.widget;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

public class MyLinearSmoothScroller extends LinearSmoothScroller {
    public MyLinearSmoothScroller(Context context) {
        super(context);
    }

    @Override
    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        return boxStart - viewStart;
    }
}
