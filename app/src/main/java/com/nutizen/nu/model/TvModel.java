package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.http.HttpClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TvModel {

    public Observable<ArrayList<LiveResponseBean>> requestTvs() {
        return HttpClient.getApiInterface()
                .requestTvs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
