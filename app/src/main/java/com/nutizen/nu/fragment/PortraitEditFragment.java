package com.nutizen.nu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.listener.ProfileEditSaveListener;
import com.nutizen.nu.presenter.PortraitEditPresenter;
import com.nutizen.nu.utils.GlideUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.PortraitEditView;

import java.io.File;

public class PortraitEditFragment extends BaseDialogFragment<PortraitEditPresenter> implements View.OnClickListener, PortraitEditView {

    public static final String TAG = "PortraitEditFragment";
    public static final String PORTRAIT_STRING = "portrait string";

    private View mProgress;
    private ImageView mPortraitIv;
    private View mDoneBtn;
    private String mChoosePortraitDeletePath;

    private ProfileEditSaveListener mSaveListener;

    public static PortraitEditFragment getInstance(String portraitUrl) {
        PortraitEditFragment portraitEditFragment = new PortraitEditFragment();
        Bundle args = new Bundle();
        args.putString(PORTRAIT_STRING, portraitUrl);
        portraitEditFragment.setArguments(args);
        return portraitEditFragment;
    }

    public void setProfileEditSaveListener(ProfileEditSaveListener listener) {
        mSaveListener = listener;
    }

    @Override
    public void onDetach() {
        if (mChoosePortraitDeletePath != null) {
            mPresenter.deletePhotoFile(mChoosePortraitDeletePath);
        }
        super.onDetach();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_portrait;
    }

    @Override
    protected PortraitEditPresenter initPresenter() {
        return new PortraitEditPresenter(this, this);
    }

    @Override
    protected void initView(View rootView) {
        mProgress = rootView.findViewById(R.id.progress_bar_layout);
        mPortraitIv = rootView.findViewById(R.id.iv_portrait);

        rootView.findViewById(R.id.btn_take_photo).setOnClickListener(this);
        rootView.findViewById(R.id.btn_choose_library).setOnClickListener(this);
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mDoneBtn = rootView.findViewById(R.id.btn_edit);
        mDoneBtn.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        String portraitUrl = getArguments().getString(PORTRAIT_STRING);
        GlideUtils.loadImage(mPortraitIv, R.mipmap.portrait, portraitUrl, new CenterCrop());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                mPresenter.selectTakePhoto();
                break;
            case R.id.btn_choose_library:
                mPresenter.selectAlbum();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_edit:
                mProgress.setVisibility(View.VISIBLE);
                mPresenter.savePortrait();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onError(String error) {
        mProgress.setVisibility(View.GONE);
        ToastUtils.showShort(error);
    }

    @Override
    public void onLuBanSuccess(File picFile) {//这里返回的是LubanCache的图片，至于裁剪的图片已经被删了
        mProgress.setVisibility(View.GONE);
        mChoosePortraitDeletePath = picFile.getPath();
        GlideUtils.loadImage(mPortraitIv, R.mipmap.portrait, picFile.getPath(), new CenterCrop());
        mDoneBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdatePortraitSuccess() {
        mProgress.setVisibility(View.GONE);
        mSaveListener.onProfileEditSaveSuccess();
        dismiss();
    }
}
