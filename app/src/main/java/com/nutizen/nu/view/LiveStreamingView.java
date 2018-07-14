package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.LiveStreamingResult;

import java.io.File;

public interface LiveStreamingView {
    void onError(String error);

    void onLuBanSuccess(File picFile);

    void onLiveStreamCreate(LiveStreamingResult liveStreamingResult, String liveTitle, boolean archive, boolean audioOnly);
}
