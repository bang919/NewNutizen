package com.nutizen.nu.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.nutizen.nu.adapter.BasePlayerAdapter;
import com.nutizen.nu.adapter.LivePlayerAdapter;
import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.presenter.LivePlayerActivityPresenter;
import com.nutizen.nu.view.LivePlayerActivityView;

public class LivePlayerActivity extends PlayerActivity<LiveResponseBean, LivePlayerActivityPresenter> implements LivePlayerActivityView {

    public static void startLivePlayActivity(Context context, LiveResponseBean liveResponseBean) {
        Intent intent = new Intent(context, LivePlayerActivity.class);
        intent.putExtra(LivePlayerActivity.DATA_BEAN, liveResponseBean);
        context.startActivity(intent);
    }

    @Override
    protected LiveResponseBean setDataBean() {
        LiveResponseBean liveBean = (LiveResponseBean) getIntent().getSerializableExtra(DATA_BEAN);
        mPresenter.setTitleAndUrl(liveBean.getTitle(), liveBean.getUrl());
        mPresenter.preparePlayer();
        return liveBean;
    }

    @Override
    protected BasePlayerAdapter createBasePlayerAdapter() {
        return new LivePlayerAdapter(mData);
    }

    @Override
    protected LivePlayerActivityPresenter initPresenter() {
        return new LivePlayerActivityPresenter(this, this);
    }

    @Override
    protected String returnCommentType() {
        return mData.getType();
    }

    @Override
    protected int returnCommentId() {
        return mData.getId();
    }

    @Override
    protected View setFavouriteBtn() {
        return ((LivePlayerAdapter) mBasePlayerAdapter).getFollowBtn();
    }

    @Override
    protected void editFavourite(EditFavouriteReqBean editFavouriteReqBean) {
        mPresenter.editFavourite(editFavouriteReqBean);
    }

    @Override
    public void isFollow(boolean isFollow) {
        setIsFavourite(isFollow);
    }
}
