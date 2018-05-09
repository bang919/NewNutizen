package com.nutizen.nu.widget;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * 让Recyclerview的smoothScroolTo滚动后，position在最上面
 */
public class MyLinearSmoothScroller extends LinearSmoothScroller {
    public MyLinearSmoothScroller(Context context) {
        super(context);
    }

    @Override
    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        return boxStart - viewStart;
    }
}
