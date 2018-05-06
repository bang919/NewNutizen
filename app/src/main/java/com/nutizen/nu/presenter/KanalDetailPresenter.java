package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ContributorModel;
import com.nutizen.nu.model.FavouriteModel;
import com.nutizen.nu.utils.DialogUtils;
import com.nutizen.nu.view.KanalDetailView;

import java.util.ArrayList;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

public class KanalDetailPresenter extends BasePresenter<KanalDetailView> {

    private ContributorModel mContributorModel;
    private FavouriteModel mFavouriteModel;

    public KanalDetailPresenter(Context context, KanalDetailView view) {
        super(context, view);
        mContributorModel = new ContributorModel();
        mFavouriteModel = new FavouriteModel();
    }

    public void getDatas(final KanalRspBean.SearchBean kanalBean) {

        Observable<ContentResponseBean> contentResponseBeanObservable = mContributorModel
                .searchContentByContributor(kanalBean.getViewer_id(), 9999, null)
                .doOnNext(new Consumer<ContentResponseBean>() {
                    @Override
                    public void accept(ContentResponseBean contentResponseBean) throws Exception {
                        mView.onContentResult(contentResponseBean.getSearch());
                    }
                });

        Observable<ArrayList<LiveResponseBean>> liveResponseBeanObservable = mContributorModel
                .searchLiveByContributor(kanalBean.getViewer_id(), 1, 9999, null)
                .doOnNext(new Consumer<ArrayList<LiveResponseBean>>() {
                    @Override
                    public void accept(ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                        mView.onLiveResullt(liveResponseBeans);
                    }
                });

        Observable<Boolean> checkFollowObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                if (LoginPresenter.getAccountMessage() != null) {
                    e.onError(new Exception("had login"));
                } else {
                    e.onNext(false);
                }
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Boolean>>() {
            @Override
            public ObservableSource<? extends Boolean> apply(Throwable throwable) throws Exception {
                return mFavouriteModel.getFavourites().doOnNext(new Consumer<TreeMap<String, ArrayList<FavouriteRspBean>>>() {
                    @Override
                    public void accept(TreeMap<String, ArrayList<FavouriteRspBean>> maps) throws Exception {
                        ArrayList<FavouriteRspBean> favouriteRspBeans = maps.get(kanalBean.getType());
                        boolean isFavourite = false;
                        if (favouriteRspBeans != null) {
                            for (FavouriteRspBean favouriteRspBean : favouriteRspBeans) {
                                if (favouriteRspBean.getContent_id() == kanalBean.getViewer_id()) {
                                    isFavourite = true;
                                    break;
                                }
                            }
                        }
                        mView.isFollow(isFavourite);
                    }
                }).map(new Function<TreeMap<String, ArrayList<FavouriteRspBean>>, Boolean>() {
                    @Override
                    public Boolean apply(TreeMap<String, ArrayList<FavouriteRspBean>> stringArrayListTreeMap) throws Exception {
                        return true;
                    }
                });
            }
        });

        Observable<String> zip = Observable.zip(contentResponseBeanObservable, checkFollowObservable, liveResponseBeanObservable, new Function3<ContentResponseBean, Boolean, ArrayList<LiveResponseBean>, String>() {
            @Override
            public String apply(ContentResponseBean contentResponseBean, Boolean aBoolean, ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                return "success";
            }
        }).retry(2);

        String observerTag = getClass().getName() + "getDatas";
        subscribeNetworkTask(observerTag, zip, new MyObserver<String>() {
            @Override
            public void onMyNext(String s) {
                mView.onSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }

    public boolean checkLogin(BaseActivity baseActivity) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        if (accountMessage == null) {
            DialogUtils.getAskLoginDialog(baseActivity).show();
            return false;
        }
        return true;
    }

    /**
     * 更改喜爱
     */
    public void editFavourite(EditFavouriteReqBean editFavouriteReqBean) {
        mFavouriteModel.editFavourite(editFavouriteReqBean).retry(2).subscribe();
    }
}
