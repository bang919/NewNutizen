package com.nutizen.nu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.MainActivity;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.AnimUtil;

public abstract class TextTitleFragment<P extends BasePresenter> extends BaseDialogFragment<P> implements View.OnClickListener {

    private View mBackBtn;
    private TextView mTitleTv;
    public static final String START_FRAM_MAIN = "start from main";
    private boolean startFromMainAct;

    /**
     * @param t                子类fragment实例
     * @param startFromMainAct 是否从MainActivity启动
     */
    public static <T extends TextTitleFragment> T getInstance(T t, boolean startFromMainAct, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean(START_FRAM_MAIN, startFromMainAct);
        t.setArguments(bundle);
        return t;
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
            ((MainActivity) activity).fragmentDestroyToMain();
        } else {
            ((MainActivity) activity).fragmentStartFromMain();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_text_title;
    }

    @Override
    protected void initView(View rootView) {
        ConstraintLayout include = rootView.findViewById(R.id.include_conslayout);
        LayoutInflater.from(rootView.getContext()).inflate(getBodyLayout(), include, true);
        mBackBtn = rootView.findViewById(R.id.back);
        mTitleTv = rootView.findViewById(R.id.title_text_title_fragment);
        if (setNoTitleBackgroundColorSource() != -1) {
            int color = getContext().getResources().getColor(setNoTitleBackgroundColorSource());
            mTitleTv.setBackgroundColor(color);
            rootView.findViewById(R.id.line).setBackgroundColor(color);
        } else if (mTitleTv != null) {
            mTitleTv.setText(setTitle());
        }
        mBackBtn.setOnClickListener(this);
        initBodyView(rootView);
    }

    protected int setNoTitleBackgroundColorSource() {//设置后不会显示标题
        return -1;
    }

    protected String setTitle() {
        return "";
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
