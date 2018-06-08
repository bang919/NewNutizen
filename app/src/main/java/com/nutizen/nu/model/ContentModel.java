package com.nutizen.nu.model;

import com.nutizen.nu.BuildConfig;
import com.nutizen.nu.bean.request.WatchHistoryCountBody;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.WatchHistoryCountRes;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.presenter.LoginPresenter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bigbang on 2018/4/11.
 */

public class ContentModel {

    public Observable<ContentResponseBean> requestHomeBanner() {
        return HttpClient.getApiInterface()
                .requestHomeBanner()
                .compose(getWritterTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ContentResponseBean> requestEditors() {
        return HttpClient.getApiInterface()
                .requestEditors()
                .compose(getWritterTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ContentResponseBean> requestNewly(int limit, int offset) {
        return HttpClient.getApiInterface()
                .requestNewly(limit, offset)
                .compose(getWritterTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private ObservableTransformer<ContentResponseBean, ContentResponseBean> getWritterTransformer() {
        return new ObservableTransformer<ContentResponseBean, ContentResponseBean>() {
            @Override
            public ObservableSource<ContentResponseBean> apply(Observable<ContentResponseBean> upstream) {
                return upstream.map(new Function<ContentResponseBean, ContentResponseBean>() {
                    @Override
                    public ContentResponseBean apply(ContentResponseBean contentResponseBean) throws Exception {
                        for (ContentResponseBean.SearchBean contentBean : contentResponseBean.getSearch()) {
                            String[] genres = contentBean.getGenres().split(",");
                            String writter = "";
                            for (int i = 0; i < genres.length; i++) {
                                String genre = genres[i];
                                if (genre.contains("-")) {
                                    String[] split = genre.split("-");
                                    writter = split[1];
                                }
                            }
                            contentBean.setWritter(writter);
                        }
                        return contentResponseBean;
                    }
                });
            }
        };
    }


    /**
     * 获取Content的VideoId，以调用getPlaybackInfoByVideoId获取播放地址
     *
     * @param contentId
     * @return
     */
    public Observable<ContentResponseBean.SearchBean> getVideoIdByContentId(final int contentId) {
        return HttpClient.getApiInterface()
                .getVideoIdByContentId(contentId)
                .map(new Function<ContentResponseBean, ContentResponseBean.SearchBean>() {
                    @Override
                    public ContentResponseBean.SearchBean apply(ContentResponseBean contentResponseBean) throws Exception {
                        ArrayList<ContentResponseBean.SearchBean> search = contentResponseBean.getSearch();
                        if (search == null || search.size() == 0 || search.get(0).getVideo_id() == 0) {
                            throw new Exception("Can't find video for content_id: " + contentId);
                        }else {
                            return search.get(0);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用videoid获取Content的播放地址
     *
     * @param vid
     * @return
     */
    public Observable<ContentPlaybackBean> getPlaybackInfoByVideoId(int vid) {
        return HttpClient.getApiInterface()
                .getPlaybackInfoByVideoId(vid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用contentid获取历史播放数量
     *
     * @param cid
     * @return
     */
    public Observable<WatchHistoryCountRes> getWatchHistoryCount(int cid) {
        return HttpClient.getApiInterface()
                .getWatchHistoryCount("movie", cid, "1970-01-01", "2999-12-01")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WatchHistoryCountRes> addWatchHistoryCount(WatchHistoryCountBody watchHistoryCountBody) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        String token = accountMessage == null ? BuildConfig.tourist_token : accountMessage.getViewer_token();
        return HttpClient.getApiInterface()
                .addOfflineWatchHistoryCount("bearer " + token, watchHistoryCountBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * SearchActivity，标题搜索
     */
    public Observable<ContentResponseBean> searchMovieByTitle(String title, int page, Integer nextId) {
        return HttpClient.getApiInterface()
                .searchMovieByTitle(title, page, nextId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
