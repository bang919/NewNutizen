package com.nutizen.nu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

public class BioEditFragment extends BaseDialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {

    public static final String TAG = "BioEditFragment";
    public static final String BIO_STRING = "bio string";

    private ViewerModel mViewerModel;
    private EditText mBioEditText;
    private TextView mBioLimit;
    private View mProgress;
    private Disposable mRequestDisposable;
    private ProfileEditSaveListener mBioSaveListener;


    public static BioEditFragment getInstance(String bio) {
        BioEditFragment bioEditFragment = new BioEditFragment();
        Bundle args = new Bundle();
        args.putString(BIO_STRING, bio);
        bioEditFragment.setArguments(args);
        return bioEditFragment;
    }

    public void setBioSaveListener(ProfileEditSaveListener listener) {
        mBioSaveListener = listener;
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
        return R.layout.fragment_edit_bio;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mBioEditText = rootView.findViewById(R.id.etext_bio);
        mBioLimit = rootView.findViewById(R.id.bio_limit);
        mProgress = rootView.findViewById(R.id.progress_bar_layout);

        rootView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.btn_edit).setOnClickListener(this);

        showSoftInput();
    }

    private void showSoftInput() {
        if (mBioEditText != null) {
            mBioEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mBioEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(mBioEditText, 0);
                        }
                    }, 300);
                    mBioEditText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    @Override
    protected void initEvent() {
        String bio = getArguments().getString(BIO_STRING);
        if (!TextUtils.isEmpty(bio)) {
            mBioEditText.setText(bio);
            mBioEditText.setSelection(bio.length());
            mBioLimit.setText(bio.length() + "/200");
        }

        mBioEditText.setOnEditorActionListener(this);//禁止换行
        mBioEditText.addTextChangedListener(new TextWatcher() {//统计字数
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBioLimit.setText(s.length() + "/200");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_edit:
                mProgress.setVisibility(View.VISIBLE);
                saveBioEdit();
                break;
        }
    }

    private void saveBioEdit() {
        if (mRequestDisposable != null) {
            mRequestDisposable.dispose();
        }
        if (mViewerModel == null) {
            mViewerModel = new ViewerModel();
        }
        final String bio = mBioEditText.getText().toString();
        final LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        ViewDetailReqBean viewDetailReqBean = new ViewDetailReqBean();
        viewDetailReqBean.setAddress(bio);

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
                        if (mBioSaveListener != null) {
                            mBioSaveListener.onProfileEditSaveSuccess();
                        }
                        dismiss();
                    }
                });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }
}
