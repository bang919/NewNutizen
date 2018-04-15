package com.nutizen.nu.presenter;

import android.content.Context;
import android.os.PowerManager;

import com.nutizen.nu.common.BasePresenter;

import static android.content.Context.POWER_SERVICE;

public class BasePlayerPresenter<V> extends BasePresenter<V> {

    private PowerManager.WakeLock mWakeLock;

    public BasePlayerPresenter(Context context, V view) {
        super(context, view);
    }

    public void keepScreen(Context context) {
        if (mWakeLock == null) {
            PowerManager pManager = ((PowerManager) context.getSystemService(POWER_SERVICE));
            mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "cn");
            mWakeLock.acquire();
        }
    }

    public void unkeepScreen() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
