package com.nutizen.nu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by bigbang on 2018/4/11.
 * 提供 监听滑动到最上面/最下面 功能
 */

public class MyScrollView extends ScrollView {

    private OnScrollToTopListener mOnScrollToTopListener;

    public void setOnScrollToTopListener(OnScrollToTopListener listener) {
        mOnScrollToTopListener = listener;
    }

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollToTopListener == null) {
            return;
        }
        if (t == 0) {
            mOnScrollToTopListener.onScrollToTop();
        } else if (oldt == 0 && t != 0) {
            mOnScrollToTopListener.onLeaveFromTop();
        }
    }

    public interface OnScrollToTopListener {
        void onScrollToTop();

        void onLeaveFromTop();
    }
}
