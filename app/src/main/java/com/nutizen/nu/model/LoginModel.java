package com.nutizen.nu.model;

import com.nutizen.nu.bean.request.LoginRequestBean;
import com.nutizen.nu.bean.response.ForgetPasswordResponse;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.ResetPasswordResonseBean;
import com.nutizen.nu.bean.third.FacebookSdkBean;
import com.nutizen.nu.bean.third.LoginFacebookRspBean;
import com.nutizen.nu.bean.third.RegisterFacebookRspBean;
import com.nutizen.nu.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/3/6.
 */

public class LoginModel {
    public Observable<LoginResponseBean> login(LoginRequestBean loginRequestBean) {
        return HttpClient.getApiInterface()
                .login(loginRequestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResetPasswordResonseBean> searchEmailToRestPassword(String email) {
        String json = "{\"email\":\"" + email + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        return HttpClient.getApiInterface()
                .searchEmailToRestPassword(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ForgetPasswordResponse> resetPassword(String password, String code) {
        String json = "{\"password\":\"" + password + "\",\"digit\":\"" + code + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        return HttpClient.getApiInterface()
                .forgetpassword(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RegisterFacebookRspBean> registerByFacebook(FacebookSdkBean facebookReqBean){
        return HttpClient.getApiInterface()
                .registerByFacebook(facebookReqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginFacebookRspBean> loginByFacebook(FacebookSdkBean facebookReqBean){
        return HttpClient.getApiInterface()
                .loginByFacebook(facebookReqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
