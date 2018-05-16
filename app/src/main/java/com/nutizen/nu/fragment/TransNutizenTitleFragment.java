package com.nutizen.nu.fragment;

import android.os.Bundle;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;

public abstract class TransNutizenTitleFragment<P extends BasePresenter> extends TextTitleFragment<P> {

    /**
     * @param cls              必须是TransNutizenTitleFragment的子类fragment
     * @param startFromMainAct 是否从MainActivity启动，用来switch MainActivity的动画
     * @return
     */
    public static <T extends TransNutizenTitleFragment> T getInstance(Class<T> cls, boolean startFromMainAct, Bundle bundle) {
        try {
            T transTitleFragment = cls.newInstance();
            return getInstance(transTitleFragment, startFromMainAct, bundle);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_trans_nu_title;
    }

}
