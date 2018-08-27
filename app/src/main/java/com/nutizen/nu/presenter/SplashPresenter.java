package com.nutizen.nu.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.AdvertisementBean;
import com.nutizen.nu.bean.response.ContentBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.model.SplashModel;
import com.nutizen.nu.utils.DynamicLinkUtil;
import com.nutizen.nu.utils.FileUtils;
import com.nutizen.nu.utils.LogUtils;
import com.nutizen.nu.utils.SPUtils;
import com.nutizen.nu.utils.SubscribeNotificationUtile;
import com.nutizen.nu.view.SplashView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by bigbang on 2018/3/29.
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    private final static String TAG = "SplashPresenter";
    private SplashModel mSplashModel;

    public SplashPresenter(Context context, SplashView view) {
        super(context, view);
        mSplashModel = new SplashModel();
    }

    public void handleBackgroundPic(ImageView imageView) throws IOException {
        String diskCacheDir = FileUtils.getDiskCacheDir(MyApplication.getMyApplicationContext());
        File splashImagePath = new File(diskCacheDir + "/splashImage");
        downloadBackgroundPic(splashImagePath);
        Glide.with(imageView).load(splashImagePath)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CenterCrop()))
                .transition(new DrawableTransitionOptions().crossFade()).into(imageView);
    }

    private void downloadBackgroundPic(final File splashImagePath) throws IOException {
        LogUtils.d(TAG, "start downloadBackgroundPic  -- " + splashImagePath.getAbsolutePath());
        final String splashPicSharedPreferencesUrl = (String) SPUtils.get(MyApplication.getMyApplicationContext(), Constants.SPLASH_PIC_URL, "");
        //如果splashImagePath还没有初始化，复制splash_default到目标文件
        if (TextUtils.isEmpty(splashPicSharedPreferencesUrl) || !splashImagePath.exists()) {
            LogUtils.d(TAG, "开始复制splash_default");
            if (!splashImagePath.exists()) {
                splashImagePath.createNewFile();
            }
            Drawable drawable = MyApplication.getMyApplicationContext().getResources().getDrawable(R.mipmap.splash_default);
            InputStream is = FileUtils.drawable2InputStream(drawable);
            OutputStream os = new FileOutputStream(splashImagePath);
            byte bt[] = new byte[1024];
            int c;
            while ((c = is.read(bt)) > 0) {
                os.write(bt, 0, c);
            }
            is.close();
            os.close();
            SPUtils.put(MyApplication.getMyApplicationContext(), Constants.SPLASH_PIC_URL, Constants.SPLASH_PIC_URL);
            LogUtils.d(TAG, "完成复制splash_default");
        }
        //下载最新Splash图片
        mSplashModel.getAdvertisement().flatMap(new Function<AdvertisementBean, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(AdvertisementBean advertisementBean) throws Exception {
                List<AdvertisementBean.PictureArrayBean> picture_array = advertisementBean.getPicture_array();
                if (picture_array != null && picture_array.size() > 0) {
                    String pictureUrl = picture_array.get((int) (Math.random() * picture_array.size())).getUrl();
                    LogUtils.d(TAG, "请求Splash图片成功，开始下载图片: " + pictureUrl);
                    if (!pictureUrl.equals(splashPicSharedPreferencesUrl) || !splashImagePath.exists()) {
                        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.SPLASH_PIC_URL, pictureUrl);
                        return mSplashModel.downloadAdvertisement(pictureUrl);
                    }
                }
                throw new Exception("No need to refresh advertisement pic");
            }
        }).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (responseBody != null) {
                    try {
                        FileUtils.saveResponseBody(responseBody, splashImagePath);
                        if (mView != null) {//有可能下载完的时候已经退出SplashActivity了
                            mView.getNewPic();
                        }
                        LogUtils.d(TAG, "save Splash pic success");
                    } catch (IOException e) {
                        LogUtils.d(TAG, "save Splash pic failure");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void checkDynamicLinkAndNotification(final Activity activity, final Intent intent, final long waitTime) {
        Observable<ContentBean> notificationOb = Observable.create(new ObservableOnSubscribe<ContentBean>() {
            @Override
            public void subscribe(final ObservableEmitter<ContentBean> e) throws Exception {
                SubscribeNotificationUtile.checkFCMNotification(activity, intent, new SubscribeNotificationUtile.CheckFCMNotificationCallback() {
                    @Override
                    public void onFCMNotificationMovie(ContentResponseBean.SearchBean contentBean) {
                        ContentBean value = new ContentBean();
                        value.setType(contentBean.getType());
                        value.setMovieBean(contentBean);
                        e.onNext(value);
                    }

                    @Override
                    public void onFCMNotificationLive(LiveResponseBean liveBean) {
                        ContentBean value = new ContentBean();
                        value.setType(liveBean.getType());
                        value.setLiveBean(liveBean);
                        e.onNext(value);
                    }

                    @Override
                    public void onFCMNotificationFailure() {
                        e.onNext(new ContentBean());
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());

        Observable<ContentBean> dynamicOb = Observable.create(new ObservableOnSubscribe<ContentBean>() {
            @Override
            public void subscribe(final ObservableEmitter<ContentBean> e) throws Exception {
                DynamicLinkUtil.checkDynamicLink(activity, intent, new DynamicLinkUtil.DynamicLinkUtilCallback() {
                    @Override
                    public void onDynamicMovie(ContentResponseBean.SearchBean contentBean) {
                        ContentBean value = new ContentBean();
                        value.setType(contentBean.getType());
                        value.setMovieBean(contentBean);
                        e.onNext(value);
                    }

                    @Override
                    public void onDynamicLive(LiveResponseBean liveBean) {
                        ContentBean value = new ContentBean();
                        value.setType(liveBean.getType());
                        value.setLiveBean(liveBean);
                        e.onNext(value);
                    }

                    @Override
                    public void onDynamicFailure() {
                        e.onNext(new ContentBean());
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());

        Observable<Boolean> checkLoginOb = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(checkLogin());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());


        Observable<Long> waitOb = Observable.timer(waitTime, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
        zipObservables(notificationOb, dynamicOb, checkLoginOb, waitOb);
    }

    private void zipObservables(Observable<ContentBean> notificationOb, Observable<ContentBean> dynamicOb, Observable<Boolean> checkLoginOb, Observable<Long> waitOb) {
        Observable<SplashContentBean> zip = Observable.zip(notificationOb, dynamicOb, checkLoginOb, waitOb, new Function4<ContentBean, ContentBean, Boolean, Long, SplashContentBean>() {
            @Override
            public SplashContentBean apply(ContentBean contentBean, ContentBean contentBean2, Boolean aBoolean, Long l) throws Exception {
                SplashContentBean splashContentBean = new SplashContentBean();
                splashContentBean.setLogin(aBoolean);
                if (contentBean.getMovieBean() != null || contentBean.getLiveBean() != null) {
                    splashContentBean.setContentBean(contentBean);
                } else if (contentBean2.getMovieBean() != null || contentBean2.getLiveBean() != null) {
                    splashContentBean.setContentBean(contentBean2);
                }
                return splashContentBean;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        subscribeNetworkTask(getClass().getName().concat("zipObservables"), zip, new MyObserver<SplashContentBean>() {
            @Override
            public void onMyNext(SplashContentBean splashContentBean) {
                mView.isLogin(splashContentBean.isLogin());
                if (splashContentBean.getContentBean() != null && splashContentBean.getContentBean().getType().equals("movie")) {
                    ContentResponseBean.SearchBean movieBean = splashContentBean.getContentBean().getMovieBean();
                    mView.jumpToContentPlayerActivity(movieBean);
                } else if (splashContentBean.getContentBean() != null && splashContentBean.getContentBean().getType().equals("live")) {
                    LiveResponseBean liveBean = splashContentBean.getContentBean().getLiveBean();
                    mView.jumpToLivePlayerActivity(liveBean);
                }
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.isLogin(checkLogin());
            }
        });
    }

    private boolean checkLogin() {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        return accountMessage != null;
    }

    class SplashContentBean {
        private boolean isLogin;
        private ContentBean contentBean;

        public boolean isLogin() {
            return isLogin;
        }

        public void setLogin(boolean login) {
            isLogin = login;
        }

        public ContentBean getContentBean() {
            return contentBean;
        }

        public void setContentBean(ContentBean contentBean) {
            this.contentBean = contentBean;
        }
    }
}
