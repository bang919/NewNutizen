package com.nutizen.nu.presenter;

import android.content.Context;

import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.model.ContentModel;
import com.nutizen.nu.view.MoreHomeView;

public class MoreHomePresenter extends BasePresenter<MoreHomeView> {

    private ContentModel mContentModel;
    private final int mPageCount = 9;
    private int mCurrentPage = 1;

    public MoreHomePresenter(Context context, MoreHomeView view) {
        super(context, view);
        mContentModel = new ContentModel();
    }

    public void requestEditors() {
        String observerTag = getClass().getName().concat("requestEditors");
        subscribeNetworkTask(observerTag, mContentModel.requestEditors(), new MyObserver<ContentResponseBean>() {
            @Override
            public void onMyNext(ContentResponseBean contentResponseBean) {
                mView.onContentDatas(contentResponseBean);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onDataRequestFailure(errorMessage);
            }
        });
    }

    public void requestNewlys() {
        mCurrentPage = 0;
        requestMoreNewlys();
    }

    public void requestMoreNewlys() {
        if (mCurrentPage == -1) {
            mView.noMoreData();
            return;
        }
        if (mCurrentPage != 0) {
            mView.requestingMoreData();
        }
        String observerTag = getClass().getName().concat("requestNewlys");
        subscribeNetworkTask(observerTag, mContentModel.requestNewly(mPageCount, mCurrentPage * mPageCount), new MyObserver<ContentResponseBean>() {
            @Override
            public void onMyNext(ContentResponseBean contentResponseBean) {
                if (mCurrentPage == 0) {
                    mView.onContentDatas(contentResponseBean);
                } else {
                    mView.onMoreContentDatas(contentResponseBean);
                }
                if (contentResponseBean.getSearch() == null || contentResponseBean.getSearch().size() < mPageCount) {
                    mCurrentPage = -1;
                    mView.noMoreData();
                } else {
                    mCurrentPage++;
                }
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onDataRequestFailure(errorMessage);
            }
        });
    }
}
