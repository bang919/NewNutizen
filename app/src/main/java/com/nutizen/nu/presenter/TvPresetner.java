package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.TvModel;
import com.nutizen.nu.view.TvView;

import java.util.ArrayList;

public class TvPresetner extends BasePresenter<TvView> {

    private TvModel mTvModel;

    public TvPresetner(Context context, TvView view) {
        super(context, view);
        mTvModel = new TvModel();
    }

    public void requestTvs() {
        String observerTag = getClass().getName() + "requestTvs";
        subscribeNetworkTask(observerTag, mTvModel.requestTvs(), new MyObserver<ArrayList<LiveResponseBean>>() {
            @Override
            public void onMyNext(ArrayList<LiveResponseBean> liveResponseBeans) {
                mView.onTvsResponse(liveResponseBeans);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onTvsFailure(errorMessage);
            }
        });
    }
}
