package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.model.FavouriteModel;
import com.nutizen.nu.view.LivePlayerActivityView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class LivePlayerActivityPresenter extends PlayerActivityPresenter<LiveResponseBean, LivePlayerActivityView> {

    private FavouriteModel mFavouriteModel;


    public LivePlayerActivityPresenter(Context context, LivePlayerActivityView view) {
        super(context, view);
        mFavouriteModel = new FavouriteModel();
    }

    @Override
    public void getDatas(LiveResponseBean liveResponseBean) {
        Observable<ArrayList<CommentResult>> commentObservable = getComments(liveResponseBean.getType(), liveResponseBean.getId());
        Observable<Boolean> followObservable = mFavouriteModel.checkFollow(liveResponseBean.getType(), liveResponseBean.getId()).doOnNext(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isFollow) throws Exception {
                mView.isFollow(isFollow);
            }
        });

        Observable<String> zip = Observable.zip(commentObservable, followObservable, new BiFunction<ArrayList<CommentResult>, Boolean, String>() {
            @Override
            public String apply(ArrayList<CommentResult> commentResults, Boolean aBoolean) throws Exception {
                return "success";
            }
        });
        String observableTag = getClass().getName() + "getDatas";
        subscribeNetworkTask(observableTag, zip, new MyObserver<String>() {
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
     * 更改喜爱
     */
    public void editFavourite(EditFavouriteReqBean editFavouriteReqBean) {
        subscribeNetworkTask(mFavouriteModel.editFavourite(editFavouriteReqBean));
    }
}
