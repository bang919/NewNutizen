package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.http.HttpClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LiveModel {

    /**
     * ======================================================= Contributor Live =======================================================
     */
    public Observable<ArrayList<LiveResponseBean>> requestLive() {
        return HttpClient.getApiInterface()
                .requestLive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * ======================================================= Tv And Radio =======================================================
     */
    public Observable<ArrayList<LiveResponseBean>> requestTvAndRadio() {
        return HttpClient.getApiInterface()
                .requestTvAndRadio()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
