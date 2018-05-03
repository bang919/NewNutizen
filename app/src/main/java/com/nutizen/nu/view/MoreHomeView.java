package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.ContentResponseBean;

public interface MoreHomeView {

    void onContentDatas(ContentResponseBean contentResponseBean);

    void onMoreContentDatas(ContentResponseBean contentResponseBean);

    void requestingMoreData();

    void noMoreData();

    void onDataRequestFailure(String error);
}
