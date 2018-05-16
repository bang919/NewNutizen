package com.nutizen.nu.fragment;

import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.KanalRspBean;

import java.util.ArrayList;

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

        KanalRspBean.SearchBean kanalBean = new KanalRspBean.SearchBean();
        kanalBean.setViewer_id(favouriteRspBean.getContent_id());
        kanalBean.setUsername(favouriteRspBean.getContent_title());
        kanalBean.setThumbnail(favouriteRspBean.getContent_thumbnail());

        KanalRspBean.SearchBean.PosterBean kanalPoster = new KanalRspBean.SearchBean.PosterBean();
        FavouriteRspBean.ContentPosterBean favouritePoster = favouriteRspBean.getContent_poster();
        if (favouritePoster != null) {
            ArrayList<KanalRspBean.SearchBean.PosterBean.HorizontalBean> horizontals = new ArrayList<>();
            for (FavouriteRspBean.ContentPosterBean.HorizontalBean horizontal : favouritePoster.getHorizontal()) {
                KanalRspBean.SearchBean.PosterBean.HorizontalBean horizontalBean = new KanalRspBean.SearchBean.PosterBean.HorizontalBean();
                horizontalBean.setPoster_url(horizontal.getPoster_url());
                horizontals.add(horizontalBean);
            }
            kanalPoster.setHorizontal(horizontals);
            //-----------------------------------------------------------------------------------------
            ArrayList<KanalRspBean.SearchBean.PosterBean.VerticalBean> verticals = new ArrayList<>();
            for (FavouriteRspBean.ContentPosterBean.VerticalBean vertical : favouritePoster.getVertical()) {
                KanalRspBean.SearchBean.PosterBean.VerticalBean verticalBean = new KanalRspBean.SearchBean.PosterBean.VerticalBean();
                verticalBean.setPoster_url(vertical.getPoster_url());
                verticals.add(verticalBean);
            }
            kanalPoster.setVertical(verticals);
        }

        kanalBean.setPoster(kanalPoster);

        KanalDetailFragment.getInstance(kanalBean).show(getFragmentManager(), KanalDetailFragment.TAG);
    }
}
