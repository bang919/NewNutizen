package com.nutizen.nu.server;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.utils.SPUtils;
import com.nutizen.nu.utils.SubscribeNotificationUtile;

/**
 * Created by bigbang on 2018/3/22.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        boolean subscribeImportant = (boolean) SPUtils.get(this, Constants.NOTIFICATION_IMPORTANT, false);
        boolean subscribeHomeBanner = (boolean) SPUtils.get(this, Constants.NOTIFICATION_HOME_BANNER, false);
        boolean subscribePilihanEditor = (boolean) SPUtils.get(this, Constants.NOTIFICATION_PILIHAN_EDITOR, false);
        boolean subscribeKanal = (boolean) SPUtils.get(this, Constants.NOTIFICATION_KANAL, false);
        boolean subscribeLive = (boolean) SPUtils.get(this, Constants.NOTIFICATION_LIVE, false);
        if (subscribeImportant) {
            SubscribeNotificationUtile.subscribeImportant();
        }
        if (subscribeHomeBanner) {
            SubscribeNotificationUtile.subscribeHomeBanner();
        }
        if (subscribePilihanEditor) {
            SubscribeNotificationUtile.subscribePilihanEditor();
        }
        if (subscribeKanal) {
            SubscribeNotificationUtile.subscribeAllContributorsVod(this);
        }
        if (subscribeLive) {
            SubscribeNotificationUtile.subscribeAllContributorsLive(this);
        }
    }
}
