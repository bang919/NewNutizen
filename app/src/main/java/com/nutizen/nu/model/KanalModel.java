package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KanalModel {
    public Observable<KanalRspBean> requestKanals(String contributorName) {
        return HttpClient.getApiInterface()
                .getKanal(contributorName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
