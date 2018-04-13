package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.LoginResponseBean;

/**
 * Created by bigbang on 2018/3/30.
 */

public interface LoginView {
    void loginSuccess(LoginResponseBean loginResponseBean);
    void loginFailure(String errorMsg);
    void facebookSdkCallback(boolean onComplete);//ProgressDialog (setCancelable --> true) && dismiss --> onComplete
}
