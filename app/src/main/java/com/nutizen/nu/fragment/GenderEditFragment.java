package com.nutizen.nu.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.request.ViewDetailReqBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.listener.ProfileEditSaveListener;
import com.nutizen.nu.model.ViewerModel;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GenderEditFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String TAG = "GenderEditFragment";
    public static final String GENDER_INT = "gender int";

    private ViewerModel mViewerModel;
    private CheckBox mMaleBox;
    private CheckBox mFemaleBox;
    private View mProgress;
    private Disposable mRequestDisposable;

    private ProfileEditSaveListener mSaveListener;

    public static GenderEditFragment getInstance(int gender) {
        GenderEditFragment genderEditFragment = new GenderEditFragment();
        Bundle args = new Bundle();
        args.putInt(GENDER_INT, gender);
        genderEditFragment.setArguments(args);
        return genderEditFragment;
    }

    public void setProfileEditSaveListener(ProfileEditSaveListener listener) {
        mSaveListener = listener;
    }

    @Override
    public void onPause() {
        if (mRequestDisposable != null) {
            mRequestDisposable.dispose();
        }
        super.onPause();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_gender;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mMaleBox = rootView.findViewById(R.id.male_checkbox);
        mFemaleBox = rootView.findViewById(R.id.female_checkbox);
        mProgress = rootView.findViewById(R.id.progress_bar_layout);

        mMaleBox.setOnClickListener(this);
        mFemaleBox.setOnClickListener(this);
        rootView.findViewById(R.id.male_tv).setOnClickListener(this);
        rootView.findViewById(R.id.female_tv).setOnClickListener(this);
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.btn_edit).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        int gender = getArguments().getInt(GENDER_INT);
        if (gender == 0) {
            mMaleBox.setChecked(true);
        } else {
            mFemaleBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.male_checkbox:
            case R.id.male_tv:
                mMaleBox.setChecked(true);
                mFemaleBox.setChecked(false);
                break;
            case R.id.female_checkbox:
            case R.id.female_tv:
                mMaleBox.setChecked(false);
                mFemaleBox.setChecked(true);
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_edit:
                mProgress.setVisibility(View.VISIBLE);
                saveGender();
                break;
        }
    }

    private void saveGender() {
        if (mRequestDisposable != null) {
            mRequestDisposable.dispose();
        }
        if (mViewerModel == null) {
            mViewerModel = new ViewerModel();
        }
        int gender = mMaleBox.isChecked() ? 0 : 1;
        final LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        ViewDetailReqBean viewDetailReqBean = new ViewDetailReqBean();
        viewDetailReqBean.setGender(gender);
        mViewerModel.updateViewerDetail(viewDetailReqBean, accountMessage.getViewer_token())
                .subscribe(new Observer<NormalResBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRequestDisposable = d;
                    }

                    @Override
                    public void onNext(NormalResBean normalResBean) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgress.setVisibility(View.GONE);
                        mRequestDisposable = null;
                        ToastUtils.showShort(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        mRequestDisposable = null;
                        mProgress.setVisibility(View.GONE);
                        if (mSaveListener != null) {
                            mSaveListener.onProfileEditSaveSuccess();
                        }
                        dismiss();
                    }
                });
    }
}
