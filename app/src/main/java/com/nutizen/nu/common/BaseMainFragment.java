package com.nutizen.nu.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutizen.nu.activity.MainActivity;

import java.util.Map;

/**
 * Created by bigbang on 2018/4/11.
 */

public abstract class BaseMainFragment<P extends BasePresenter> extends Fragment {

    protected P mPresenter;
    private Activity mActivity;
    private boolean hadGoneFragmentCreate;
    private boolean hadRequestData;//第一次进来，请求数据

    protected void onViewPagerFragmentResume() {
    }

    protected void onViewPagerFragmentPause() {
    }

    /**
     * 获取xml布局
     */
    protected abstract int getLayout();

    /**
     * 初始化presenter
     */
    protected abstract P initPresenter();

    /**
     * 初始化视图
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 请求数据，有两种情况跳用：
     * 1。第一次进这个BaseFragment
     * 2。下拉刷新
     */
    public abstract void refreshData();

    /**
     * 数据请求完成
     */

    public void onDataRefreshFinish() {
        onDataRefreshFinish(false);
    }

    /**
     * 数据请求完成
     *
     * @param success 是否请求成功。若请求成功，设置hadRequestData为true，下一次进来Fragment就不会自动请求了
     */
    public void onDataRefreshFinish(boolean success) {
        if (!hadRequestData && success) {
            hadRequestData = true;
        }
        setRefreshing(false);
    }

    /**
     * 设置SwipeRefreshLayout是否可滚动
     *
     * @param enable
     */
    public void setRefreshEnable(boolean enable) {
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).setRefreshEnable(enable);
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).setRefreshing(refreshing);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        onViewPagerFragmentCreate(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    /**
     * 可见/不可见
     *
     * @param isVisibleToUser false为不可见，true为可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mPresenter != null) {
                mPresenter.cancelAllRequest();
            }
            onViewPagerFragmentPause();
        } else {
            if (!hadGoneFragmentCreate) {
                return;
            }
            onViewPagerFragmentResume();
            if (!hadRequestData) {
                initRequestData();
            }
        }
    }

    protected void onViewPagerFragmentCreate(View rootView) {
        hadGoneFragmentCreate = true;
        mPresenter = initPresenter();
        initView(rootView);
        initEvent();
        if (getUserVisibleHint()) {
            onViewPagerFragmentResume();
            initRequestData();
        }
    }

    private void initRequestData() {
        setRefreshing(true);
        refreshData();
    }

    @Nullable
    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
        setUserVisibleHint(false);
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
        intent.setClass(mActivity, activity);
        this.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
    }
}