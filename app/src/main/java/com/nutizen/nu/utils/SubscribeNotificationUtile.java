package com.nutizen.nu.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessaging;
import com.nutizen.nu.bean.response.ContentBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.presenter.LoginPresenter;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by bigbang on 2018/3/21.
 */

public class SubscribeNotificationUtile {
    public static void subscribeAll(Context context) {
        subscribeImportant();
        subscribeHomeBanner();
        subscribePilihanEditor();
        subscribeAllContributorsVod(context);
        subscribeAllContributorsLive(context);
    }

    public static void unsubscribeAll(Context context) {
        unsubscribeImportant();
        unsubscribeHomeBanner();
        unsubscribePilihanEditor();
        unsubscribeAllContributorsVod(context);
        unsubscribeAllContributorsLive(context);
    }

    public static void subscribeImportant() {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_IMPORTANT, true);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATION_IMPORTANT);
    }

    public static void unsubscribeImportant() {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_IMPORTANT, false);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_IMPORTANT);
    }

    public static void subscribeHomeBanner() {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_HOME_BANNER, true);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATION_HOME_BANNER);
    }

    public static void unsubscribeHomeBanner() {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_HOME_BANNER, false);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_HOME_BANNER);
    }

    public static void subscribePilihanEditor() {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_PILIHAN_EDITOR, true);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATION_PILIHAN_EDITOR);
    }

    public static void unsubscribePilihanEditor() {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_PILIHAN_EDITOR, false);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_PILIHAN_EDITOR);
    }

    /**
     * follow Contributor ===================================================================
     */
    private static final String TYPE_CONTRIBUTOR = "contributor";

    public static void requestFollowContributors(Context context, final ResponseContributorsCallback callback) {
        String token = LoginPresenter.getAccountMessage() != null ? LoginPresenter.getAccountMessage().getViewer_token() : null;
        if (TextUtils.isEmpty(token)) {
            if (callback != null)
                callback.onResponseContributors(new ArrayList<String>());
            return;
        }
        HttpClient.getApiInterface().getFavourites("Bearer " + token)
                .subscribe(new Observer<ArrayList<FavouriteRspBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<FavouriteRspBean> favouriteRspBeans) {
                        if (favouriteRspBeans != null && favouriteRspBeans.size() != 0) {
                            ArrayList<String> followContributors = new ArrayList<>();
                            for (FavouriteRspBean result : favouriteRspBeans) {
                                String viewerContentType = result.getViewer_content_type();
                                if (!TextUtils.isEmpty(viewerContentType) && viewerContentType.contains(TYPE_CONTRIBUTOR)) {
                                    followContributors.add(result.getContent_title().replaceAll("[^a-zA-Z0-9-_.~%]", "_"));
                                }
                            }
                            if (callback != null)
                                callback.onResponseContributors(followContributors);
                        } else {
                            if (callback != null)
                                callback.onResponseContributors(new ArrayList<String>());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null)
                            callback.onResponseContributors(new ArrayList<String>());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //需要先判断是否打开vod
    public static void subscribeAllContributorsVod(Context context) {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_KANAL, true);
        requestFollowContributors(context, new ResponseContributorsCallback() {
            @Override
            public void onResponseContributors(ArrayList<String> contributors) {
                for (String contributor : contributors) {
                    FirebaseMessaging.getInstance().subscribeToTopic(contributor.concat("_vod"));
                }
            }
        });
    }

    //需要先判断是否关闭vod
    public static void unsubscribeAllContributorsVod(Context context) {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_KANAL, false);
        requestFollowContributors(context, new ResponseContributorsCallback() {
            @Override
            public void onResponseContributors(ArrayList<String> contributors) {
                for (String contributor : contributors) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(contributor.concat("_vod"));
                }
            }
        });
    }

    //需要先判断是否打开vod
    public static void subscribeOneContributorVod(String contributor) {
        String _name = contributor.replaceAll("[^a-zA-Z0-9-_.~%]", "_").concat("_vod");
        FirebaseMessaging.getInstance().subscribeToTopic(_name);
    }

    //需要先判断是否关闭vod
    public static void unsubscribeOneContributorVod(String contributor) {
        String _name = contributor.replaceAll("[^a-zA-Z0-9-_.~%]", "_").concat("_vod");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(_name);
    }

    //需要先判断是否打开live
    public static void subscribeAllContributorsLive(Context context) {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_LIVE, true);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATION_LIVE);
        requestFollowContributors(context, new ResponseContributorsCallback() {
            @Override
            public void onResponseContributors(ArrayList<String> contributors) {
                if (contributors != null && contributors.size() != 0) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_LIVE);
                    for (String contributor : contributors) {
                        FirebaseMessaging.getInstance().subscribeToTopic(contributor.concat("_live"));
                    }
                }
            }
        });
    }

    //需要先判断是否关闭live
    public static void unsubscribeAllContributorsLive(Context context) {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.NOTIFICATION_LIVE, false);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_LIVE);
        requestFollowContributors(context, new ResponseContributorsCallback() {
            @Override
            public void onResponseContributors(ArrayList<String> contributors) {
                if (contributors != null && contributors.size() != 0) {
                    for (String contributor : contributors) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(contributor.concat("_live"));
                    }
                }
            }
        });
    }

    //需要先判断是否打开live
    public static void subscribeOneContributorLive(String contributor) {
        String _name = contributor.replaceAll("[^a-zA-Z0-9-_.~%]", "_").concat("_live");
        FirebaseMessaging.getInstance().subscribeToTopic(_name);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_LIVE);
    }

    //需要先判断是否关闭live
    public static void unsubscribeOneContributorLive(final Context context, final String contributor) {
        String _name = contributor.replaceAll("[^a-zA-Z0-9-_.~%]", "_").concat("_live");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(_name);
        requestFollowContributors(context, new ResponseContributorsCallback() {
            @Override
            public void onResponseContributors(ArrayList<String> contributors) {
                if (contributors != null) {
                    contributors.remove(contributor.replaceAll("[^a-zA-Z0-9-_.~%]", "_"));
                }
                boolean subscribeLive = (boolean) SPUtils.get(context, Constants.NOTIFICATION_LIVE, false);
                if (subscribeLive && (contributors == null || contributors.size() == 0)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATION_LIVE);
                }
            }
        });
    }


    public interface ResponseContributorsCallback {
        void onResponseContributors(ArrayList<String> contributors);
    }

    /**
     * notification Item list =======================================================================================
     *
     * @param itemBean
     */
    public static void saveItemBean(ContentBean itemBean) {
        try {
            ArrayList<ContentBean> itemBeans = getItemBeans();
            if (itemBeans == null) {
                itemBeans = new ArrayList<>();
            }
            itemBeans.add(0, itemBean);

            SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.NOTIFICATIONS_LIST, itemBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ContentBean> getItemBeans() {
        ArrayList<ContentBean> itemBeans = null;
        try {
            itemBeans = SPUtils.getObject(MyApplication.getMyApplicationContext(), Constants.NOTIFICATIONS_LIST);
            long l = System.currentTimeMillis();
            for (int i = itemBeans.size() - 1; i >= 0; i--) {
                ContentBean itemBean1 = itemBeans.get(i);
                if (itemBean1.getCreateTimeL() + 30 * 24 * 60 * 60 * 1000l < l) {
                    itemBeans.remove(itemBean1);
                }
            }

            SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.NOTIFICATIONS_LIST, itemBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemBeans;
    }

    public static void removeItemBean(ContentBean itemBean) {
        try {
            ArrayList<ContentBean> itemBeans = getItemBeans();
            for (ContentBean itemBean1 : itemBeans) {
                if (itemBean.getType().equals(itemBean1.getType()) && itemBean.getId() == (itemBean1.getId())) {
                    itemBeans.remove(itemBean1);
                    SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.NOTIFICATIONS_LIST, itemBeans);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否已经订阅，检查intent是否包含notification信息
     */
    public static void checkFCMNotification(Context context, Intent intent, CheckFCMNotificationCallback callback) {
        //check if login , subscribe contributor:
        boolean subscribeKanal = (boolean) SPUtils.get(context, Constants.NOTIFICATION_KANAL, false);
        boolean subscribeLive = (boolean) SPUtils.get(context, Constants.NOTIFICATION_LIVE, false);
        if (subscribeKanal) {
            SubscribeNotificationUtile.subscribeAllContributorsVod(context);
        }
        if (subscribeLive) {
            SubscribeNotificationUtile.subscribeAllContributorsLive(context);
        }

        //check if click in notification
        if (intent.getSerializableExtra(ContentBean.CONTENT_BEAN) != null) {
            ContentBean contentBean = (ContentBean) intent.getSerializableExtra(ContentBean.CONTENT_BEAN);
            if (contentBean.getType() != null && contentBean.getType().equals("vod")) {
                ContentResponseBean.SearchBean movieBean = contentBean.getMovieBean();
                callback.onFCMNotificationMovie(movieBean);
                return;
            } else if (contentBean.getType() != null && contentBean.getType().equals("live")) {
                LiveResponseBean liveBean = contentBean.getLiveBean();
                callback.onFCMNotificationLive(liveBean);
                return;
            }
        }
        callback.onFCMNotificationFailure();
    }

    public interface CheckFCMNotificationCallback {
        void onFCMNotificationMovie(ContentResponseBean.SearchBean contentBean);

        void onFCMNotificationLive(LiveResponseBean liveBean);

        void onFCMNotificationFailure();
    }
}
