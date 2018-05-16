package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.model.FavouriteModel;
import com.nutizen.nu.model.KanalModel;
import com.nutizen.nu.view.FavouriteView;

import java.util.ArrayList;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class FavouritePresenter extends BasePresenter<FavouriteView> {

    private FavouriteModel mFavouriteModel;
    private ContentModel mContentModel;
    private KanalModel mKanalModel;

    public FavouritePresenter(Context context, FavouriteView view) {
        super(context, view);
        mFavouriteModel = new FavouriteModel();
        mContentModel = new ContentModel();
        mKanalModel = new KanalModel();
    }

    public void requestFavouriteContent() {
        subscribeNetworkTask(getClass().getName().concat("requestFavouriteContent"), mFavouriteModel.getFavourites(), new MyObserver<TreeMap<String, ArrayList<FavouriteRspBean>>>() {
            @Override
            public void onMyNext(TreeMap<String, ArrayList<FavouriteRspBean>> favouriteMap) {
                mView.onFavouriteResponse(favouriteMap);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }


    public void removeFavourites(ArrayList<FavouriteRspBean> favouriteRspBeans) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        Observable<NormalResBean>[] editFavouriteReqBeans = new Observable[favouriteRspBeans.size()];
        for (int i = 0; i < favouriteRspBeans.size(); i++) {
            FavouriteRspBean bean = favouriteRspBeans.get(i);
            EditFavouriteReqBean editFavouriteReqBean = new EditFavouriteReqBean();
            editFavouriteReqBean.setContentid(bean.getContent_id());
            editFavouriteReqBean.setContenttype(bean.getViewer_content_type());
            editFavouriteReqBean.setViewerid(accountMessage.getViewer_id());
            editFavouriteReqBean.setOperation(EditFavouriteReqBean.EDIT_UNMARK);
            editFavouriteReqBeans[i] = mFavouriteModel.editFavourite(editFavouriteReqBean);
        }

        Observable<String> zip = Observable.zipArray(new Function<Object[], String>() {

            @Override
            public String apply(Object[] editFavouriteReqBeans) throws Exception {
                return "edit success";
            }
        }, false, 1, editFavouriteReqBeans);
        subscribeNetworkTask(getClass().getName().concat("removeFavourites"), zip, new MyObserver<String>() {
            @Override
            public void onMyNext(String s) {
                requestFavouriteContent();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onFailure(errorMessage);
            }
        });
    }
}
