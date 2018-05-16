package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.FavouriteRspBean;

import java.util.ArrayList;
import java.util.TreeMap;

public interface FavouriteView {

    void onFavouriteResponse(TreeMap<String, ArrayList<FavouriteRspBean>> favouriteMap);

    void onFailure(String errorMsg);
}
