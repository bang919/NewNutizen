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
                        return mViewerModel.getViewerDetail(loginResponseBean);
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
                                return mViewerModel.registerByFacebook(facebookSdkBean);//这里应该初始化信息
                            }
                        });
                    }
                })
                .flatMap(new Function<LoginResponseBean, ObservableSource<LoginResponseBean>>() {
                    @Override
                    public ObservableSource<LoginResponseBean> apply(LoginResponseBean loginResponseBean) throws Exception {
                        return mViewerModel.getViewerDetail(loginResponseBean);
                    }
                });

        subscribeNetworkTask(observerTag, loginFacebookRspBeanObservable, new MyObserver<LoginResponseBean>() {
            @Override
            public void onMyNext(LoginResponseBean loginResponseBean) {
                LoginResponseBean.DetailBean detail = loginResponseBean.getDetail();
                //没有图片就先显示facebook图片吧
                if (TextUtils.isEmpty(detail.getViewer_thumbnail())) {
                    detail.setViewer_thumbnail(facebookSdkBean.getPicture());
                }
                detail.setViewer_email(facebookSdkBean.getEmail());
                detail.setIs_third(1);
                loginResponseBean.setDetail(detail);
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
