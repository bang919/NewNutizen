package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.AdvertisementBean;
import com.nutizen.nu.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by bigbang on 2018/3/29.
 */

public class SplashModel {
    public Observable<AdvertisementBean> getAdvertisement() {
        return HttpClient.getApiInterface()
                .getAdvertisement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseBody> downloadAdvertisement(String adUrl) {
        return HttpClient.getApiInterface()
                .downloadFile(adUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
