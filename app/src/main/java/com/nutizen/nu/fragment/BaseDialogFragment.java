package com.nutizen.nu.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;

public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment {

    protected P mPresenter;
    private boolean hadMeet;//是否已启动过。。用来修复一个bug：DialogFragment打开Activity，再返回DialogFragment会出现Fragment启动动画

    public BaseDialogFragment() {
        setStyle(0, R.style.FullScreenLightDialog);
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

    @Override
    public void onResume() {
        super.onResume();
        //修复一个bug：DialogFragment打开Activity，再返回DialogFragment会出现Fragment启动动画
        if (hadMeet) {//已启动过
            getDialog().getWindow().setWindowAnimations(R.style.animate_dialog_exit);
        }
        hadMeet = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable());
        getDialog().getWindow().setWindowAnimations(R.style.animate_dialog_enter_and_exit);

        View rootView = inflater.inflate(getLayout(), container, false);
        onViewPagerFragmentCreate(rootView);
        return rootView;
    }


    protected void onViewPagerFragmentCreate(View rootView) {
        mPresenter = initPresenter();
        initView(rootView);
        initEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
    }
}
