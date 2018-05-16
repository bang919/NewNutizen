package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.KanalRspBean;

import java.util.ArrayList;
import java.util.TreeMap;

public interface FavouriteView {

    void onFavouriteResponse(TreeMap<String, ArrayList<FavouriteRspBean>> favouriteMap);

    void onContentClick(ContentResponseBean.SearchBean contentBean);

    void onKanalClick(KanalRspBean.SearchBean kanalBean);

    void onFailure(String errorMsg);
}
