package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;

/**
 * Created by bigbang on 2018/3/29.
 */

public interface SplashView {
    void getNewPic();

    void isLogin(boolean login);

    void jumpToContentPlayerActivity(ContentResponseBean.SearchBean contentBean);

    void jumpToLivePlayerActivity(LiveResponseBean liveResponseBean);
}
