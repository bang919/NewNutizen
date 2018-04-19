package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.WatchHistoryCountRes;
import com.nutizen.nu.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bigbang on 2018/4/11.
 */

public class ContentModel {

    public Observable<ContentResponseBean> requestHomeBanner() {
        return HttpClient.getApiInterface()
                .requestHomeBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ContentResponseBean> requestEditors() {
        return HttpClient.getApiInterface()
                .requestEditors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ContentResponseBean> requestNewly(int limit, int offset) {
        return HttpClient.getApiInterface()
                .requestNewly(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取Content的VideoId，以调用getPlaybackInfoByVideoId获取播放地址
     *
     * @param contentId
     * @return
     */
    public Observable<ContentResponseBean> getVideoIdByContentId(int contentId) {
        return HttpClient.getApiInterface()
                .getVideoIdByContentId(contentId)
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
     * @param cid
     * @return
     */
    public Observable<WatchHistoryCountRes> getWatchHistoryCount(int cid) {
        return HttpClient.getApiInterface()
                .getWatchHistoryCount("movie", cid, "1970-01-01", "2999-12-01")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
