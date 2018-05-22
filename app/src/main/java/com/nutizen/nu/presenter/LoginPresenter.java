package com.nutizen.nu.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.nutizen.nu.bean.request.LoginRequestBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.third.FacebookSdkBean;
import com.nutizen.nu.bean.third.RegisterFacebookRspBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.model.ViewerModel;
import com.nutizen.nu.utils.LogUtils;
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
    private ViewerModel mViewerModel;

    public LoginPresenter(Context context, LoginView view) {
        super(context, view);
        mViewerModel = new ViewerModel();
    }

    public void login(final String usernameEmail, String password) {
        final String observerTag = getClass().getName() + "login";
        subscribeNetworkTask(observerTag, mViewerModel.login(new LoginRequestBean(usernameEmail, password))
                .flatMap(new Function<LoginResponseBean, ObservableSource<LoginResponseBean>>() {
                    @Override
                    public ObservableSource<LoginResponseBean> apply(final LoginResponseBean loginResponseBean) throws Exception {
                        return mViewerModel.getViewerDetail(loginResponseBean.getViewer_token()).map(new Function<LoginResponseBean.DetailBean, LoginResponseBean>() {
                            @Override
                            public LoginResponseBean apply(LoginResponseBean.DetailBean detailBean) throws Exception {
                                loginResponseBean.setDetail(detailBean);
                                return loginResponseBean;
                            }
                        });
                    }
                }), new MyObserver<LoginResponseBean>() {
            @Override
            public void onMyNext(LoginResponseBean loginResponseBean) {
                updateLoginMessage(loginResponseBean);
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
                LogUtils.d(TAG, "onCancel: " + platform.getName());
            }
        });
        platform.showUser(null);
    }

    private void loginByFacebookAccessToken(final FacebookSdkBean facebookSdkBean) {
        final String observerTag = getClass().getName() + "loginByFacebookAccessToken";
        Observable<LoginResponseBean> loginFacebookRspBeanObservable = mViewerModel.loginByFacebook(facebookSdkBean)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<RegisterFacebookRspBean>>() {
                    @Override
                    public ObservableSource<RegisterFacebookRspBean> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<RegisterFacebookRspBean>>() {
                            @Override
                            public ObservableSource<RegisterFacebookRspBean> apply(Throwable throwable) throws Exception {
                                return mViewerModel.registerByFacebook(facebookSdkBean);
                            }
                        });
                    }
                });
        subscribeNetworkTask(observerTag, loginFacebookRspBeanObservable, new MyObserver<LoginResponseBean>() {
            @Override
            public void onMyNext(LoginResponseBean loginFacebookRspBean) {
                LoginResponseBean loginResponseBean = new LoginResponseBean();
                LoginResponseBean.DetailBean detailBean = new LoginResponseBean.DetailBean();

                loginResponseBean.setStatus(loginFacebookRspBean.isStatus());
                loginResponseBean.setViewer_id(loginFacebookRspBean.getViewer_id());
                loginResponseBean.setViewer_token(loginFacebookRspBean.getViewer_token());
                loginResponseBean.setViewer_token_expiry_date(loginFacebookRspBean.getViewer_token_expiry_date());
                loginResponseBean.setViewer_static_token(loginFacebookRspBean.getViewer_static_token());
                loginResponseBean.setIs_contributor(false);

                detailBean.setViewer_username(facebookSdkBean.getName());
                detailBean.setViewer_email(facebookSdkBean.getEmail());
                detailBean.setViewer_firstname(facebookSdkBean.getFirst_name());
                detailBean.setViewer_lastname(facebookSdkBean.getLast_name());
                detailBean.setViewer_gender(facebookSdkBean.getGender() != null && facebookSdkBean.getGender().equals("female") ? 1 : 0);
                detailBean.setViewer_country(facebookSdkBean.getLocale());
                detailBean.setIs_third(1);
                detailBean.setViewer_thumbnail(facebookSdkBean.getPicture());
                if (!TextUtils.isEmpty(facebookSdkBean.getEmail())) {
                    String email = facebookSdkBean.getEmail();
                    detailBean.setViewer_nickname(email.split("@")[0]);
                } else if (!TextUtils.isEmpty(facebookSdkBean.getName())) {
                    detailBean.setViewer_nickname(facebookSdkBean.getName());
                } else {
                    detailBean.setViewer_nickname(facebookSdkBean.getFirst_name() + " " + facebookSdkBean.getLast_name());
                }

                loginResponseBean.setDetail(detailBean);
                updateLoginMessage(loginResponseBean);
                mView.loginSuccess(loginResponseBean);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.loginFailure(errorMessage);
            }
        });
    }

    /**
     * Save login message
     */
    public static void updateLoginMessage(LoginResponseBean loginResponseBean) {
        SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN, loginResponseBean);
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
