package com.nutizen.nu.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.ShareAdapter;
import com.nutizen.nu.common.BasePresenter;

public class ShareFragment extends BaseDialogFragment implements ShareAdapter.OnShareAdapterClickListener {

    public static final String TAG = "ShareFragment";
    private OnSharePlatformClickListener mOnSharePlatformClickListener;
    private RecyclerView mShareRv;

    public static ShareFragment getInstance() {
        return new ShareFragment();
    }

    public void setOnSharePlatformClickListener(OnSharePlatformClickListener listener) {
        mOnSharePlatformClickListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setWindowAnimations(0);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_share;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mShareRv = rootView.findViewById(R.id.rv_share);
        rootView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initEvent() {
        mShareRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mShareRv.setAdapter(new ShareAdapter(this));
    }


    @Override
    public void onShareAdapterClick(String plateName) {
        if (mOnSharePlatformClickListener != null) {
            mOnSharePlatformClickListener.onSharePlatformClick(plateName);
            dismiss();
        }
    }

    public interface OnSharePlatformClickListener {
        void onSharePlatformClick(String platformName);
    }
}
