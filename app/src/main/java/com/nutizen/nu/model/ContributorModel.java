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
                        ContentResponseBean contentResponseBean = new ContentResponseBean();
                        contentResponseBean.setTotalResults(contributorContentResult.getTotalResults());
                        contentResponseBean.setTotalCount(contributorContentResult.getTotalCount());
                        contentResponseBean.setResponse("True");
                        ArrayList<ContentResponseBean.SearchBean> searchBeans = new ArrayList<>();
                        for (ContributorContentResult.SearchBean result : contributorContentResult.getSearch()) {
                            ContentResponseBean.SearchBean searchBean = new ContentResponseBean.SearchBean();
                            searchBean.setId(result.getMovie_id());
                            searchBean.setTitle(result.getMovie_title());
                            //不设置太多了，影响效率啊。。。。
//                            searchBean.setChinese_title(result.getMovie_chinese_title());
//                            searchBean.setOthesr_title(result.getMovie_others_title());
                            searchBean.setTagline(result.getMovie_tagline());
                            searchBean.setDescription(result.getMovie_description());
                            searchBean.setGenres(result.getMovie_genres());
                            searchBean.setDatereleased(result.getMovie_datereleased());
                            searchBean.setThumbnail(result.getMovie_thumbnail());
                            searchBean.setRating(result.getMovie_rating());
//                            searchBean.setDirectors(result.getMovie_directors());
//                            searchBean.setWriters(result.getMovie_writers());
//                            searchBean.setCast(result.getMovie_cast());
                            searchBean.setCountry(result.getMovie_country());
                            searchBean.setLanguage(result.getMovie_language());
                            searchBean.setVideo_id(result.getVideo_id());
                            searchBean.setDuration(result.getMovie_duration());
                            searchBeans.add(searchBean);
                        }
                        contentResponseBean.setSearch(searchBeans);
                        return contentResponseBean;
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
                        ArrayList<LiveResponseBean> liveResponseBeans = new ArrayList<>();
                        for (ContributorLiveResult.SearchBean searchBean : contributorLiveResult.getSearch()) {
                            LiveResponseBean liveResponseBean = new LiveResponseBean();
                            liveResponseBean.setId(searchBean.getLive_id());
                            liveResponseBean.setTitle(searchBean.getLive_title());
//                            liveResponseBean.setRecommend(searchBean.getLive_recommend());
//                            liveResponseBean.setCategory(searchBean.());
                            liveResponseBean.setSynopsis(searchBean.getLive_description());
                            liveResponseBean.setThumbnail(searchBean.getLive_thumbnail());
                            liveResponseBean.setStatus(searchBean.getLive_status());
//                            liveResponseBean.setDelay(searchBean.getLive_delay());
                            liveResponseBean.setType("live");
//                            liveResponseBean.setEpgUrl(searchBean.getLive_epg_url());
                            liveResponseBean.setUrl(searchBean.getHttpUrl());
                            liveResponseBean.setUuid(searchBean.getLive_uuid());
                            liveResponseBeans.add(liveResponseBean);
                        }
                        return liveResponseBeans;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
