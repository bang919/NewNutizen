package com.nutizen.nu.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.nutizen.nu.bean.response.ForgetPasswordResponse;
import com.nutizen.nu.bean.response.ResetPasswordResonseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ViewerModel;
import com.nutizen.nu.view.NormalView;

/**
 * Created by bigbang on 2018/3/30.
 */

public class ForgetPasswordPresenter extends BasePresenter<NormalView> {

    private ViewerModel mViewerModel;

    public ForgetPasswordPresenter(Context context, NormalView view) {
        super(context, view);
        mViewerModel = new ViewerModel();
    }

    public void searchEmailToRestPassword(String email) {
        final String observerTag = getClass().getName() + "searchEmailToRestPassword";
        subscribeNetworkTask(observerTag, mViewerModel.searchEmailToRestPassword(email), new MyObserver<ResetPasswordResonseBean>() {
            @Override
            public void onMyNext(ResetPasswordResonseBean resetPasswordResonseBean) {
                if (resetPasswordResonseBean.status && resetPasswordResonseBean.err == null) {
                    mView.onSuccess();
                } else {
                    ResetPasswordResonseBean.ErrBean err = resetPasswordResonseBean.err;
                    String errorMsg = "Reset password error, please try latter.";
                    if (err != null && err.message != null && err.message.size() > 0) {
                        errorMsg = err.message.get(0);
                    }
                    mView.onFailure(errorMsg);
                }
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }

    public void resetPassword(String password, String digit) {
        final String observerTag = getClass().getName() + "searchEmailToRestPassword";
        subscribeNetworkTask(observerTag, mViewerModel.resetPassword(password, digit), new MyObserver<ForgetPasswordResponse>() {
            @Override
            public void onMyNext(ForgetPasswordResponse forgetPasswordResponse) {
                if (forgetPasswordResponse.status && forgetPasswordResponse.err == null) {
                    mView.onSuccess();
                } else {
                    ForgetPasswordResponse.ErrBean err = forgetPasswordResponse.err;
                    String errorMsg = "Reset password error, please try latter.";
                    if (err != null && !TextUtils.isEmpty(err.message)) {
                        errorMsg = err.message;
                    }
                    mView.onFailure(errorMsg);
                }
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }
}
