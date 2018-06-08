package com.nutizen.nu.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.ContributorModel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DynamicLinkUtil {

    public static final String TAG = "DynamicLinkUtil";

    public static void checkDynamicLink(Activity activity, Intent intent, final DynamicLinkUtilCallback dynamicLinkUtilCallback) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(activity, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        Log.d(TAG, "deepLink - " + deepLink);
                        if (deepLink == null) {
                            dynamicLinkUtilCallback.onDynamicFailure();
                            return;
                        }
                        String items[] = deepLink.getPath().substring(1).split("/");
                        String type = items[1];
                        if (type.equals("movie")) {
                            try {
                                String contentId = items[2];
                                searchContent(Integer.valueOf(contentId), dynamicLinkUtilCallback);
                            } catch (Exception e) {
                                dynamicLinkUtilCallback.onDynamicFailure();
                                return;
                            }
                        } else if (type.equals("live")) {
                            try {
                                String authorId = items[4];//viewid
                                String liveId = items[2];
                                searchLive(Integer.valueOf(authorId), Integer.valueOf(liveId), dynamicLinkUtilCallback);
                            } catch (Exception e) {
                                dynamicLinkUtilCallback.onDynamicFailure();
                                return;
                            }
                        }
                    }
                }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dynamicLinkUtilCallback.onDynamicFailure();
            }
        });
    }

    private static void searchContent(int contentId, final DynamicLinkUtilCallback dynamicLinkUtilCallback) {
        ContentModel contentModel = new ContentModel();
        contentModel.getVideoIdByContentId(contentId)
                .subscribe(new Observer<ContentResponseBean.SearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ContentResponseBean.SearchBean searchBean) {
                        dynamicLinkUtilCallback.onDynamicMovie(searchBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dynamicLinkUtilCallback.onDynamicFailure();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private static void searchLive(int authorId, int liveId, final DynamicLinkUtilCallback dynamicLinkUtilCallback) {
        ContributorModel contributorModel = new ContributorModel();
        contributorModel.searchOneLiveOfContributor(authorId, liveId)
                .subscribe(new Observer<LiveResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LiveResponseBean liveResponseBean) {
                        dynamicLinkUtilCallback.onDynamicLive(liveResponseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dynamicLinkUtilCallback.onDynamicFailure();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface DynamicLinkUtilCallback {
        void onDynamicMovie(ContentResponseBean.SearchBean contentBean);

        void onDynamicLive(LiveResponseBean liveBean);

        void onDynamicFailure();
    }
}
