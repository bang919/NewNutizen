package com.nutizen.nu.common;

import android.app.Application;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;
import com.mob.MobSDK;
import com.nutizen.nu.utils.LogUtils;


/**
 * Created by Administrator on 2017/11/7.
 */

public class MyApplication extends Application {

    private final String TAG = "MyApplication";
    private static Context mApplicationContext;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        catchException();
        initModules();
        mApplicationContext = getApplicationContext();
    }

    private void catchException() {//解决ShareSdk拦截Crash的bug
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
            }
        });
    }

    private void initModules() {
        LogUtils.isDebug = true;
        //ShareSdk
        MobSDK.init(this);
        //FileDownloader
        FileDownloader.setup(getApplicationContext());
    }

    public static Context getMyApplicationContext() {
        return mApplicationContext;
    }

}
