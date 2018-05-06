package com.nutizen.nu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.MainActivity;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.AnimUtil;

public abstract class TransNutizenTitleFragment<P extends BasePresenter> extends BaseDialogFragment<P> implements View.OnClickListener {

    private View mBackBtn;
    public static final String START_FRAM_MAIN = "start from main";
    private boolean startFromMainAct;

    /**
     * @param cls              必须是TransNutizenTitleFragment的子类fragment
     * @param startFromMainAct 是否从MainActivity启动，用来switch MainActivity的动画
     * @return
     */
    public static <T extends TransNutizenTitleFragment> T getInstance(Class<T> cls, boolean startFromMainAct, Bundle bundle) {
        try {
            T transTitleFragment = cls.newInstance();
            bundle.putBoolean(START_FRAM_MAIN, startFromMainAct);
            transTitleFragment.setArguments(bundle);
            return transTitleFragment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            startFromMainAct = arguments.getBoolean(START_FRAM_MAIN);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (startFromMainAct) {
            switchMainIconAppear(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AnimUtil.setViewAlphaAnim(mBackBtn, true, 800);
        if (startFromMainAct) {
            switchMainIconAppear(false);
        }
    }

    private void switchMainIconAppear(boolean appear) {
        Activity activity = getActivity();
        if (activity == null || !(activity instanceof MainActivity)) {
            return;
        }
        if (appear) {
            ((MainActivity) activity).resumeToShowIcons();
        } else {

            ((MainActivity) activity).pauseToHideIcons();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_trans_nu_title;
    }

    @Override
    protected void initView(View rootView) {
        ConstraintLayout include = rootView.findViewById(R.id.include_conslayout);
        LayoutInflater.from(rootView.getContext()).inflate(getBodyLayout(), include, true);
        mBackBtn = rootView.findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);
        initBodyView(rootView);
    }

    protected abstract int getBodyLayout();

    protected abstract void initBodyView(View rootView);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
        }
    }
}
