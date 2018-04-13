package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.http.HttpClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bigbang on 2018/4/11.
 */

public class HomeFragmentModel {
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

    public Observable<ArrayList<LiveResponseBean>> requestLive() {
        return HttpClient.getApiInterface()
                .requestLive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
