package com.nutizen.nu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.KanalDetailPresenter;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.KanalDetailView;

import java.util.ArrayList;

public class KanalDetailFragment extends TransNutizenTitleFragment<KanalDetailPresenter> implements KanalDetailView {

    public static final String TAG = "KanalDetailFragment";
    public static final String KANAL_BEAN_DETAIL = "kanal_bean_detail";

    private KanalRspBean.SearchBean mKanalBean;
    private View mRootView;
    private ImageView mBannerIv;
    private ImageView mFollowBt;

    private boolean initFollow;
    private Runnable followRunnable;
    private Handler mHandler;

    public static KanalDetailFragment getInstance(boolean isFromMainAct, Bundle bundle) {
        return getInstance(KanalDetailFragment.class, isFromMainAct, bundle);
    }

    @Override
    public void onPause() {
        checkFollowRequest(true);
        super.onPause();
    }

    @Override
    protected int getBodyLayout() {
        return R.layout.fragment_kanal_detail;
    }

    @Override
    protected void initBodyView(View rootView) {
        mRootView = rootView;
        mBannerIv = rootView.findViewById(R.id.kanal_banner);
        mFollowBt = rootView.findViewById(R.id.follow_bt);
    }

    @Override
    protected KanalDetailPresenter initPresenter() {
        return new KanalDetailPresenter(getContext(), this);
    }

    @Override
    protected void initEvent() {
        mHandler = new Handler();
        initData();

        /**
         * setOnClickListener
         */
        mFollowBt.setOnClickListener(this);

        followRunnable = new Runnable() {
            @Override
            public void run() {
                LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
                boolean isSelected = mFollowBt.isSelected();
                if (accountMessage != null && initFollow != isSelected) {
                    initFollow = isSelected;

                    EditFavouriteReqBean editFavouriteReqBean = new EditFavouriteReqBean();
                    editFavouriteReqBean.setContentid(mKanalBean.getViewer_id());
                    editFavouriteReqBean.setContenttype(mKanalBean.getType());
                    editFavouriteReqBean.setViewerid(accountMessage.getViewer_id());
                    editFavouriteReqBean.setOperation(isSelected ? EditFavouriteReqBean.EDIT_MARK : EditFavouriteReqBean.EDIT_UNMARK);
                    mPresenter.editFavourite(editFavouriteReqBean);
                }
            }
        };
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mKanalBean = (KanalRspBean.SearchBean) arguments.getSerializable(KANAL_BEAN_DETAIL);
        }

        if (mKanalBean != null) {
            GlideUtils.loadImage(mBannerIv, -1, mKanalBean.getThumbnail(), new CenterCrop());
            mPresenter.getDatas(mKanalBean);

        } else {
            onFailure(getContext().getString(R.string.loadfail));
        }
    }


    @Override
    public void onContentResult(ArrayList<ContentResponseBean.SearchBean> contentResponseBeans) {

    }

    @Override
    public void onLiveResullt(ArrayList<LiveResponseBean> liveResponseBeans) {

    }

    @Override
    public void onSuccess() {
        setLoadingVisibility(false);
    }

    @Override
    public void onFailure(String errorMsg) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void isFollow(boolean isFollow) {
        initFollow = isFollow;
        mFollowBt.setSelected(isFollow);
    }

    private void setLoadingVisibility(boolean visibility) {
        mRootView.findViewById(R.id.progress_bar_layout).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.follow_bt:
                if (mPresenter.checkLogin((BaseActivity) getContext())) {
                    v.setSelected(!v.isSelected());
                    checkFollowRequest(false);
                }
                break;
        }
    }

    private void checkFollowRequest(boolean withoutDelay) {//看看是否需要请求更改喜爱状态
        mHandler.removeCallbacks(followRunnable);
        mHandler.postDelayed(followRunnable, withoutDelay ? 0 : 3000);//3秒内连续按无效
    }
}
