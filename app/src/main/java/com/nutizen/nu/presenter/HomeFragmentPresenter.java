package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.HomeFragmentModel;
import com.nutizen.nu.view.HomeFragmentView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bigbang on 2018/4/11.
 */

public class HomeFragmentPresenter extends BasePresenter<HomeFragmentView> {

    public final String TAG = "HomeFragmentPresenter";
    private HomeFragmentModel mHomeFragmentModel;

    public HomeFragmentPresenter(Context context, HomeFragmentView view) {
        super(context, view);
        mHomeFragmentModel = new HomeFragmentModel();
    }

    public void requestDatas() {
        Observable<ContentResponseBean> source1 = mHomeFragmentModel.requestHomeBanner().map(new Function<ContentResponseBean, ContentResponseBean>() {
            @Override
            public ContentResponseBean apply(ContentResponseBean contentResponseBean) throws Exception {
                mView.onBannerData(contentResponseBean);
                return contentResponseBean;
            }
        });
        Observable<ContentResponseBean> source2 = mHomeFragmentModel.requestEditors().map(new Function<ContentResponseBean, ContentResponseBean>() {
            @Override
            public ContentResponseBean apply(ContentResponseBean contentResponseBean) throws Exception {
                mView.onEditorsData(contentResponseBean);
                return contentResponseBean;
            }
        });
        Observable<ContentResponseBean> source3 = mHomeFragmentModel.requestNewly(9, 0).map(new Function<ContentResponseBean, ContentResponseBean>() {
            @Override
            public ContentResponseBean apply(ContentResponseBean contentResponseBean) throws Exception {
                mView.onNewlyData(contentResponseBean);
                return contentResponseBean;
            }
        });
        Observable<ArrayList<LiveResponseBean>> source4 = mHomeFragmentModel.requestLive().map(new Function<ArrayList<LiveResponseBean>, ArrayList<LiveResponseBean>>() {
            @Override
            public ArrayList<LiveResponseBean> apply(ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                mView.onLiveData(liveResponseBeans);
                return liveResponseBeans;
            }
        });

        Observable<String> zip = Observable.zip(source1, source2, source3, source4,
                new Function4<ContentResponseBean, ContentResponseBean, ContentResponseBean, ArrayList<LiveResponseBean>, String>() {
                    @Override
                    public String apply(ContentResponseBean contentResponseBean, ContentResponseBean contentResponseBean2,
                                        ContentResponseBean contentResponseBean3, ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                        return "Success";
                    }
                })
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        final String observerTag = getClass().getName() + "requestDatas";
        subscribeNetworkTask(observerTag, zip, new MyObserver<String>() {
            @Override
            public void onMyNext(String s) {
                mView.onDataRequestSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onDataRequestFailure(errorMessage);
            }
        });
    }
}
