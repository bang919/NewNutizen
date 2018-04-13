package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;

import java.util.ArrayList;

/**
 * Created by bigbang on 2018/4/11.
 */

public interface HomeFragmentView {
    void onBannerData(ContentResponseBean contentResponseBean);

    void onEditorsData(ContentResponseBean contentResponseBean);

    void onNewlyData(ContentResponseBean contentResponseBean);

    void onLiveData(ArrayList<LiveResponseBean> liveResponseBeans);

    void onDataRequestSuccess();

    void onDataRequestFailure(String error);
}
