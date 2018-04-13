package com.nutizen.nu.presenter;

import android.content.Context;
import android.util.Log;

import com.nutizen.nu.bean.request.LoginRequestBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.third.FacebookSdkBean;
import com.nutizen.nu.bean.third.LoginFacebookRspBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.model.LoginModel;
import com.nutizen.nu.utils.SPUtils;
import com.nutizen.nu.view.LoginView;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by bigbang on 2018/3/30.
 */

public class LoginPresenter extends BasePresenter<LoginView> {

    public final String TAG = "LoginPresenter";
    private LoginModel mLoginModel;

    public LoginPresenter(Context context, LoginView view) {
        super(context, view);
        mLoginModel = new LoginModel();
    }

    public void login(final String usernameEmail, String password) {
        Observable<LoginResponseBean> loginObservable = mLoginModel.login(new LoginRequestBean(usernameEmail, password));
        final String observerTag = getClass().getName() + "login";
        subscribeNetworkTask(observerTag, loginObservable, new MyObserver<LoginResponseBean>() {
            @Override
            public void onMyNext(LoginResponseBean loginResponseBean) {
                SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN, loginResponseBean);
                SPUtils.put(MyApplication.getMyApplicationContext(), Constants.USERNAME, usernameEmail);
                mView.loginSuccess(loginResponseBean);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.loginFailure(errorMessage);
            }
        });
    }

    public void loginByFacebook(final Platform platform) {
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
        platform.SSOSetting(false);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                mView.facebookSdkCallback(true);
                String simpleName = platform.getClass().getSimpleName();
                switch (simpleName) {
                    case "Facebook":
                        String facebookAccessToken = platform.getDb().getToken();
                        loginByFacebookAccessToken(new FacebookSdkBean(hashMap, facebookAccessToken));
                        break;
                    default:
                        mView.loginFailure("Unknow error.");
                        break;
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                mView.facebookSdkCallback(false);
                mView.loginFailure(throwable.getLocalizedMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                mView.facebookSdkCallback(false);
                Log.d(TAG, "onCancel: " + platform.getName());
            }
        });
        platform.showUser(null);
    }

    private void loginByFacebookAccessToken(final FacebookSdkBean facebookSdkBean) {
        final String observerTag = getClass().getName() + "loginByFacebookAccessToken";
        Observable<LoginFacebookRspBean> loginFacebookRspBeanObservable = mLoginModel.loginByFacebook(facebookSdkBean)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                return mLoginModel.registerByFacebook(facebookSdkBean);
                            }
                        });
                    }
                });
        subscribeNetworkTask(observerTag, loginFacebookRspBeanObservable, new MyObserver<LoginFacebookRspBean>() {
            @Override
            public void onMyNext(LoginFacebookRspBean loginFacebookRspBean) {
                LoginResponseBean loginResponseBean = new LoginResponseBean();
                loginResponseBean.setToken(loginFacebookRspBean.getViewer_token());
                loginResponseBean.setEmail(facebookSdkBean.getEmail());
                loginResponseBean.setNickname(facebookSdkBean.getName());
                loginResponseBean.setUsername(facebookSdkBean.getFirst_name()+" "+facebookSdkBean.getLast_name());
                LoginResponseBean.ThumbnailBean thumbnail = new LoginResponseBean.ThumbnailBean();
                thumbnail.setDefaultX(facebookSdkBean.getPicture());
                loginResponseBean.setThumbnail(thumbnail);
                mView.loginSuccess(loginResponseBean);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.loginFailure(errorMessage);
            }
        });
    }

    /**
     * Get the account's user/token .
     */
    public static LoginResponseBean getAccountMessage() {
        try {
            LoginResponseBean loginResBean = SPUtils.getObject(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN);
            return loginResBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        logout();//走到这里就已经失败了，所以logout()
        return null;
    }

    /**
     * Logout a RegisterAndLoginResBean account
     */
    public static void logout() {
        SPUtils.remove(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN);
    }
}
