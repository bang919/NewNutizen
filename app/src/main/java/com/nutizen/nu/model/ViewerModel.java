package com.nutizen.nu.model;

import com.nutizen.nu.bean.request.LoginRequestBean;
import com.nutizen.nu.bean.request.ViewDetailReqBean;
import com.nutizen.nu.bean.response.ForgetPasswordResponse;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.bean.response.ResetPasswordResonseBean;
import com.nutizen.nu.bean.third.FacebookSdkBean;
import com.nutizen.nu.bean.third.RegisterFacebookRspBean;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.presenter.LoginPresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ViewerModel {
    public Observable<LoginResponseBean> login(LoginRequestBean loginRequestBean) {
        return HttpClient.getApiInterface()
                .login(loginRequestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean.DetailBean> getViewerDetail(String token) {
        return HttpClient.getApiInterface()
                .getViewerDetail("bearer " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalResBean> updateViewerDetail(final ViewDetailReqBean viewDetailReqBean, String token) {
        return HttpClient.getApiInterface()
                .updateViewerDetail(viewDetailReqBean, "bearer " + token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<NormalResBean>() {
                    @Override
                    public void accept(NormalResBean normalResBean) throws Exception {
                        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
                        LoginResponseBean.DetailBean accountMessageDetail = accountMessage.getDetail();
                        if (viewDetailReqBean.getAddress() != null) {
                            accountMessageDetail.setViewer_address(viewDetailReqBean.getAddress());
                        }
                        if (viewDetailReqBean.getBirthdate() != null) {
                            accountMessageDetail.setViewer_birthdate(viewDetailReqBean.getBirthdate());
                        }
                        if (viewDetailReqBean.getGender() != null) {
                            accountMessageDetail.setGender(viewDetailReqBean.getGender());
                        }
                        LoginPresenter.updateLoginMessage(accountMessage);
                    }
                });
    }

    public Observable<NormalResBean> updatePortrait(MultipartBody.Part file, String token) {

        return HttpClient.getApiInterface()
                .uploadPortrait(file, "bearer " + token)
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

    public Observable<RegisterFacebookRspBean> registerByFacebook(FacebookSdkBean facebookReqBean) {
        return HttpClient.getApiInterface()
                .registerByFacebook(facebookReqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponseBean> loginByFacebook(FacebookSdkBean facebookReqBean) {
        return HttpClient.getApiInterface()
                .loginByFacebook(facebookReqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
