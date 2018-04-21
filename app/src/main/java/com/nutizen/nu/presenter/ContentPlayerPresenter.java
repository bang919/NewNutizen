package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.request.CommentBean;
import com.nutizen.nu.bean.request.WatchHistoryCountBody;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.WatchHistoryCountRes;
import com.nutizen.nu.model.CommentModel;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.view.ContentPlayerView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

public class ContentPlayerPresenter extends BasePlayerPresenter<ContentPlayerView> {

    private final String TAG = "ContentPlayerPresenter";
    private ContentModel mContentModel;
    private CommentModel mCommentModel;
    private final int intPageLimit = 9999;

    public ContentPlayerPresenter(Context context, ContentPlayerView view) {
        super(context, view);
        mContentModel = new ContentModel();
        mCommentModel = new CommentModel();
    }

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
                        String[] genres = contentBean.getGenres().split(",");
                        String writter = "";
                        for (int i = 0; i < genres.length; i++) {
                            String genre = genres[i];
                            if (genre.contains("-")) {
                                String[] split = genre.split("-");
                                writter = split[1];
                            }
                        }
                        mView.onContentPlaybackResponse(writter, contentBean, contentPlaybackBean);
                    }
                });

        Observable<WatchHistoryCountRes> watchHistoryObservable = mContentModel.getWatchHistoryCount(contentBean.getId())
                .doOnNext(new Consumer<WatchHistoryCountRes>() {
                    @Override
                    public void accept(WatchHistoryCountRes watchHistoryCountRes) throws Exception {
                        mView.onWatchHistoryCount(watchHistoryCountRes.getOffline());
                    }
                });

        Observable<ArrayList<CommentResult>> commentListObs = mCommentModel
                .getCommentList(contentBean.getType(), contentBean.getId(), 1, intPageLimit, -1)
                .doOnNext(new Consumer<ArrayList<CommentResult>>() {
                    @Override
                    public void accept(ArrayList<CommentResult> commentResults) throws Exception {
                        mView.onCommentListResponse(commentResults);
                    }
                });

        Observable<String> zip = Observable.zip(contentPlaybackBeanObservable, commentListObs, watchHistoryObservable, new Function3<ContentPlaybackBean, ArrayList<CommentResult>, WatchHistoryCountRes, String>() {
            @Override
            public String apply(ContentPlaybackBean contentPlaybackBean, ArrayList<CommentResult> commentResults, WatchHistoryCountRes watchHistoryCountRes) throws Exception {
                return "Success";
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

    public void commitComment(final ContentResponseBean.SearchBean contentBean, String comment) {
        String obserableTag = getClass().getName() + "sendComment";
        Observable<ArrayList<CommentResult>> commentsObservable = mCommentModel.sendComment(new CommentBean("movie", contentBean.getId(), comment))
                .retry(2)
                .flatMap(new Function<CommentResult, ObservableSource<ArrayList<CommentResult>>>() {
                    @Override
                    public ObservableSource<ArrayList<CommentResult>> apply(CommentResult commentResult) throws Exception {
                        return mCommentModel.getCommentList("movie", contentBean.getId(), 1, intPageLimit, -1).retry(2);
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

    public void deleteComment(final ContentResponseBean.SearchBean contentBean, CommentResult commentResult) {
        String obserableTag = getClass().getName() + "deleteComment";
        subscribeNetworkTask(obserableTag, mCommentModel.deleteComment(commentResult)
                .retry(2)
                .flatMap(new Function<CommentResult, ObservableSource<ArrayList<CommentResult>>>() {
                    @Override
                    public ObservableSource<ArrayList<CommentResult>> apply(CommentResult commentResult) throws Exception {
                        return mCommentModel.getCommentList("movie", contentBean.getId(), 1, intPageLimit, -1).retry(2);
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

    public void addWatchHistoryCount(WatchHistoryCountBody watchHistoryCountBody) {
            mContentModel.addWatchHistoryCount(watchHistoryCountBody).retry(2).subscribe();
    }
}
