package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.model.TvModel;
import com.nutizen.nu.view.BaseLiveView;

import java.util.ArrayList;

public class BaseLivePresetner extends BasePlayerPresenter<BaseLiveView> {

    private TvModel mTvModel;

    public BaseLivePresetner(Context context, BaseLiveView view) {
        super(context, view);
        mTvModel = new TvModel();
    }

    public void requestTvs() {
        String observerTag = getClass().getName() + "requestTvs";
        subscribeNetworkTask(observerTag, mTvModel.requestTvs(), new MyObserver<ArrayList<LiveResponseBean>>() {
            @Override
            public void onMyNext(ArrayList<LiveResponseBean> liveResponseBeans) {
                ArrayList<LiveResponseBean> datas = new ArrayList<>();
                for (LiveResponseBean liveResponseBean : liveResponseBeans) {
                    if (liveResponseBean.getCategory().equals("tv")) {
                        datas.add(liveResponseBean);
                    }
                }
                mView.onLivesResponse(datas);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onLivesFailure(errorMessage);
            }
        });
    }

    public void requestRadios() {
        String observerTag = getClass().getName() + "requestRadios";
        subscribeNetworkTask(observerTag, mTvModel.requestTvs(), new MyObserver<ArrayList<LiveResponseBean>>() {
            @Override
            public void onMyNext(ArrayList<LiveResponseBean> liveResponseBeans) {
                ArrayList<LiveResponseBean> datas = new ArrayList<>();
                for (LiveResponseBean liveResponseBean : liveResponseBeans) {
                    if (liveResponseBean.getCategory().equals("radio")) {
                        datas.add(liveResponseBean);
                    }
                }
                mView.onLivesResponse(datas);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onLivesFailure(errorMessage);
            }
        });
    }
}
