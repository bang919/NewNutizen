package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;

public interface ContentPlayerActivityView extends  BasePlayerActivityView{

    void onContentPlaybackResponse(ContentResponseBean.SearchBean contentResponseBean, ContentPlaybackBean contentPlaybackBean);

    void onWatchHistoryCount(int count);

    void isFavourite(boolean favourite);
}
