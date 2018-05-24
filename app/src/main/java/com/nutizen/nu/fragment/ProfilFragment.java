package com.nutizen.nu.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.listener.ProfileEditSaveListener;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.presenter.ProfilPresenter;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.view.ProfileView;

public class ProfilFragment extends TextTitleFragment<ProfilPresenter> implements ProfileView, ProfileEditSaveListener {

    public static final String TAG = "ProfilFragment";
    private ImageView mPortrait;
    private TextView mNameTv;
    private TextView mBioDes, mEmailDes, mGenderDes;
    private LoginResponseBean.DetailBean mDetailBean;

    public static ProfilFragment getInstance() {
        return new ProfilFragment();
    }

    @Override
    protected int setNoTitleBackgroundColorSource() {
        return R.color.colorBlack;
    }

    @Override
    protected int getBodyLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initBodyView(View rootView) {
        mPortrait = rootView.findViewById(R.id.portrait);
        mNameTv = rootView.findViewById(R.id.tv_name);
        mBioDes = rootView.findViewById(R.id.des_bio);
        mEmailDes = rootView.findViewById(R.id.des_email);
        mGenderDes = rootView.findViewById(R.id.des_gender);

        rootView.findViewById(R.id.edit_portrait).setOnClickListener(this);
        rootView.findViewById(R.id.edit_bio).setOnClickListener(this);
        rootView.findViewById(R.id.edit_gender).setOnClickListener(this);

        refreshProfileMessage();
    }

    private void refreshProfileMessage() {
        mDetailBean = LoginPresenter.getAccountMessage().getDetail();

        GlideUtils.loadImage(mPortrait, R.mipmap.portrait, mDetailBean.getViewer_thumbnail(), new CenterCrop(), new CircleCrop());

        mNameTv.setText(mDetailBean.getViewer_username());

        if (!TextUtils.isEmpty(mDetailBean.getViewer_email())) {
            mEmailDes.setText(mDetailBean.getViewer_email());
        }

        if (!TextUtils.isEmpty(mDetailBean.getViewer_address())) {
            mBioDes.setText(mDetailBean.getViewer_address());
        }

        mGenderDes.setText(getString(mDetailBean.getViewer_gender() == 0 ? R.string.male : R.string.female));

    }

    @Override
    protected ProfilPresenter initPresenter() {
        return new ProfilPresenter(getContext(), this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.edit_portrait:
                PortraitEditFragment portraitEditFragment = PortraitEditFragment.getInstance(mDetailBean.getViewer_thumbnail());
                portraitEditFragment.setProfileEditSaveListener(this);
                portraitEditFragment.show(getFragmentManager(), PortraitEditFragment.TAG);
                break;
            case R.id.edit_bio:
                BioEditFragment bioEditFragment = BioEditFragment.getInstance(mDetailBean.getViewer_address());
                bioEditFragment.setBioSaveListener(this);
                bioEditFragment.show(getFragmentManager(), BioEditFragment.TAG);
                break;
            case R.id.edit_gender:
                GenderEditFragment genderEditFragment = GenderEditFragment.getInstance(mDetailBean.getViewer_gender());
                genderEditFragment.setProfileEditSaveListener(this);
                genderEditFragment.show(getFragmentManager(), genderEditFragment.TAG);
                break;
        }
    }

    @Override
    public void onProfileEditSaveSuccess() {
        refreshProfileMessage();
    }
}
