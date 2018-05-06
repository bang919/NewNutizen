package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;

import java.util.ArrayList;

public interface KanalDetailView {

    void onContentResult(ArrayList<ContentResponseBean.SearchBean> contentResponseBeans);

    void onLiveResullt(ArrayList<LiveResponseBean> liveResponseBeans);

    void onSuccess();

    void onFailure(String errorMsg);

    void isFollow(boolean isFollow);
}
