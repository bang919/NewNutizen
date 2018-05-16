package com.nutizen.nu.fragment;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.GlideUtils;

public class LeftFavouriteFragment extends TextTitleFragment {

    public static final String TAG = "LeftFavouriteFragment";

    public static LeftFavouriteFragment getInstance() {
        return getInstance(new LeftFavouriteFragment(), true, null);
    }

    @Override
    protected int getBodyLayout() {
        return R.layout.fragment_left_favourite;
    }

    @Override
    protected String setTitle() {
        return getContext().getString(R.string.favourites);
    }

    @Override
    protected void initBodyView(View rootView) {
        ImageView contentIv = rootView.findViewById(R.id.iv_button_contents);
        //这里xml设置会模糊，因为ConstraintLayout做了些缩放
        GlideUtils.loadImage(contentIv, -1, R.mipmap.favourite_contents, new CenterCrop());
        contentIv.setOnClickListener(this);
        ImageView kanalIv = rootView.findViewById(R.id.iv_button_kanals);
        GlideUtils.loadImage(kanalIv, -1, R.mipmap.favourite_kanals, new CenterCrop());
        kanalIv.setOnClickListener(this);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_button_contents:
                FavouriteContentFragment.getInstance().show(getFragmentManager(), FavouriteContentFragment.TAG);
                break;
            case R.id.iv_button_kanals:
                FavouriteContributorFragment.getInstance().show(getFragmentManager(), FavouriteContributorFragment.TAG);
                break;
        }
    }
}
