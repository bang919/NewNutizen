package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.view.LiveStreamingView;

public class LiveStreamingActivityPresenter extends BasePresenter<LiveStreamingView> {
    public LiveStreamingActivityPresenter(Context context, LiveStreamingView view) {
        super(context, view);
    }
}
