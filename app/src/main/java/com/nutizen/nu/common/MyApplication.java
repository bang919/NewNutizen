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

    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initModules();
        mApplicationContext = getApplicationContext();
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
