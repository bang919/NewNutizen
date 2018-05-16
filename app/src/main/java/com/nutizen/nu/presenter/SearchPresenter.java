package com.nutizen.nu.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.KanalModel;
import com.nutizen.nu.view.SearchView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class SearchPresenter extends BasePresenter<SearchView> {

    private KanalModel mKanalModel;
    private ContentModel mContentModel;

    private String mText;
    private final int mPageCount = 20;
    private int nextId = -1;

    public SearchPresenter(Context context, SearchView view) {
        super(context, view);
        mKanalModel = new KanalModel();
        mContentModel = new ContentModel();
    }

    public void search(String text) {
        mText = text;
        nextId = -1;
        Observable<String> zip = Observable.zip(
                mKanalModel.requestKanals(text).doOnNext(new Consumer<KanalRspBean>() {
                    @Override
                    public void accept(KanalRspBean kanalRspBean) throws Exception {
                        mView.onKanalSearch(kanalRspBean);
                    }
                }),
                mContentModel.searchMovieByTitle(text, mPageCount, null).doOnNext(new Consumer<ContentResponseBean>() {
                    @Override
                    public void accept(ContentResponseBean contentResponseBean) throws Exception {
                        if (contentResponseBean != null && contentResponseBean.getSearch() != null & contentResponseBean.getSearch().size() > 0
                                && contentResponseBean.getTotalResults() != contentResponseBean.getTotalCount()) {
                            ArrayList<ContentResponseBean.SearchBean> results = contentResponseBean.getSearch();
                            nextId = results.get(results.size() - 1).getId();
                        }
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

    public void searchMoreMovie() {
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        if (nextId == -1) {
            mView.noMoreContents();
            return;
        }
        subscribeNetworkTask(getClass().getName().concat("searchMoreMovie"), mContentModel.searchMovieByTitle(mText, mPageCount, nextId), new MyObserver<ContentResponseBean>() {
            @Override
            public void onMyNext(ContentResponseBean contentResponseBean) {
                if (contentResponseBean != null && contentResponseBean.getSearch() != null & contentResponseBean.getSearch().size() > 0) {
                    ArrayList<ContentResponseBean.SearchBean> results = contentResponseBean.getSearch();
                    nextId = results.get(results.size() - 1).getId();
                    mView.onMoreVideoSearch(contentResponseBean);
                } else {
                    mView.noMoreContents();
                    nextId = -1;
                }
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }
}
