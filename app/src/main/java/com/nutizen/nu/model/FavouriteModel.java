package com.nutizen.nu.model;

import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.presenter.LoginPresenter;

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

    public Observable<NormalResBean> editFavourite(EditFavouriteReqBean editFavouriteReqBean) {
        return HttpClient.getApiInterface()
                .editFavourite(editFavouriteReqBean)
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

    public Observable<Boolean> checkFollow(final String type, final int contributorId) {
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
