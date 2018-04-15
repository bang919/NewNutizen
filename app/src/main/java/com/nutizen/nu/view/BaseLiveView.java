package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.LiveResponseBean;

import java.util.ArrayList;

public interface BaseLiveView {
    void onLivesResponse(ArrayList<LiveResponseBean> liveResponseBeans);

    void onLivesFailure(String errorMsg);
}
