package com.nutizen.nu.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.ContributorModel;
import com.nutizen.nu.receiver.NotificationReceiver;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.utils.SubscribeNotificationUtile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by bigbang on 2018/3/20.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "FCMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        initNotification(remoteMessage);
    }

    private void initNotification(RemoteMessage remoteMessage) {

        Log.d(TAG, "get a notification data");

        Map<String, String> data = remoteMessage.getData();

        String title = "Nutizen";
        String type = "";
        int id = 0;
        String liveSynopsis = "";
        String body = "";
        String account_profile = null;


        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            if (next.getKey().equals("type")) {
                type = next.getValue();
            } else if (next.getKey().equals("id")) {
                id = Integer.valueOf(next.getValue());
            } else if (next.getKey().equals("description")) {
                liveSynopsis = next.getValue();
            } else if (next.getKey().equals("title")) {
                title = next.getValue();
            } else if (next.getKey().equals("body")) {
                body = next.getValue();
            } else if (next.getKey().equals("account_profile")) {
                account_profile = next.getValue();
            }
        }
        if (!TextUtils.isEmpty(type) && type.equals("vod")) {
            getVodNotification(id, account_profile, title, body, 3);
        } else if (!TextUtils.isEmpty(type) && type.equals("live")) {
            getLiveNotification(id, liveSynopsis, account_profile, title, body, 3);
        }
    }


    private void getVodNotification(final int movieId, final String account_profile, final String title, final String body, final int retryTime) {
        if (retryTime <= 0 || movieId == 0) {
            return;
        }
        ContentModel contentModel = new ContentModel();
        contentModel.getVideoIdByContentId(movieId)
                .subscribe(new Observer<ContentResponseBean.SearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ContentResponseBean.SearchBean searchBean) {
                        if (searchBean != null) {
                            ContentBean contentBean = new ContentBean();
                            contentBean.setType(searchBean.getType());
                            contentBean.setMovieBean(searchBean);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();
                            contentBean.setCreateTimeS(simpleDateFormat.format(date));
                            contentBean.setCreateTimeL(System.currentTimeMillis());
                            SubscribeNotificationUtile.saveItemBean(contentBean);
                            initNotification2(contentBean, account_profile, title, body);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        int retry = retryTime - 1;
                        getVodNotification(movieId, account_profile, title, body, retry);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getLiveNotification(final int liveId, final String liveSynopsis, final String account_profile, final String title, final String body, final int retryTime) {
        if (retryTime <= 0 || liveId == 0) {
            return;
        }
        String contributorId = liveSynopsis.split(";")[1];
        ContributorModel contributorModel = new ContributorModel();
        contributorModel.searchOneLiveOfContributor(Integer.parseInt(contributorId), liveId)
                .subscribe(new Observer<LiveResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LiveResponseBean liveResponseBean) {
                        if (liveResponseBean != null) {
                            ContentBean contentBean = new ContentBean();
                            contentBean.setType(liveResponseBean.getType());
                            contentBean.setLiveBean(liveResponseBean);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();
                            contentBean.setCreateTimeS(simpleDateFormat.format(date));
                            contentBean.setCreateTimeL(System.currentTimeMillis());
                            SubscribeNotificationUtile.saveItemBean(contentBean);
                            initNotification2(contentBean, account_profile, title, body);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        int retry = retryTime - 1;
                        getLiveNotification(liveId, liveSynopsis, account_profile, title, body, retry);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initNotification2(ContentBean contentBean, String account_profile, String title, String body) {
        Intent mainIntent = new Intent(this, NotificationReceiver.class);
        mainIntent.putExtra(ContentBean.CONTENT_BEAN, contentBean);
        PendingIntent mainPendingIntent = PendingIntent.getBroadcast(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrates = {0, 1000, 1000, 1000};
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置通知标题
                .setContentTitle(title)
                //设置通知内容
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .setSound(defaultSoundUri)
                .setVibrate(vibrates);
        //设置小图标
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.ic_nulogo_tran);
            builder.setColor(Color.BLUE);
        } else {
            builder.setSmallIcon(R.mipmap.ic_nulogo);
        }
        //已在主线程中，可以更新UI
        getPic1(account_profile, contentBean.getThumbnail(), title, body, builder, notificationManager, contentBean.getId());
    }

    private void getPic1(final String account_profile, final String thumbnail, final String title, final String body, final NotificationCompat.Builder builder, final NotificationManager notificationManager, final int id) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                GlideUtils.requestBitmap(getBaseContext(), account_profile, new GlideUtils.BitmapCallback() {
                    @Override
                    public void onBitmapCallback(Bitmap account_bitmap) {
                        getPic2(account_bitmap, thumbnail, title, body, builder, notificationManager, id);
                    }

                    @Override
                    public void onBitmapFailure() {
                        getPic2(null, thumbnail, title, body, builder, notificationManager, id);
                    }
                });
            }
        });
    }

    private void getPic2(final Bitmap account_bitmap, final String thumbnail, final String title, final String body, final NotificationCompat.Builder builder, final NotificationManager notificationManager, final int id) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                GlideUtils.requestBitmap(getBaseContext(), thumbnail, new GlideUtils.BitmapCallback() {
                    @Override
                    public void onBitmapCallback(Bitmap thumbnail_bitmap) {
                        sendNotification(thumbnail_bitmap, title, body, account_bitmap, builder, notificationManager, id);
                    }

                    @Override
                    public void onBitmapFailure() {
                        sendNotification(null, title, body, account_bitmap, builder, notificationManager, id);
                    }
                });
            }
        });
    }

    private void sendNotification(Bitmap thumbnail_bitmap, String finalTitle, String finalBody, Bitmap account_bitmap, NotificationCompat.Builder builder, NotificationManager notificationManager, int finalId) {
        if (thumbnail_bitmap != null) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(finalTitle);
            bigPictureStyle.setSummaryText(finalBody);
            bigPictureStyle.bigPicture(thumbnail_bitmap);
            if (account_bitmap != null) {
                bigPictureStyle.bigLargeIcon(account_bitmap);
            }
            builder.setStyle(bigPictureStyle);
        }
        if (account_bitmap != null) {
            builder.setLargeIcon(account_bitmap);
        }


        Notification notification = builder.build();
        notificationManager.notify(finalId, notification);
    }

}
