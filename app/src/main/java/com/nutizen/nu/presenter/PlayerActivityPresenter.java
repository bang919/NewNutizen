package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.request.CommentBean;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.model.CommentModel;
import com.nutizen.nu.view.BasePlayerActivityView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public abstract class PlayerActivityPresenter<D, T extends BasePlayerActivityView> extends BasePlayerPresenter<T> {

    private CommentModel mCommentModel;
    private final int intPageLimit = 9999;

    public PlayerActivityPresenter(Context context, T view) {
        super(context, view);
        mCommentModel = new CommentModel();
    }

    public abstract void getDatas(D dataBean);

    public Observable<ArrayList<CommentResult>> getComments(String commentType, int commentId) {
        Observable<ArrayList<CommentResult>> commentListObs = mCommentModel
                .getCommentList(commentType, commentId, 1, intPageLimit, -1)
                .doOnNext(new Consumer<ArrayList<CommentResult>>() {
                    @Override
                    public void accept(ArrayList<CommentResult> commentResults) throws Exception {
                        mView.onCommentListResponse(commentResults);
                    }
                });
        return commentListObs;
    }

    public void commitComment(final String type, final int cid, String comment) {
        String obserableTag = getClass().getName() + "sendComment";
        Observable<ArrayList<CommentResult>> commentsObservable = mCommentModel.sendComment(new CommentBean(type, cid, comment))
                .retry(2)
                .flatMap(new Function<CommentResult, ObservableSource<ArrayList<CommentResult>>>() {
                    @Override
                    public ObservableSource<ArrayList<CommentResult>> apply(CommentResult commentResult) throws Exception {
                        return mCommentModel.getCommentList(type, cid, 1, intPageLimit, -1).retry(2);
                    }
                });
        subscribeNetworkTask(obserableTag, commentsObservable, new MyObserver<ArrayList<CommentResult>>() {
            @Override
            public void onMyNext(ArrayList<CommentResult> commentResults) {
                mView.onCommentListResponse(commentResults);
                mView.onSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }

    public void deleteComment(final String type, final int cid, CommentResult commentResult) {
        String obserableTag = getClass().getName() + "deleteComment";
        subscribeNetworkTask(obserableTag, mCommentModel.deleteComment(commentResult)
                .retry(2)
                .flatMap(new Function<CommentResult, ObservableSource<ArrayList<CommentResult>>>() {
                    @Override
                    public ObservableSource<ArrayList<CommentResult>> apply(CommentResult commentResult) throws Exception {
                        return mCommentModel.getCommentList(type, cid, 1, intPageLimit, -1).retry(2);
                    }
                }), new MyObserver<ArrayList<CommentResult>>() {
            @Override
            public void onMyNext(ArrayList<CommentResult> commentResults) {
                mView.onCommentListResponse(commentResults);
                mView.onSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }
}
