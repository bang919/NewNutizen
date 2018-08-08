package com.nutizen.nu.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.request.CommentBean;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Twitter;
import com.nutizen.nu.model.CommentModel;
import com.nutizen.nu.model.ShareModel;
import com.nutizen.nu.view.BasePlayerActivityView;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public abstract class PlayerActivityPresenter<D, T extends BasePlayerActivityView> extends BasePlayerPresenter<T> implements ShareModel.OnShortDynamicCompleteListener, PlatformActionListener {

    private CommentModel mCommentModel;
    private ShareModel mShareModel;

    private final int intPageLimit = 9999;

    public PlayerActivityPresenter(Context context, T view) {
        super(context, view);
        mCommentModel = new CommentModel();
        mShareModel = new ShareModel(this);
    }

    public abstract void getDatas(D dataBean);

    public void shareToPlatform(D dataBean, String platformName) {
        if (dataBean instanceof ContentResponseBean.SearchBean) {
            mShareModel.shareContent((ContentResponseBean.SearchBean) dataBean, platformName);
        } else if (dataBean instanceof LiveResponseBean) {
            mShareModel.shareLive((LiveResponseBean) dataBean, platformName);
        }
    }


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

    /**
     * shareToPlatform 生成 DynamicLink 回调
     *
     * @param platformName
     * @param task
     */
    @Override
    public void onComplete(String platformName, @NonNull Task<ShortDynamicLink> task) {
        if (mView == null) {//如果mView==null即dynamic link未返回就离开Activity，则不分享
            return;
        }
        if (task.isSuccessful()) {
            // Short link created
            Uri shortLink = task.getResult().getShortLink();
            /**
             * Twitter需要另外处理
             */
            if (platformName.equals(Twitter.NAME)) {
                shareToTwitter(shortLink);
                return;
            }
            Platform platform = ShareSDK.getPlatform(platformName);
            Platform.ShareParams params = new Platform.ShareParams();
            params.setUrl(shortLink.toString());
            params.setText(shortLink.toString());
            platform.setPlatformActionListener(this);
            platform.share(params);
        } else {
            mView.onFailure(mContext.getString(R.string.dynamic_link_error));
        }
    }

    @Override
    public void switchPlayerSize(BaseActivity baseActivity, View topBarView, boolean fullScreen) {
        super.switchPlayerSize(baseActivity, topBarView, fullScreen);
        //切换是否能swipeback退出
        baseActivity.setSwipeBackEnable(!fullScreen);
    }

    private void shareToTwitter(Uri shortLink) {
        try {
            new TweetComposer.Builder(mContext)
                    .text(" ")
                    .url(new URL(shortLink.toString()))
                    .show();
            mView.onFailure(mContext.getString(R.string.jump_to_twitter_share));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mView.onFailure(e.getLocalizedMessage());
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        mView.onFailure(mContext.getString(R.string.share_success));
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        mView.onFailure(throwable.getLocalizedMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        mView.onFailure(mContext.getString(R.string.cancel));
    }
}
