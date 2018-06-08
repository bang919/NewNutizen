package com.nutizen.nu.model;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.ContributorContentResult;
import com.nutizen.nu.bean.response.ContributorLiveResult;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.http.HttpClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ContributorModel {

    public Observable<ContentResponseBean> searchContentByContributor(Integer contributorId, Integer limit, Integer nextId) {
        return HttpClient.getApiInterface()
                .searchContentByContributor(contributorId, limit, nextId)
                .subscribeOn(Schedulers.io())
                .map(new Function<ContributorContentResult, ContentResponseBean>() {
                    @Override
                    public ContentResponseBean apply(ContributorContentResult contributorContentResult) throws Exception {
                        return contributorContentResult.toContentResponseBean();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<LiveResponseBean>> searchLiveByContributor(Integer contributorId, Integer status, Integer limit, Integer nextId) {
        return HttpClient.getApiInterface()
                .searchLiveByContributor(contributorId, status, limit, nextId)
                .map(new Function<ContributorLiveResult, ArrayList<LiveResponseBean>>() {
                    @Override
                    public ArrayList<LiveResponseBean> apply(ContributorLiveResult contributorLiveResult) throws Exception {
                        return contributorLiveResult.toLiveResponseBeans();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LiveResponseBean> searchOneLiveOfContributor(Integer contributorId, final int liveId) {
        return searchLiveByContributor(contributorId, 1, 999, null)
                .map(new Function<ArrayList<LiveResponseBean>, LiveResponseBean>() {
                    @Override
                    public LiveResponseBean apply(ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                        if (liveResponseBeans != null && liveResponseBeans.size() > 0) {
                            for (LiveResponseBean liveResponseBean : liveResponseBeans) {
                                if (liveId == liveResponseBean.getId()) {
                                    return liveResponseBean;
                                }
                            }

                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
