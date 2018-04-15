package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.LiveResponseBean;

import java.util.ArrayList;

public interface TvView {
    void onTvsResponse(ArrayList<LiveResponseBean> liveResponseBeans);

    void onTvsFailure(String errorMsg);
}
