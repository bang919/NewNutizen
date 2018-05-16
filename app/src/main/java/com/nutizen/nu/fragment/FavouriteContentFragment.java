package com.nutizen.nu.fragment;

import com.nutizen.nu.activity.ContentPlayerActivity;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.FavouriteRspBean;

public class FavouriteContentFragment extends FavouriteFragment {

    public static FavouriteContentFragment getInstance() {
        return new FavouriteContentFragment();
    }

    @Override
    protected String setTitle() {
        return "Favourite Content";
    }

    @Override
    protected String setType() {
        return "movie";
    }

    /**
     * 把FavouriteRspBean转换成ContentResponseBean
     */
    @Override
    public void onFavouriteItemClick(final FavouriteRspBean favouriteRspBean) {
        setProgressBarVisibility(true);
        mPresenter.getContentResponseByFavourite(favouriteRspBean);
    }

    @Override
    public void onContentClick(ContentResponseBean.SearchBean contentBean) {
        super.onContentClick(contentBean);
        ContentPlayerActivity.startContentPlayActivity(getContext(), contentBean);
    }
}
