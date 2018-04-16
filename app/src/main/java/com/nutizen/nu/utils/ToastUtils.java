package com.nutizen.nu.utils;

import android.content.Context;
import android.widget.Toast;

import com.nutizen.nu.common.MyApplication;


/**
 * Created by bigbang on 2017/5/19.
 * Toast提示工具类
 */

public class ToastUtils {

    private static Toast toast;
    private static Context mContext = MyApplication.getMyApplicationContext();

    /**
     * 短时间显示  Toast
     */
    public static void showShort(CharSequence sequence) {
        if (toast == null) {
            toast = Toast.makeText(mContext, sequence, Toast.LENGTH_SHORT);

        } else {
            toast.setText(sequence);
        }
        toast.show();

    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(int message) {
        if (null == toast) {
            toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(int message) {
        if (null == toast) {
            toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            //    toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 隐藏toast
     */
    public static void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
