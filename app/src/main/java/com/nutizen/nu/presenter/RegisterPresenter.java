package com.nutizen.nu.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.nutizen.nu.bean.response.RegisterResponse;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.utils.SPUtils;
import com.nutizen.nu.view.NormalView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by bigbang on 2018/3/30.
 */

public class RegisterPresenter extends BasePresenter<NormalView> {

    public RegisterPresenter(Context context, NormalView view) {
        super(context, view);
    }

    public void registerAccount(final String username, String password, String email) {
        final String observerTag = getClass().getName() + "login";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"email\":\"" + email + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Observable<RegisterResponse> registerResponseObservable = HttpClient.getApiInterface().registerAccount(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        subscribeNetworkTask(observerTag, registerResponseObservable, new MyObserver<RegisterResponse>() {
            @Override
            public void onMyNext(RegisterResponse registerResponse) {
                if (registerResponse.status && registerResponse.err == null) {
                    SPUtils.put(MyApplication.getMyApplicationContext(), Constants.USERNAME, username);
                    mView.onSuccess();
                } else {
                    RegisterResponse.ErrBean err = registerResponse.err;
                    String errorMsg = "Register account error, please try latter.";
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
