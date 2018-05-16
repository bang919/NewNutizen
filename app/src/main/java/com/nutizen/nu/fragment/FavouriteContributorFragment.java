package com.nutizen.nu.fragment;

import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.KanalRspBean;

public class FavouriteContributorFragment extends FavouriteFragment {

    public static FavouriteContributorFragment getInstance() {
        return new FavouriteContributorFragment();
    }

    @Override
    protected String setTitle() {
        return "Favourite Kanal";
    }

    @Override
    protected String setType() {
        return "contributor";
    }

    /**
     * 把FavouriteRspBean转换成KanalRspBean
     */
    @Override
    public void onFavouriteItemClick(final FavouriteRspBean favouriteRspBean) {
        setProgressBarVisibility(true);
        mPresenter.getKanalResponseByFavourite(favouriteRspBean);
    }

    @Override
    public void onKanalClick(KanalRspBean.SearchBean kanalBean) {
        super.onKanalClick(kanalBean);
        KanalDetailFragment.getInstance(kanalBean).show(getFragmentManager(), KanalDetailFragment.TAG);
    }
}
