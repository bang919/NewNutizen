package com.nutizen.nu.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.LoginActivity;
import com.nutizen.nu.presenter.LoginPresenter;

import java.util.Map;

/**
 * Created by bigbang on 2017/5/2.
 * activity 基类
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    protected P mPresenter;


    /**
     * 获取xml布局
     */
    protected abstract int getLayout();

    /**
     * 状态条颜色,如果不需要状态条，重写这个方法，并
     * return Constants.NULL_COLOR
     */
    public int getBarColor() {
        return R.color.colorPrimary;
    }

    /**
     * 初始化presenter
     */
    protected abstract P initPresenter();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBarTransparent();
        setContentView(getLayout());
        setSystemBarColor(getBarColor());
        mPresenter = initPresenter();
        initView();
        initData();
        initEvent();
    }

    /**
     * （全屏模式）沉浸式状态栏
     */
    public void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 着色模式（改变状态栏颜色）
     */
    public void setSystemBarColor(int colorResource) {
        if (colorResource == Constants.NULL_COLOR) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(colorResource));
            ViewGroup mContentView = findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        } else {
            Window window = getWindow();
            ViewGroup mContentView = findViewById(Window.ID_ANDROID_CONTENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight();
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                    //不预留系统空间
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    lp.topMargin += statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                    statusBarView.getLayoutParams().height == statusBarHeight) {
                //避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(getResources().getColor(colorResource));
                return;
            }
            statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(getResources().getColor(colorResource));
            //向 ContentView 中添加假 View
            mContentView.addView(statusBarView, 0, lp);
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 显示软键盘
     *
     * @param editText
     */
    protected void showSoftKeyboard(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 200L);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 跳转到新的activity
     *
     * @param activity 需要打开的activity
     */
    public void jumpToActivity(Class<? extends Activity> activity) {
        jumpToActivity(null, activity);
    }


    /**
     * 跳转到新的activity,带参数
     *
     * @param activity 需要打开的activity
     */
    public void jumpToActivity(Map<String, Object> map, Class<? extends Activity> activity) {
        Intent intent = new Intent();
        // 把返回数据存入Intent
        if (map != null) {
            for (String key : map.keySet()) {
                if (map.get(key) instanceof String) {
                    intent.putExtra(key, String.valueOf(map.get(key)));
                } else if (map.get(key) instanceof Long) {
                    intent.putExtra(key, Long.valueOf(String.valueOf(map.get(key))));
                } else if (map.get(key) instanceof Double) {
                    intent.putExtra(key, Double.valueOf(String.valueOf(map.get(key))));
                } else if (map.get(key) instanceof Integer) {
                    intent.putExtra(key, Integer.valueOf(String.valueOf(map.get(key))));
                } else if (map.get(key) instanceof Float) {
                    intent.putExtra(key, Float.valueOf(String.valueOf(map.get(key))));
                } else if (map.get(key) instanceof Boolean) {
                    intent.putExtra(key, Boolean.valueOf(String.valueOf(map.get(key))));
                } else if (map.get(key) instanceof Bundle) {
                    intent.putExtras((Bundle) map.get(key));
                }
            }
        }
        intent.setClass(this, activity);
        this.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
    }

    public void logout() {
        LoginPresenter.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
