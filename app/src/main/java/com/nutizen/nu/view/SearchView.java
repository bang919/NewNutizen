package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;

public interface SearchView {
    void onVideoSearch(ContentResponseBean contentResponseBean);

    void onKanalSearch(KanalRspBean kanalRspBean);

    void noMoreContents();

    void onMoreVideoSearch(ContentResponseBean contentResponseBean);

    void onSuccess();

    void onFailure(String errorMessage);
}
