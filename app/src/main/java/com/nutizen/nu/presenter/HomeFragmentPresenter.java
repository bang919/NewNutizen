package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.LiveModel;
import com.nutizen.nu.view.HomeFragmentView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bigbang on 2018/4/11.
 */

public class HomeFragmentPresenter extends BasePresenter<HomeFragmentView> {

    public final String TAG = "HomeFragmentPresenter";
    private ContentModel mContentModel;
    private LiveModel mLiveModel;

    public HomeFragmentPresenter(Context context, HomeFragmentView view) {
        super(context, view);
        mContentModel = new ContentModel();
        mLiveModel = new LiveModel();
    }

    public void requestDatas() {
        Observable<ContentResponseBean> source1 = mContentModel.requestHomeBanner()
                .doOnNext(new Consumer<ContentResponseBean>() {
                    @Override
                    public void accept(ContentResponseBean contentResponseBean) throws Exception {
                        mView.onBannerData(contentResponseBean);
                    }
                });

        Observable<ContentResponseBean> source2 = mContentModel.requestEditors().doOnNext(new Consumer<ContentResponseBean>() {
            @Override
            public void accept(ContentResponseBean contentResponseBean) throws Exception {
                mView.onEditorsData(contentResponseBean);
            }
        });

        Observable<ContentResponseBean> source3 = mContentModel.requestNewly(9, 0)
                .doOnNext(new Consumer<ContentResponseBean>() {
                    @Override
                    public void accept(ContentResponseBean contentResponseBean) throws Exception {
                        mView.onNewlyData(contentResponseBean);
                    }
                });

        Observable<ArrayList<LiveResponseBean>> source4 = mLiveModel.requestLive()
                .doOnNext(new Consumer<ArrayList<LiveResponseBean>>() {
                    @Override
                    public void accept(ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                        mView.onLiveData(liveResponseBeans);
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
