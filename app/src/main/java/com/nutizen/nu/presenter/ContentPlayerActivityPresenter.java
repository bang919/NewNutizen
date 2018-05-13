package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.request.WatchHistoryCountBody;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.WatchHistoryCountRes;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.FavouriteModel;
import com.nutizen.nu.view.ContentPlayerActivityView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;

public class ContentPlayerActivityPresenter extends PlayerActivityPresenter<ContentResponseBean.SearchBean, ContentPlayerActivityView> {

    private ContentModel mContentModel;

    private FavouriteModel mFavouriteModel;


    public ContentPlayerActivityPresenter(Context context, ContentPlayerActivityView view) {
        super(context, view);
        mContentModel = new ContentModel();
        mFavouriteModel = new FavouriteModel();
    }

    @Override
    public void getDatas(final ContentResponseBean.SearchBean contentBean) {
        Observable<ContentPlaybackBean> contentPlaybackBeanObservable = mContentModel.getVideoIdByContentId(contentBean.getId())
                .flatMap(new Function<ContentResponseBean, ObservableSource<ContentPlaybackBean>>() {
                    @Override
                    public ObservableSource<ContentPlaybackBean> apply(ContentResponseBean contentResponseBean) throws Exception {
                        ArrayList<ContentResponseBean.SearchBean> search = contentResponseBean.getSearch();
                        if (search == null || search.size() == 0 || search.get(0).getVideo_id() == 0) {
                            throw new Exception("Can't find video for " + contentBean.getTitle() + " (content_id: " + contentBean.getId() + ")");
                        }
                        return mContentModel.getPlaybackInfoByVideoId(search.get(0).getVideo_id());
                    }
                }).doOnNext(new Consumer<ContentPlaybackBean>() {
                    @Override
                    public void accept(ContentPlaybackBean contentPlaybackBean) throws Exception {
                        mView.onContentPlaybackResponse(contentBean, contentPlaybackBean);
                    }
                });

        Observable<WatchHistoryCountRes> watchHistoryObservable = mContentModel.getWatchHistoryCount(contentBean.getId())
                .doOnNext(new Consumer<WatchHistoryCountRes>() {
                    @Override
                    public void accept(WatchHistoryCountRes watchHistoryCountRes) throws Exception {
                        mView.onWatchHistoryCount(watchHistoryCountRes.getOffline());
                    }
                });
        Observable<ArrayList<CommentResult>> commentListObs = getComments(contentBean.getType(), contentBean.getId());

        Observable<Boolean> checkFavouriteObservable = mFavouriteModel.checkFavourite(contentBean.getType(), contentBean.getId()).doOnNext(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isFavourite) throws Exception {
                mView.isFavourite(isFavourite);
            }
        });

        Observable<String> zip = Observable.zip(contentPlaybackBeanObservable, commentListObs, watchHistoryObservable, checkFavouriteObservable,
                new Function4<ContentPlaybackBean, ArrayList<CommentResult>, WatchHistoryCountRes, Boolean, String>() {
                    @Override
                    public String apply(ContentPlaybackBean contentPlaybackBean, ArrayList<CommentResult> commentResults, WatchHistoryCountRes watchHistoryCountRes, Boolean pass) throws Exception {
                        return "success";
                    }
                }).retry(2);

        String observerTag = getClass().getName() + "getDatas";
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

    /**
     * onDestroy时增加观看次数
     */
    public void addWatchHistoryCount(WatchHistoryCountBody watchHistoryCountBody) {
        subscribeNetworkTask(mContentModel.addWatchHistoryCount(watchHistoryCountBody));
    }

    /**
     * 更改喜爱
     */
    public void editFavourite(EditFavouriteReqBean editFavouriteReqBean) {
        subscribeNetworkTask(mFavouriteModel.editFavourite(editFavouriteReqBean));
    }
}
