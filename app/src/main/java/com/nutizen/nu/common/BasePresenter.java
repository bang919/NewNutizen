package com.nutizen.nu.common;

import android.content.Context;

import com.nutizen.nu.utils.ExceptionUtil;
import com.nutizen.nu.utils.LogUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by bigbang on 2017/5/18.
 * presenter 基类
 */

public class BasePresenter<V> {

    protected V mView;
    protected Context mContext;

    public BasePresenter(Context context, V view) {
        mView = view;
        mContext = context;
    }

    protected  <T> void subscribeNetworkTask(String observerTag, Observable<T> observable, MyObserver<T> myObserver) {
        Observer observer = createObserver(observerTag, myObserver);
        observable.subscribe(observer);
    }


    public void destroy() {
        removeAllObserver();
        if (mView != null) {
            mView = null;
        }
    }


    /**
     * --------------------------------- DisposableMap ---------------------------------
     */

    private HashMap<String, Disposable> mDisposableMap = new HashMap<>();

    private void putObserver(String observerTag, Disposable disposable) {
        mDisposableMap.put(observerTag, disposable);
    }

    public void removeObserver(String observerTag) {
        Disposable d = mDisposableMap.get(observerTag);
        if (d != null && !d.isDisposed()) {
            d.dispose();
            mDisposableMap.remove(observerTag);
        }
        mDisposableMap.remove(observerTag);
    }

    public void removeAllObserver() {
        Iterator<Map.Entry<String, Disposable>> iterator = mDisposableMap.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().dispose();
        }
        mDisposableMap.clear();
    }

    public void cancelAllRequest() {
        removeAllObserver();
    }

    public <T> Observer<T> createObserver(final String observerTag, final MyObserver<T> observer) {

        removeObserver(observerTag);

        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                putObserver(observerTag, d);
            }

            @Override
            public void onNext(T t) {
                observer.onMyNext(t);
            }

            @Override
            public void onError(Throwable e) {
                String errorMessage = ExceptionUtil.getHttpExceptionMessage(e);
                observer.onMyError(errorMessage);
                removeObserver(observerTag);
                LogUtils.d("okhttp", "error - " + errorMessage);
            }

            @Override
            public void onComplete() {
                removeObserver(observerTag);
            }
        };
    }

    public interface MyObserver<T> {
        void onMyNext(T t);

        void onMyError(String errorMessage);
    }
}
