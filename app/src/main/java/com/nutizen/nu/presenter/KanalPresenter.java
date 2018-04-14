package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.KanalModel;
import com.nutizen.nu.view.KanalView;

public class KanalPresenter extends BasePresenter<KanalView> {

    private KanalModel mKanalModel;

    public KanalPresenter(Context context, KanalView view) {
        super(context, view);
        mKanalModel = new KanalModel();
    }

    public void requestKanals(String contributorName) {
        String observerTag = getClass().getName() + "requestKanals";
        subscribeNetworkTask(observerTag, mKanalModel.requestKanals(contributorName).retry(3), new MyObserver<KanalRspBean>() {
            @Override
            public void onMyNext(KanalRspBean kanalRspBean) {
                mView.onKanalsResponse(kanalRspBean);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }
}
