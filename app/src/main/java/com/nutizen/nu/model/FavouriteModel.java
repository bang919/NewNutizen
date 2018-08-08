package com.nutizen.nu.model;

import android.content.Context;

import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.SPUtils;
import com.nutizen.nu.utils.SubscribeNotificationUtile;

import java.util.ArrayList;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FavouriteModel {

    public Observable<NormalResBean> editFavourite(Context context, EditFavouriteReqBean editFavouriteReqBean) {
        //edit notification subscribe
        if (editFavouriteReqBean.getOperation().contains(EditFavouriteReqBean.EDIT_MARK) && editFavouriteReqBean.getContenttype().contains("contributor")) {
            boolean subscribeKanal = (boolean) SPUtils.get(context, Constants.NOTIFICATION_KANAL, false);
            boolean subscribeLive = (boolean) SPUtils.get(context, Constants.NOTIFICATION_LIVE, false);
            if (subscribeKanal) {
                SubscribeNotificationUtile.subscribeOneContributorVod(editFavouriteReqBean.getTag());
            }
            if (subscribeLive) {
                SubscribeNotificationUtile.subscribeOneContributorLive(editFavouriteReqBean.getTag());
            }
        } else if (editFavouriteReqBean.getOperation().contains(EditFavouriteReqBean.EDIT_UNMARK) && editFavouriteReqBean.getContenttype().contains("contributor")) {
            SubscribeNotificationUtile.unsubscribeOneContributorVod(editFavouriteReqBean.getTag());
            SubscribeNotificationUtile.unsubscribeOneContributorLive(context, editFavouriteReqBean.getTag());
        }
        //API不支持EditFavouriteReqBean多传参数(tag)，修复不能follow Kanal的bug
        EditFavouriteReqBean editBean = new EditFavouriteReqBean();
        editBean.setViewerid(editFavouriteReqBean.getViewerid());
        editBean.setOperation(editFavouriteReqBean.getOperation());
        editBean.setContenttype(editFavouriteReqBean.getContenttype());
        editBean.setContentid(editFavouriteReqBean.getContentid());
        return HttpClient.getApiInterface()
                .editFavourite(editBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TreeMap<String, ArrayList<FavouriteRspBean>>> getFavourites() {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        return HttpClient.getApiInterface()
                .getFavourites("bearer " + accountMessage.getViewer_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ArrayList<FavouriteRspBean>, TreeMap<String, ArrayList<FavouriteRspBean>>>() {
                    @Override
                    public TreeMap<String, ArrayList<FavouriteRspBean>> apply(ArrayList<FavouriteRspBean> favouriteRspBeans) throws Exception {
                        TreeMap<String, ArrayList<FavouriteRspBean>> map = new TreeMap<>();
                        for (FavouriteRspBean favouriteRspBean : favouriteRspBeans) {
                            String type = favouriteRspBean.getViewer_content_type();
                            ArrayList<FavouriteRspBean> tempList = map.get(type);
                            if (tempList == null) {
                                tempList = new ArrayList<>();
                            }
                            tempList.add(favouriteRspBean);
                            map.put(type, tempList);
                        }
                        return map;
                    }
                });
    }

    public Observable<Boolean> checkFavourite(final String type, final int contentId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
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
                return getFavourites().map(new Function<TreeMap<String, ArrayList<FavouriteRspBean>>, Boolean>() {
                    @Override
                    public Boolean apply(TreeMap<String, ArrayList<FavouriteRspBean>> stringArrayListTreeMap) throws Exception {
                        ArrayList<FavouriteRspBean> favouriteRspBeans = stringArrayListTreeMap.get(type);
                        boolean isFavourite = false;
                        if (favouriteRspBeans != null) {
                            for (FavouriteRspBean favouriteRspBean : favouriteRspBeans) {
                                if (favouriteRspBean.getContent_id() == contentId) {
                                    isFavourite = true;
                                    break;
                                }
                            }
                        }
                        return isFavourite;
                    }
                });
            }
        });
    }

    public Observable<Boolean> checkFollow(final int contributorId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
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
                return getFavourites().map(new Function<TreeMap<String, ArrayList<FavouriteRspBean>>, Boolean>() {
                    @Override
                    public Boolean apply(TreeMap<String, ArrayList<FavouriteRspBean>> stringArrayListTreeMap) throws Exception {
                        ArrayList<FavouriteRspBean> favouriteRspBeans = stringArrayListTreeMap.get("contributor");
                        boolean isFavourite = false;
                        if (favouriteRspBeans != null) {
                            for (FavouriteRspBean favouriteRspBean : favouriteRspBeans) {
                                if (favouriteRspBean.getContent_id() == contributorId) {
                                    isFavourite = true;
                                    break;
                                }
                            }
                        }
                        return isFavourite;
                    }
                });
            }
        });
    }
}
