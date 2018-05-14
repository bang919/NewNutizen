package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.KanalModel;
import com.nutizen.nu.view.SearchView;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class SearchPresenter extends BasePresenter<SearchView> {

    private KanalModel mKanalModel;
    private ContentModel mContentModel;

    public SearchPresenter(Context context, SearchView view) {
        super(context, view);
        mKanalModel = new KanalModel();
        mContentModel = new ContentModel();
    }

    public void search(String text) {
        Observable<String> zip = Observable.zip(
                mKanalModel.requestKanals(text).doOnNext(new Consumer<KanalRspBean>() {
                    @Override
                    public void accept(KanalRspBean kanalRspBean) throws Exception {
                        mView.onKanalSearch(kanalRspBean);
                    }
                }),
                mContentModel.searchMovieByTitle(text).doOnNext(new Consumer<ContentResponseBean>() {
                    @Override
                    public void accept(ContentResponseBean contentResponseBean) throws Exception {
                        mView.onVideoSearch(contentResponseBean);
                    }
                }),
                new BiFunction<KanalRspBean, ContentResponseBean, String>() {
                    @Override
                    public String apply(KanalRspBean kanalRspBean, ContentResponseBean contentResponseBean) throws Exception {
                        return "success";
                    }
                });
        String observerTag = getClass().getName() + "search";
        subscribeNetworkTask(observerTag, zip, new MyObserver<String>() {
            @Override
            public void onMyNext(String s) {
                mView.onSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }
}
