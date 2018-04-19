package com.nutizen.nu.presenter;

import android.content.Context;

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
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

public class ContentPlayerPresenter extends BasePlayerPresenter<ContentPlayerView> {

    private ContentModel mContentModel;
    private CommentModel mCommentModel;

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

//                        Observable<ContentPlaybackBean> playbackInfoByVideoId = mContentModel.getPlaybackInfoByVideoId(search.get(0).getVideo_id());
//                        Observable<WatchHistoryCountRes> watchHistoryCountResObservable = mContentModel.getWatchHistoryCount(search.get(0).getVideo_id())
//                                .map(new Function<WatchHistoryCountRes, WatchHistoryCountRes>() {
//                                    @Override
//                                    public WatchHistoryCountRes apply(WatchHistoryCountRes watchHistoryCountRes) throws Exception {
//                                        mView.onWatchHistoryCount(watchHistoryCountRes.getOffline());
//                                        return watchHistoryCountRes;
//                                    }
//                                });

                        return mContentModel.getPlaybackInfoByVideoId(search.get(0).getVideo_id());
                    }
                }).map(new Function<ContentPlaybackBean, ContentPlaybackBean>() {
                    @Override
                    public ContentPlaybackBean apply(ContentPlaybackBean contentPlaybackBean) throws Exception {
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
                        return contentPlaybackBean;
                    }
                });

        Observable<WatchHistoryCountRes> watchHistoryObservable = mContentModel.getWatchHistoryCount(contentBean.getId())
                .map(new Function<WatchHistoryCountRes, WatchHistoryCountRes>() {
                    @Override
                    public WatchHistoryCountRes apply(WatchHistoryCountRes watchHistoryCountRes) throws Exception {
                        mView.onWatchHistoryCount(watchHistoryCountRes.getOffline());
                        return watchHistoryCountRes;
                    }
                });

        Observable<ArrayList<CommentResult>> commentListObs = mCommentModel
                .getCommentList(contentBean.getType(), contentBean.getId(), 1, 3, -1)
                .map(new Function<ArrayList<CommentResult>, ArrayList<CommentResult>>() {
                    @Override
                    public ArrayList<CommentResult> apply(ArrayList<CommentResult> commentResults) throws Exception {
                        mView.onCommentListResponse(commentResults);
                        return commentResults;
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
}
