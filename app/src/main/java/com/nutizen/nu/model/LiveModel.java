package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.http.HttpClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LiveModel {

    /**
     * ======================================================= Contributor Live =======================================================
     */
    public Observable<ArrayList<LiveResponseBean>> requestLive() {
        return HttpClient.getApiInterface()
                .requestLive()
                .map(new Function<ArrayList<LiveResponseBean>, ArrayList<LiveResponseBean>>() {
                    @Override
                    public ArrayList<LiveResponseBean> apply(ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                        ArrayList<LiveResponseBean> result = new ArrayList<>();
                        for (LiveResponseBean bean : liveResponseBeans) {
                            if (bean.getSynopsis().contains(";")) {
                                result.add(bean);
                            }
                        }
                        return result;
                    }
                })
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
