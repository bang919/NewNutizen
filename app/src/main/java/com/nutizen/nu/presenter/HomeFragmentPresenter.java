package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.LiveModel;
import com.nutizen.nu.view.HomeFragmentView;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bigbang on 2018/4/11.
 */

public class HomeFragmentPresenter extends BasePresenter<HomeFragmentView> {

    public final String TAG = "HomeFragmentPresenter";
    private ContentModel mContentModel;
    private LiveModel mLiveModel;

    public HomeFragmentPresenter(Context context, HomeFragmentView view) {
        super(context, view);
        mContentModel = new ContentModel();
        mLiveModel = new LiveModel();
    }

    public void requestDatas() {
        Observable<ContentResponseBean> source1 = mContentModel.requestHomeBanner()
                .doOnNext(new Consumer<ContentResponseBean>() {
                    @Override
                    public void accept(ContentResponseBean contentResponseBean) throws Exception {
                        mView.onBannerData(contentResponseBean);
                    }
                });
        Observable<ContentResponseBean> source2 = mContentModel.requestEditors().map(new Function<ContentResponseBean, ContentResponseBean>() {
            @Override
            public ContentResponseBean apply(ContentResponseBean contentResponseBean) throws Exception {
                ArrayList<ContentResponseBean.SearchBean> searchs = contentResponseBean.getSearch();

                Pattern pattern = Pattern.compile("editors[0-9]");
                TreeMap<Integer, ContentResponseBean.SearchBean> map = new TreeMap<>();

                for (ContentResponseBean.SearchBean searchBean : searchs) {
                    String[] genres = searchBean.getGenres().split(",");
                    for (String genre : genres) {
                        if (pattern.matcher(genre).matches()) {
                            map.put(Integer.valueOf(genre.replace("editors", "")), searchBean);
                            break;
                        }
                    }
                }

                if (map.size() > 0) {
                    contentResponseBean.setSearch(new ArrayList(map.values()));
                }

                mView.onEditorsData(contentResponseBean);
                return contentResponseBean;
            }
        });
        Observable<ContentResponseBean> source3 = mContentModel.requestNewly(9, 0)
                .doOnNext(new Consumer<ContentResponseBean>() {
                    @Override
                    public void accept(ContentResponseBean contentResponseBean) throws Exception {
                        mView.onNewlyData(contentResponseBean);
                    }
                });
        Observable<ArrayList<LiveResponseBean>> source4 = mLiveModel.requestLive().map(new Function<ArrayList<LiveResponseBean>, ArrayList<LiveResponseBean>>() {
            @Override
            public ArrayList<LiveResponseBean> apply(ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                ArrayList<LiveResponseBean> result = new ArrayList<>();
                for (LiveResponseBean bean : liveResponseBeans) {
                    if (bean.getSynopsis().contains(";")) {
                        result.add(bean);
                    }
                }
                mView.onLiveData(result);
                return result;
            }
        });

        Observable<String> zip = Observable.zip(source1, source2, source3, source4,
                new Function4<ContentResponseBean, ContentResponseBean, ContentResponseBean, ArrayList<LiveResponseBean>, String>() {
                    @Override
                    public String apply(ContentResponseBean contentResponseBean, ContentResponseBean contentResponseBean2,
                                        ContentResponseBean contentResponseBean3, ArrayList<LiveResponseBean> liveResponseBeans) throws Exception {
                        return "Success";
                    }
                })
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        final String observerTag = getClass().getName() + "requestDatas";
        subscribeNetworkTask(observerTag, zip, new MyObserver<String>() {
            @Override
            public void onMyNext(String s) {
                mView.onDataRequestSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onDataRequestFailure(errorMessage);
            }
        });
    }
}
